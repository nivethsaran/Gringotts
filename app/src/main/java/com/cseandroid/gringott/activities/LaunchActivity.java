package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        try {
            sleep(1000);
            Intent intent=new Intent(LaunchActivity.this,AuthenticationActivity.class);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar=findViewById(R.id.progress_bar_circular);
        progressBar.setVisibility(View.VISIBLE);
        try {
            sleep(1000);
            Intent intent=new Intent(LaunchActivity.this,AuthenticationActivity.class);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
