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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.cseandroid.gringott.R;

public class AboutActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    String items[]=
            {"Z.Niveth", "C.Vikram", "R.Rajsekar", "S.Venkat raghavan"
            };
    GridView lv;
    ArrayAdapter<String> ad;
    TextView browseButton;
    TextView callbutton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        browseButton = findViewById(R.id.bb);
        callbutton=findViewById(R.id.cb);
        browseButton.setOnClickListener(this);
        callbutton.setOnClickListener(this);

        lv = findViewById(R.id.lv);

        ad = new ArrayAdapter<String>
                (this,R.layout.list_about,
                        R.id.tv,items);

        lv.setAdapter(ad);
        lv.setOnItemClickListener(this);

    }
    public void onClick(View view) {

        if (view.getId() == browseButton.getId()) {
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "gringot@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK");
            startActivity(intent);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView,
                            View view, int i, long l) {
    }


}
