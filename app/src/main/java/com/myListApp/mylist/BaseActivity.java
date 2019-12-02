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
        //actionBar.setLogo(R.drawable.ic_grocery_launcher);
       // actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.custum_action_bar);
        View v=actionBar.getCustomView();
        ImageView logOut=v.findViewById(R.id.logout_btn);
        logOut.setOnClickListener(v1 -> {
            AlertDialog dialog=new AlertDialog.Builder(this).create();
            dialog.setMessage("האם אתה בטוח שברצונך לצאת מחשבונך?");
            dialog.setIcon(R.drawable.ic_log_out);
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "כן", (dialog1, which) -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(this,LogInActivity.class);
                startActivity(intent);
            });
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "לא", (dialog12, which) -> dialog12.dismiss());
            dialog.show();
        });


    }

    protected abstract int getLayoutResource();
}
