package ru.timuruktus.oridea.Events.ToMainActivity;


import android.app.Fragment;

public class ChangeFragmentEvent {

    public Fragment fragment;

    public ChangeFragmentEvent(Fragment fragment) {
        this.fragment = fragment;
    }
}
