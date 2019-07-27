package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cseandroid.gringott.R;
import com.cseandroid.gringott.fragments.MySettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_settings,new MySettingsFragment()).commit();
    }
}
