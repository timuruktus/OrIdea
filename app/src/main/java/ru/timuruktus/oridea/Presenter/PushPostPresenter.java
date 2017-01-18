package ru.timuruktus.oridea.Presenter;


import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import ru.timuruktus.oridea.Events.Global.ReturnCategoriesEvent;
import ru.timuruktus.oridea.Events.Global.ShowErrorEvent;
import ru.timuruktus.oridea.Events.ToPushPostPresenter.OnChooseCategoryEvent;
import ru.timuruktus.oridea.Events.ToPushPostPresenter.OnPostImageLoadedEvent;
import ru.timuruktus.oridea.Events.ToPushPostPresenter.OnPushButtonClickEvent;
import ru.timuruktus.oridea.Events.ToUploadFiles.UploadPostImageEvent;
import ru.timuruktus.oridea.Model.JSONFragments.Post;
import ru.timuruktus.oridea.Model.JSONFragments.UserAccount;

public class PushPostPresenter {

    private DatabaseReference mDatabase;
    public static final String TAG = "tag";
    private String username,text,title,category;
    Bitmap localImg;
    private ArrayList<String> categories;
    private static String urlToImage;


    public PushPostPresenter(){
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onChooseCategory(OnChooseCategoryEvent event){
        categories = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    categories.add(postSnapshot.getValue().toString());
                    System.out.println(postSnapshot.getValue().toString());
                }
                EventBus.getDefault().post(new ReturnCategoriesEvent(categories));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Subscribe
    public void onPushButtonClick(OnPushButtonClickEvent event){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            EventBus.getDefault().post(new ShowErrorEvent());
            return;
        }
        if(event.urlToImage == null){
            EventBus.getDefault().post(new UploadPostImageEvent(event.localImg));
        }else{
            this.urlToImage = event.urlToImage;
            this.text = event.editText;
            this.title = event.editTitle;
            this.category = event.category;
            loadPostToDB();
        }
    }

    private void loadPostToDB(){
        try {
            Post post;
            getAuthorLogin();
            if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() == null){
                // TODO: THIS IS PLACEHOLDER. NEEDED TO BE CHANGED
                String defaultIMGUrl = "http://img.freeflagicons.com/thumb/square_icon/russia/russia_640.png";
                post = new Post(text, urlToImage, defaultIMGUrl, category, title, username);
            }else {
                post = new Post(text, urlToImage,
                        FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(), category, title,
                        username);
            }
            /**
             *      -UserPosts
             *          -postId
             *              -post
             *      -Users
             *          -currentUserEmail
             *              -Posts
             *                  -currentPost
             */
            String postID = mDatabase.child("UsersPosts").push().getKey();
            mDatabase.child("UsersPosts").child(postID).setValue(post);
            mDatabase.child("Users").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                    child("Posts").
                    setValue(post);
            EventBus.getDefault().unregister(this);
        }catch (NullPointerException ex){
            ex.printStackTrace();
            EventBus.getDefault().post(new ShowErrorEvent());
        }

    }

    private void getAuthorLogin() throws NullPointerException{
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                if(userAccount.getUsername() == null){
                    PushPostPresenter.this.username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                }else{
                    PushPostPresenter.this.username = userAccount.getUsername();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(postListener);
    }

    @Subscribe
    public void onPostImageLoaded(OnPostImageLoadedEvent event){
        this.urlToImage = event.imgUrl;
        loadPostToDB();
    }
}
