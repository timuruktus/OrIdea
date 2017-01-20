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

import ru.timuruktus.oridea.Events.EventCallbacks.ReturnCategoriesEvent;
import ru.timuruktus.oridea.Events.Global.ShowErrorEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.ChangeFragmentEvent;
import ru.timuruktus.oridea.Events.ToPushPostPresenter.OnChooseCategoryEvent;
import ru.timuruktus.oridea.Events.EventCallbacks.OnPostImageLoadedCallback;
import ru.timuruktus.oridea.Events.ToPushPostPresenter.OnPushButtonClickEvent;
import ru.timuruktus.oridea.Events.ToUploadFiles.UploadPostImageEvent;
import ru.timuruktus.oridea.Model.JSONFragments.Post;
import ru.timuruktus.oridea.Model.JSONFragments.UserAccount;
import ru.timuruktus.oridea.View.Fragments.WelcomeFragment;

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
                }
                EventBus.getDefault().post(new ReturnCategoriesEvent(categories));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                return;
            }

        });

    }

    @Subscribe
    public void onPushButtonClick(OnPushButtonClickEvent event){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            EventBus.getDefault().post(new ShowErrorEvent());
            return;
        }
        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified() == false){
            EventBus.getDefault().post(new ShowErrorEvent(ShowErrorEvent.Action.DIDNT_VERIFY_EMAIL));
            return;
        }
        this.text = event.editText;
        this.title = event.editTitle;
        this.category = event.category;
        if(event.urlToImage == null){
            EventBus.getDefault().post(new UploadPostImageEvent(event.localImg));
        }else{
            this.urlToImage = event.urlToImage;
            preparePostToDB();
        }
    }

    private void preparePostToDB(){
            getAuthorLogin();
    }

    private void getAuthorLogin() throws NullPointerException{
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                if(userAccount == null) Log.d(TAG, "User account is empty");
                if(userAccount.getUsername() == null){
                    PushPostPresenter.this.username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                }else{
                    PushPostPresenter.this.username = userAccount.getUsername();
                }

                loadPostToDB();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }

    @Subscribe
    public void onPostImageLoaded(OnPostImageLoadedCallback event){
        this.urlToImage = event.imgUrl;
        preparePostToDB();
    }

    private void loadPostToDB(){
        try {
            Post post;
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
                    child("Posts").push().
                    setValue(postID);
            EventBus.getDefault().post(new ChangeFragmentEvent(new WelcomeFragment(), false));
        }catch (NullPointerException ex){
            ex.printStackTrace();
            EventBus.getDefault().post(new ShowErrorEvent());
        }
    }

}
