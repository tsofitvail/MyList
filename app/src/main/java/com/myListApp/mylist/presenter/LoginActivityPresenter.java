package com.myListApp.mylist.presenter;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.myListApp.mylist.Firebase.UserInfo;

public class LoginActivityPresenter {

    private View view;
    private FirebaseAuth auth;
    private com.myListApp.mylist.Firebase.UserInfo userInfo;

    public LoginActivityPresenter(View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        this.userInfo=new UserInfo();
    }

    /*
      check if the user is already connected
     */
    public void checkUserLoogedIn(){
        if (auth.getCurrentUser() != null) {
            userInfo.setName(auth.getCurrentUser().getDisplayName());
            userInfo.setEmail(auth.getCurrentUser().getEmail());
            view.moveToNextActivity(userInfo);
        }
    }

    /*
    sign in with user email and user password
     */
    public void signInWithEmailAndPassword(String email,String password){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    userInfo.setName(auth.getCurrentUser().getDisplayName());
                    userInfo.setEmail(auth.getCurrentUser().getEmail());
                    userInfo.setIsSignUp(0);
                    view.loginSuccessful(userInfo);
                }
                else
                    view.loginFail();
            }
        });

    }

    public void resetPassword(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    view.resetSucceed();
                else
                    view.resetFailed();
            }
        });
    }



    public interface View{
        void moveToNextActivity(UserInfo userInfo);
        void loginSuccessful(UserInfo userInfo);
        void loginFail();
        void resetSucceed();
        void resetFailed();

    }
}
