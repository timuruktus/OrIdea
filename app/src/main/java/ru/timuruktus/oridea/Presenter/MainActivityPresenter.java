package ru.timuruktus.oridea.Presenter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.timuruktus.oridea.Events.EventCallbacks.CheckInternetConnectionCallback;
import ru.timuruktus.oridea.Events.Global.CheckUserAuth;
import ru.timuruktus.oridea.Events.EventCallbacks.CheckUserAuthCallback;
import ru.timuruktus.oridea.Events.ToMainActivity.ChangeFragmentEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.CheckInternetConnectionEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.HideLogoutEvent;
import ru.timuruktus.oridea.Events.ToMainActivityPresenter.LeftMenuClickEvent;
import ru.timuruktus.oridea.R;
import ru.timuruktus.oridea.View.Fragments.AuthFragment;
import ru.timuruktus.oridea.View.Fragments.PushPostFragment;
import ru.timuruktus.oridea.View.Fragments.WelcomeFragment;

public class MainActivityPresenter {

    public MainActivityPresenter(){
        EventBus.getDefault().register(this);
    }

    DrawerLayout drawer;
    MenuItem menuItem;
    Fragment currentFragment;
    FragmentManager fragmentManager;

    @Subscribe
    public void onLeftMenuButtonClick(LeftMenuClickEvent event){
        this.drawer = event.drawer;
        this.menuItem = event.menuItem;
        this.currentFragment = event.currentFragment;
        this.fragmentManager = event.fragmentManager;
        EventBus.getDefault().post(new CheckInternetConnectionEvent());
    }

    @Subscribe
    public void chechInternetConnectionCallback(CheckInternetConnectionCallback event){
        if(!event.isConnected){
            Toast.makeText(drawer.getContext(), R.string.disabled_network, Toast.LENGTH_LONG).show();
            drawer.closeDrawer(GravityCompat.START);
            return;
        }else{
            EventBus.getDefault().post(new CheckUserAuth());
            int id = menuItem.getItemId();
            if(id != currentFragment.getId()) {
                if (id == R.id.news_menu) {
                    EventBus.getDefault().post(new ChangeFragmentEvent(new WelcomeFragment(), true));
                } else if (id == R.id.post_menu) {
                    EventBus.getDefault().post(new ChangeFragmentEvent(new PushPostFragment(), true));
                } else if (id == R.id.settings_menu) {

                } else if (id == R.id.registration_menu) {
                    EventBus.getDefault().post(new ChangeFragmentEvent(new AuthFragment(), true));
                } else if (id == R.id.logout_menu){
                    FirebaseAuth.getInstance().signOut();
                    EventBus.getDefault().post(new ChangeFragmentEvent(new WelcomeFragment(), false));
                    EventBus.getDefault().post(new HideLogoutEvent(true));
                }
            }
            drawer.closeDrawer(GravityCompat.START);
        }
    }



    @Subscribe
    public void checkUserAuthCallback(CheckUserAuthCallback event){
        EventBus.getDefault().post(new HideLogoutEvent(!event.auth));
        Log.d("tag", "HideLogoutEvent was sended.");
    }


}

