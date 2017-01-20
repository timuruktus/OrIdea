package ru.timuruktus.oridea.Events.Global;


public class ShowErrorEvent {

    public enum Action{
        DIDNT_VERIFY_EMAIL
    }

    public Action action;

    public ShowErrorEvent(Action action){
        this.action = action;
    }

    public ShowErrorEvent(){}
}
