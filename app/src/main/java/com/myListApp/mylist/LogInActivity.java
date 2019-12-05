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

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText passwordEditText,emailEditText;
    private Button signupButton,loginButton;
    private TextView resetPassword;
    private String email,password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        signupButton = findViewById(R.id.signup_btn);
        loginButton = findViewById(R.id.login_btn);
        resetPassword = findViewById(R.id.resetPassword);
        auth = FirebaseAuth.getInstance();
        //check if the user is already connected
        if (auth.getCurrentUser() != null) {
            //Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, BoardingActivity.class);
            intent.putExtra("DISPLAY_NAME",auth.getCurrentUser().getDisplayName());
            intent.putExtra("IS_SIGN_UP",0);
            intent.putExtra("EMAIL",auth.getCurrentUser().getEmail());
            startActivity(intent);
            finish();
        } else {
           // Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show();
        }


      signupButton.setOnClickListener(v -> {
        Intent i=new Intent(this,SignUpActivity.class);
        startActivity(i);
        finish();
        });


        loginButton.setOnClickListener(v -> {
            email=emailEditText.getText().toString();
            password=passwordEditText.getText().toString();
          auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
              if(task.isSuccessful()){
                  Toast.makeText(this, "Successfully Loged In", Toast.LENGTH_LONG).show();
                  Intent intent=new Intent(this, BoardingActivity.class);
                  intent.putExtra("DISPLAY_NAME",auth.getCurrentUser().getDisplayName());
                  intent.putExtra("IS_SIGN_UP",0);
                  intent.putExtra("EMAIL",auth.getCurrentUser().getEmail());
                  startActivity(intent);
                  finish();
              }
              else{
                  Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
              }
          });
        });


        resetPassword.setOnClickListener(v -> {
            View viewDialog=getLayoutInflater().inflate(R.layout.dialog_insert_email,null);
            EditText emailEditText=viewDialog.findViewById(R.id.emailToReset);
            Button sendBtn=viewDialog.findViewById(R.id.resetButton);
            AlertDialog insertEmailDialog = new AlertDialog.Builder(this)
                    .setView(viewDialog)
                    .create();
            sendBtn.setOnClickListener(v1 -> {
                String email=emailEditText.getText().toString();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(this, task ->{
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                        insertEmailDialog.dismiss();

                    } else {
                        Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG).show();
                    }
                });

            });
            insertEmailDialog.show();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        System.exit(0);
    }
}


