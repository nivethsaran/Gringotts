package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.cseandroid.gringott.R;

import static java.lang.Thread.sleep;

public class LaunchActivity extends AppCompatActivity {
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        progressBar=findViewById(R.id.progress_bar_circular);
        progressBar.setVisibility(View.VISIBLE);
        //new LaunchTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LaunchTask().execute();
    }

    class LaunchTask extends AsyncTask<Void, Void, Void> {

       @Override
       protected Void doInBackground(Void... voids) {
           try {
               sleep(2000);
               Intent intent = new Intent(LaunchActivity.this, PinActivity.class);
               startActivity(intent);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           return null;
       }

   }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
