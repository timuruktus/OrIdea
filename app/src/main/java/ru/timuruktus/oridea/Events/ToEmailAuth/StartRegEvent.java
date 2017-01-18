package ru.timuruktus.oridea.Events.ToEmailAuth;


public class StartRegEvent {

    public String email,pass;

    public StartRegEvent(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }
}
