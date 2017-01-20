package ru.timuruktus.oridea.Events.EventCallbacks;


import java.util.ArrayList;

public class ReturnCategoriesEvent {

    public ArrayList<String> categories;

    public ReturnCategoriesEvent(ArrayList<String> categories){
        this.categories = categories;
    }
}
