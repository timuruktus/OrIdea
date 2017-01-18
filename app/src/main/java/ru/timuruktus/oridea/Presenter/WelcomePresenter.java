package ru.timuruktus.oridea.Presenter;


import org.greenrobot.eventbus.EventBus;

public class WelcomePresenter {


    public WelcomePresenter(){
        EventBus.getDefault().register(this);
    }
}
