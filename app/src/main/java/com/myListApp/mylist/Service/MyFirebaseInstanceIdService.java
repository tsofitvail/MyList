package com.myListApp.mylist.Service;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.myListApp.mylist.R;

/*
handles the creation, rotation, and updating of registration tokens.
It makes sure that the given message is sent to specific devices/device groups.
 */
public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onNewToken(String s) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MyListActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("MyListActivity", msg);
                     //   Toast.makeText(MyListActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        //super.onNewToken(s);
        //Get hold of the registration token
       // String refreshedToken =FirebaseInstanceId.getInstance().getToken();
        //Log the token
       // Log.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(s);
    }


    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }

}
