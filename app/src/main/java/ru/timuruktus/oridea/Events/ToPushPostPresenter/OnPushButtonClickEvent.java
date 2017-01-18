package ru.timuruktus.oridea.Events.ToPushPostPresenter;


import android.graphics.Bitmap;

public class OnPushButtonClickEvent {

    public String editText, editTitle, category, urlToImage;
    public Bitmap localImg;

    public OnPushButtonClickEvent(String urlToImage, String editText, String editTitle, String category) {
        this.urlToImage = urlToImage;
        this.editText = editText;
        this.editTitle = editTitle;
        this.category = category;
    }

    public OnPushButtonClickEvent(Bitmap localImg, String editText, String editTitle, String category) {
        this.localImg = localImg;
        this.editText = editText;
        this.editTitle = editTitle;
        this.category = category;
    }
}
