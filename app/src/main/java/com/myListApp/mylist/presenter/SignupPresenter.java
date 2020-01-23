package com.myListApp.mylist.presenter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupPresenter {

    private View view;
    private FirebaseAuth auth;

    public SignupPresenter(View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    public void signupUser(String email,String password,String displayName){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName).build();
                    auth.getCurrentUser().updateProfile(profileUpdates);
                    view.signupSucceed();
                }
                else {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    view.signupFailed(errorCode);
                }

            }
        });


    }

    public interface View{
        void signupSucceed();
        void signupFailed(String errorCode);

    }
}
