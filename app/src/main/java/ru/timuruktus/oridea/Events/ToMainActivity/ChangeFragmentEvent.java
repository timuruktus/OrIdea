package ru.timuruktus.oridea.Events.ToMainActivity;


import android.app.Fragment;

public class ChangeFragmentEvent {

    public Fragment fragment;
    public boolean addToBackStack;

    public ChangeFragmentEvent(Fragment fragment, boolean addToBackStack) {
        this.fragment = fragment;
        this.addToBackStack = addToBackStack;
    }
}
