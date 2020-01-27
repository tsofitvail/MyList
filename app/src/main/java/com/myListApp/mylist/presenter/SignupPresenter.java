package com.myListApp.mylist.presenter;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.myListApp.mylist.LogInActivity;
import com.myListApp.mylist.SignUpActivity;

import java.util.concurrent.Executor;

public class SignupPresenter {

    private View view;
    private FirebaseAuth auth;

    public SignupPresenter(View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    public void sendVerifyEmail(String email,String password,String displayName){
       FirebaseUser user=auth.getCurrentUser();
       user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                   view.verificationMailSend(true);
                else  {view.verificationMailSend(false);}
            }
        });
    }
    public void signupUser(String email,String password,String displayName){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName).build();
                    auth.getCurrentUser().updateProfile(profileUpdates);
                    addToSharedPreference(email);
                    sendVerifyEmail(email,password,displayName);
                   //view.signupSucceed();
                }
                else {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    view.signupFailed(errorCode);
                }

            }
        });


    }

    private void addToSharedPreference(String email) {
        SharedPreferences.Editor editor = LogInActivity.sharedpreferences.edit();
        editor.putBoolean(email,true);
        editor.commit();
    }

    public interface View{
        void signupSucceed();
        void signupFailed(String errorCode);
        void verificationMailSend(boolean send);

    }
}
