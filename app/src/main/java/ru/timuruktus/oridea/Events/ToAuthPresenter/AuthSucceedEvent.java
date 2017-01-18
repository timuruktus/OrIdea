package ru.timuruktus.oridea.Events.ToAuthPresenter;


public class AuthSucceedEvent {

    public boolean itWasLogin;

    public AuthSucceedEvent(boolean itWasLogin) {
        this.itWasLogin = itWasLogin;
    }
}
