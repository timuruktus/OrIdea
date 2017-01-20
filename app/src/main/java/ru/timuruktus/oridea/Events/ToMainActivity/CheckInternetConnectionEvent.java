package ru.timuruktus.oridea.Events.ToMainActivity;


import org.greenrobot.eventbus.EventBus;

import ru.timuruktus.oridea.Events.EventCallbacks.CheckInternetConnectionCallback;

public class CheckInternetConnectionEvent {

    public boolean isConnected;

    public void setConnected(boolean isConnected){
        this.isConnected = isConnected;
        EventBus.getDefault().post(new CheckInternetConnectionCallback(isConnected));
    }
}
