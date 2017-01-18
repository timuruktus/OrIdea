package ru.timuruktus.oridea.Events.ToMainActivityPresenter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

public class LeftMenuClickEvent {

    public MenuItem menuItem;
    public FragmentManager fragmentManager;
    public DrawerLayout drawer;
    public Fragment currentFragment;

    public LeftMenuClickEvent(MenuItem menuItem, FragmentManager fragmentManager, DrawerLayout drawer,
                              Fragment currentFragment){
        this.menuItem = menuItem;
        this.fragmentManager = fragmentManager;
        this.drawer = drawer;
        this.currentFragment = currentFragment;
    }
}
