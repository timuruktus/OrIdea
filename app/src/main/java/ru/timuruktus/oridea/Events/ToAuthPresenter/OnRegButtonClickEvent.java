package ru.timuruktus.oridea.Events.ToAuthPresenter;


public class OnRegButtonClickEvent {

    public String email,pass;

    public OnRegButtonClickEvent(String email, String pass){
        this.email = email;
        this.pass = pass;
    }
}
