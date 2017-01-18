package ru.timuruktus.oridea.View.Activities;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.google.firebase.auth.FirebaseAuth;

import ru.timuruktus.oridea.R;

/**
 * CREATED IN 05 JANUARY 2017
 * IN 23:18(YEKATERINBURG)
 * BY KHASANOV TIMUR (16)
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    public final static String TAG = "tag";
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    public static NavigationView navigationView;
    private ContextMenu menu;
    private Toolbar toolbar;
    TextView userEmail, userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


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
        mainActivityPresenter.onLeftMenuButClick(item, fragmentManager, drawer);

        return true;
    }

    @Override
    public void onClick(View v) {

    }


    public Fragment getCurrentFragment() {
        Fragment frag = getFragmentManager().findFragmentById(R.id.fragmentContainer);
        return frag;
    }


    public void changeToolbarTitle(int resId) {
        toolbar.setTitle(resId);
    }

    public Activity getActivity() {
        return this;
    }


}
