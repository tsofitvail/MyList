package com.myListApp.mylist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        createCustomActionBar();

    }

    private void createCustomActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.custum_action_bar);
        View v=actionBar.getCustomView();
        ImageView logOut=v.findViewById(R.id.logout_btn);
        logOut.setOnClickListener(v1 -> {
            AlertDialog dialog=new AlertDialog.Builder(this).create();
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.message_question_logout));
            dialog.setIcon(R.drawable.ic_log_out);
            String positiveAnswer=getApplicationContext().getResources().getString(R.string.button_positive);
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveAnswer, (dialog1, which) -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(this,LogInActivity.class);
                startActivity(intent);
            });
            String negativeAnswer=getApplicationContext().getResources().getString(R.string.button_negative);
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeAnswer, (dialog12, which) -> dialog12.dismiss());
            dialog.show();
        });


    }

    protected abstract int getLayoutResource();
}
