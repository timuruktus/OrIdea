package ru.timuruktus.oridea.Presenter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    @Subscribe
    public void onLeftMenuButtonClick(LeftMenuClickEvent event){
        DrawerLayout drawer = event.drawer;
        MenuItem menuItem = event.menuItem;
        Fragment currentFragment = event.currentFragment;
        FragmentManager fragmentManager = event.fragmentManager;
        if(!NetworkChangeReceiver.internet){
            Toast.makeText(drawer.getContext(), R.string.disabled_network, Toast.LENGTH_LONG).show();
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        int id = menuItem.getItemId();
        if(id != currentFragment.getId()) {
            if (id == R.id.news_menu) {
                changeFragment(fragmentManager, new WelcomeFragment(), true);
            } else if (id == R.id.post_menu) {
                changeFragment(fragmentManager, new PushPostFragment(), true);
            } else if (id == R.id.settings_menu) {

            } else if (id == R.id.registration_menu) {
                changeFragment(fragmentManager, new AuthFragment(), true);
            } else if (id == R.id.logout_menu){
                FirebaseAuth.getInstance().signOut();
                changeFragment(fragmentManager, new WelcomeFragment(), false);
                EventBus.getDefault().post(new HideLogoutEvent(true));
            }
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    public void changeFragment(FragmentManager fragmentManager, Fragment fragment,
                               boolean addToBachStack){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(addToBachStack) {fragmentTransaction.addToBackStack(null);}
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}

