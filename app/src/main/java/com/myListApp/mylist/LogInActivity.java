package com.myListApp.mylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.myListApp.mylist.Firebase.UserInfo;
import com.myListApp.mylist.presenter.LoginActivityPresenter;

public class LogInActivity extends AppCompatActivity implements LoginActivityPresenter.View {

    private EditText passwordEditText,emailEditText;
    private Button signupButton,loginButton;
    private TextView resetPassword;
    private String email,password;
    private LoginActivityPresenter loginActivityPresenter;
    private AlertDialog insertEmailDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        signupButton = findViewById(R.id.signup_btn);
        loginButton = findViewById(R.id.login_btn);
        resetPassword = findViewById(R.id.resetPassword);
        loginActivityPresenter=new LoginActivityPresenter( this);
        loginActivityPresenter.checkUserLoogedIn();
        signUpButton();
        logInButton();
        resetButton();
    }

    /*
    set on click listener on reset password button
     */
    private void resetButton(){
        resetPassword.setOnClickListener(v -> {
            View viewDialog=getLayoutInflater().inflate(R.layout.dialog_insert_email,null);
            EditText emailEditText=viewDialog.findViewById(R.id.emailToReset);
            Button sendBtn=viewDialog.findViewById(R.id.resetButton);
            insertEmailDialog = new AlertDialog.Builder(this)
                    .setView(viewDialog)
                    .create();
            sendBtn.setOnClickListener(v1 -> {
                String email=emailEditText.getText().toString();
                loginActivityPresenter.resetPassword(email);
            });
            insertEmailDialog.show();
        });


    }
    /*
    set on click listener on log in button
     */
    private void logInButton() {
        loginButton.setOnClickListener(v -> {
            email=emailEditText.getText().toString();
            password=passwordEditText.getText().toString();
            loginActivityPresenter.signInWithEmailAndPassword(email,password);
        });
    }

    /*
    set on click listener on sign up button
     */
    private void signUpButton(){
        signupButton.setOnClickListener(v -> {
            Intent i=new Intent(this,SignUpActivity.class);
            startActivity(i);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        System.exit(0);
    }

    @Override
    /*
    move straight to boarding activity if the user already logged in
     */
    public void moveToNextActivity(UserInfo user) {
        Intent intent = new Intent(this, BoardingActivity.class);
        intent.putExtra("DISPLAY_NAME",user.getName());
        intent.putExtra("IS_SIGN_UP",user.getIsSignUp());
        intent.putExtra("EMAIL",user.getEmail());
        startActivity(intent);
        finish();
    }

    @Override
    /*
       move straight to boarding activity if login successful
     */
    public void loginSuccessful(UserInfo userInfo) {
        Intent intent=new Intent(this, BoardingActivity.class);
        intent.putExtra("DISPLAY_NAME",userInfo.getName());
        intent.putExtra("IS_SIGN_UP",userInfo.getIsSignUp());
        intent.putExtra("EMAIL",userInfo.getEmail());
        startActivity(intent);
        finish();
    }

    @Override
    /*
    massage if log in failed
     */
    public void loginFail() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void resetSucceed() {
        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
        insertEmailDialog.dismiss();
    }

    @Override
    public void resetFailed() {
        Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG).show();
    }
}


