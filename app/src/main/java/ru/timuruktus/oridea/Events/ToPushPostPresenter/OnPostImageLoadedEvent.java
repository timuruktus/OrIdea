package ru.timuruktus.oridea.Events.ToPushPostPresenter;


public class OnPostImageLoadedEvent {

    public String imgUrl;

    public OnPostImageLoadedEvent(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
