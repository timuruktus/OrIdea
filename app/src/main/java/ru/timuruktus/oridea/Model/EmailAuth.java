package ru.timuruktus.oridea.Model;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.timuruktus.oridea.Events.ToAuthPresenter.AuthFailedEvent;
import ru.timuruktus.oridea.Events.ToAuthPresenter.AuthSucceedEvent;
import ru.timuruktus.oridea.Events.ToEmailAuth.StartLogEvent;
import ru.timuruktus.oridea.Events.ToEmailAuth.StartRegEvent;
import ru.timuruktus.oridea.Model.JSONFragments.UserAccount;
import ru.timuruktus.oridea.Presenter.AuthPresenter;

public class EmailAuth {

    private FirebaseAuth mAuth;
    public static final String TAG = "tag";
    private String email,pass;
    private AuthPresenter authPresenter;
    private DatabaseReference mDatabase;

    public EmailAuth(){
        EventBus.getDefault().register(this);
    }

    /**
     * Checks if <field>email</field> and <field>password</field>
     * are correct
     * @return - false, if:
     * 1)password/email is empty
     * 3)email doesn't contain "@" and "."
     * 4)password length < 6 letters
     */
    private boolean validateForm() {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) valid = false;
        if (TextUtils.isEmpty(pass)) valid = false;
        if (!email.contains("@")) valid = false;
        if (!email.contains(".")) valid = false;
        if (pass.length()<6) valid = false;
        return valid;
    }

    @Subscribe
    public void startRegister(StartRegEvent event){
        this.email = event.email;
        this.pass = event.pass;
        mAuth = FirebaseAuth.getInstance();
        createAccount();
    }

    @Subscribe
    public void startAuth(StartLogEvent event){
        this.email = event.email;
        this.pass = event.pass;
        mAuth = FirebaseAuth.getInstance();
        signIn();
    }

    private void createAccount() {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            EventBus.getDefault().post(new AuthFailedEvent());
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.e(TAG,"mAuth.createUserWithEmailAndPassword.!task.isSuccessful()");
                            EventBus.getDefault().post(new AuthFailedEvent());
                            return;
                        }

                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Email sent.");
                                        }
                                    }
                                });
                        writeNewUser(email);
                        EventBus.getDefault().post(new AuthSucceedEvent(false));
                    }
                });
    }


    private void signIn() {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            EventBus.getDefault().post(new AuthFailedEvent());
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            EventBus.getDefault().post(new AuthFailedEvent());
                            return;
                        }
                        EventBus.getDefault().post(new AuthSucceedEvent(true));

                    }
                });
    }

    private void writeNewUser(String email) {
        Log.d(TAG, "Email in writeNewUser(): " + email);
        UserAccount user = new UserAccount();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }
}
