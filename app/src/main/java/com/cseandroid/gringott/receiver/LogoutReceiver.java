package com.cseandroid.gringott.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cseandroid.gringott.activities.AuthenticationActivity;
import com.cseandroid.gringott.activities.TimeoutActivity;
import com.cseandroid.gringott.services.TimeoutService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogoutReceiver extends BroadcastReceiver {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(intent.getAction().equals("TIMEOUT"))
        {
            Log.v("SERVICE","Logout Called Successfully");
            Toast.makeText(context,"Logout Called",Toast.LENGTH_SHORT).show();
            if(currentUser!=null)
            {
                mAuth.signOut();
                Intent i = new Intent(context, TimeoutActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addCategory(Intent.CATEGORY_HOME);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
