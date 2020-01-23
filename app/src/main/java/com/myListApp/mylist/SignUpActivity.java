package com.myListApp.mylist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.myListApp.mylist.presenter.SignupPresenter;

public class SignUpActivity extends AppCompatActivity implements SignupPresenter.View {

    private EditText passwordEditText,emailEditText,displayNameEditText;
    private Button signupButton;
    private String email,password,displayName;
    private ProgressBar progressBar;
    private SignupPresenter signupPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        displayNameEditText=findViewById(R.id.displayName);
        signupButton = findViewById(R.id.signup_btn);
        progressBar=findViewById(R.id.progressBar);
        signupPresenter=new SignupPresenter(this);
        signupButton();
    }

    /*
    set on click listener on sign up button
     */
    public void signupButton(){
        signupButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            email = emailEditText.getText().toString();
            password = passwordEditText.getText().toString();
            displayName=displayNameEditText.getText().toString();
            if(email.equals("") |password.equals("") | displayName.equals("")) {
                Toast.makeText(this, "לפחות אחד מהשדות ריקים", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            signupPresenter.signupUser(email,password,displayName);
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,LogInActivity.class));

    }

    @Override
    public void signupSucceed() {
        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, BoardingActivity.class);
        intent.putExtra("DISPLAY_NAME",displayName);
        intent.putExtra("IS_SIGN_UP",1);
        intent.putExtra("EMAIL",email);
        startActivity(intent);
        finish();
    }

    @Override
    public void signupFailed(String errorCode) {
        progressBar.setVisibility(View.INVISIBLE);
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                Toast.makeText(this, "המייל שהכנסת בפורמט לא חוקי", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(this, "האימייל שהכנסת כבר מחובר לחשבון במערכת", Toast.LENGTH_LONG).show();
                break;
            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(this, "הסיסמה שהזנת חלשה מידי", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
