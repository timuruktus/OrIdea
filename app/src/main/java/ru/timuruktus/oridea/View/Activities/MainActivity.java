package ru.timuruktus.oridea.View.Activities;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.timuruktus.oridea.Events.Global.CheckUserAuth;
import ru.timuruktus.oridea.Events.ToAuthPresenter.AuthSucceedEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.ChangeFragmentEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.ChangeToolbarTitleEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.CheckInternetConnectionEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.HideLogoutEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.ShowNetworkErrorEvent;
import ru.timuruktus.oridea.Events.ToMainActivityPresenter.LeftMenuClickEvent;
import ru.timuruktus.oridea.Model.EmailAuth;
import ru.timuruktus.oridea.Model.UploadFiles;
import ru.timuruktus.oridea.Presenter.AuthPresenter;
import ru.timuruktus.oridea.Presenter.MainActivityPresenter;
import ru.timuruktus.oridea.Presenter.PushPostPresenter;
import ru.timuruktus.oridea.Presenter.WelcomePresenter;
import ru.timuruktus.oridea.R;
import ru.timuruktus.oridea.View.Fragments.WelcomeFragment;

/**
 * CREATED IN 05 JANUARY 2017
 * IN 23:18(YEKATERINBURG)
 * BY KHASANOV TIMUR (16)
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public final static String TAG = "tag";
    private DrawerLayout drawer;
    public static NavigationView navigationView;
    private Toolbar toolbar;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initAllListeners();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ru.timuruktus.oridea.View.Fragments.WelcomeFragment welcomeFragment = new ru.timuruktus.oridea.View.Fragments.WelcomeFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, welcomeFragment);
        fragmentTransaction.commit();

        // TOOLBAR AND ETC.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        verifyStoragePermissions(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        EventBus.getDefault().post(new LeftMenuClickEvent(item, fragmentManager, drawer,
                getCurrentFragment()));

        return true;
    }

    @Override
    public void onClick(View v) {

    }


    @Subscribe
    public void changeToolbarTitle(ChangeToolbarTitleEvent event) {
        toolbar.setTitle(event.resId);
    }


    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new CheckUserAuth());
        super.onStart();

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    public void initAllListeners(){
        AuthPresenter authPresenter = new AuthPresenter();
        MainActivityPresenter mainActivityPresenter = new MainActivityPresenter();
        PushPostPresenter pushPostPresenter = new PushPostPresenter();
        WelcomePresenter welcomePresenter = new WelcomePresenter();
        EmailAuth emailAuth = new EmailAuth();
        UploadFiles uploadFiles = new UploadFiles();
    }

    public Fragment getCurrentFragment() {
        Fragment frag = getFragmentManager().findFragmentById(R.id.fragmentContainer);
        return frag;
    }

    @Subscribe
    public void hideLogout(HideLogoutEvent event){
        if(event.hide){
            MainActivity.navigationView.getMenu().findItem(R.id.logout_menu).setVisible(false);
            MainActivity.navigationView.getMenu().findItem(R.id.registration_menu).setVisible(true);
        }
        else{
            MainActivity.navigationView.getMenu().findItem(R.id.logout_menu).setVisible(true);
            MainActivity.navigationView.getMenu().findItem(R.id.registration_menu).setVisible(false);
        }
    }

    @Subscribe
    public void changeFragment(ChangeFragmentEvent event){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(event.addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.fragmentContainer, event.fragment);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void showNetworkError(ShowNetworkErrorEvent event){
        Toast.makeText(drawer.getContext(), R.string.disabled_network, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void checkConnection(CheckInternetConnectionEvent event) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        event.setConnected(netInfo != null && netInfo.isConnectedOrConnecting());
    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
