package ru.timuruktus.oridea.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;

import ru.timuruktus.oridea.Events.Global.ShowLoadingBarEvent;
import ru.timuruktus.oridea.Events.Global.ShowErrorEvent;
import ru.timuruktus.oridea.Events.ToPushPostPresenter.OnPostImageLoadedEvent;
import ru.timuruktus.oridea.Events.ToUploadFiles.UploadPostImageEvent;


public class UploadFiles {

    static Uri downloadUrl;

    @Subscribe
    public static void uploadPostImage(UploadPostImageEvent event){
        EventBus.getDefault().post(new ShowLoadingBarEvent(true));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        event.localImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataBAOS = baos.toByteArray();

        /***************** UPLOADS THE PIC TO FIREBASE*****************/
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://oridea-60e65.appspot.com");
        StorageReference imagesRef = storageRef.child("postImages");

        UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                EventBus.getDefault().post(new ShowErrorEvent());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                EventBus.getDefault().post(new OnPostImageLoadedEvent(downloadUrl.toString()));
            }
        });

    }
}
