package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.cseandroid.gringott.R;

public class DevelopersActivity extends AppCompatActivity {

    String items[]=
            {"Niveth Saran", "C.Vikram", "R.Rajsekar", "S.Venkat raghavan"
            };
    GridView lv;
    ArrayAdapter<String> ad;
    TextView browseButton;
    TextView callbutton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        browseButton = findViewById(R.id.bb);
        callbutton=findViewById(R.id.cb);

        lv = findViewById(R.id.lv);

        ad = new ArrayAdapter<String>
                (this,R.layout.list_about,
                        R.id.tv,items);

        lv.setAdapter(ad);


    }
    public void onClick(View view) {

        if (view.getId() == browseButton.getId()) {
            try {
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "gringot@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK");
                startActivity(intent);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"No Email App Found",Toast.LENGTH_SHORT).show();
            }

        }

        if (view.getId() == callbutton.getId()) {
            Intent j = new Intent
                    (Intent.ACTION_CALL);
            j.setData
                    (Uri.parse("tel:" + "7339065577"));

            if (ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        123);
                return;
            }

            startActivity(j);

        }

    }




}
