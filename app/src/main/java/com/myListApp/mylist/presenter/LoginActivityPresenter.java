package com.myListApp.mylist.presenter;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.myListApp.mylist.Firebase.UserInfo;
import com.myListApp.mylist.LogInActivity;
import com.myListApp.mylist.MyListActivity;

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
        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()) {
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
                    checkIfEmailVerified();
                }
                else
                    view.loginFail(true);
            }
        });

    }

    private void checkIfEmailVerified() {
        if(!auth.getCurrentUser().isEmailVerified()){
           auth.signOut();
           view.loginFail(false);
        }
        else{
            SharedPreferences sharedPref=LogInActivity.sharedpreferences;
            boolean isSignUp=sharedPref.getBoolean(auth.getCurrentUser().getEmail(),false);
            if(isSignUp) {
                userInfo.setIsSignUp(1);
                SharedPreferences.Editor editor = LogInActivity.sharedpreferences.edit();
                editor.putBoolean(auth.getCurrentUser().getEmail(),false);
                editor.commit();
            }
            userInfo.setName(auth.getCurrentUser().getDisplayName());
            userInfo.setEmail(auth.getCurrentUser().getEmail());
            //userInfo.setIsSignUp(0);
            view.loginSuccessful(userInfo);
        }



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
        void loginFail(boolean isverifyEmail);
        void resetSucceed();
        void resetFailed();

    }
}
