package ru.timuruktus.oridea.Events.Global;


import java.util.ArrayList;

public class ReturnCategoriesEvent {

    public ArrayList<String> categories;

    public ReturnCategoriesEvent(ArrayList<String> categories){
        this.categories = categories;
    }
}
