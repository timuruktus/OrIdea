package ru.timuruktus.oridea.Events.ToEmailAuth;


public class StartLogEvent {

    public String email;
    public String pass;

    public StartLogEvent(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }
}
