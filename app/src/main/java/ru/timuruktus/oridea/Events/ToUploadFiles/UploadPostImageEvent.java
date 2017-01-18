package ru.timuruktus.oridea.Events.ToUploadFiles;


import android.graphics.Bitmap;

public class UploadPostImageEvent {

    public Bitmap localImg;


    public UploadPostImageEvent(Bitmap localImg) {
        this.localImg = localImg;
    }
}
