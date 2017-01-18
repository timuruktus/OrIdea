package ru.timuruktus.oridea.Events.ToAuthPresenter;


public class OnLoginButtonClickEvent {

    public String email,pass;

    public OnLoginButtonClickEvent(String email, String pass){
        this.email = email;
        this.pass = pass;
    }
}
