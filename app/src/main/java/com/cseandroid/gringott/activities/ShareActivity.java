package com.cseandroid.gringott.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cseandroid.gringott.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareActivity extends AppCompatActivity {

List<Boolean> selected=new ArrayList<Boolean>();
Button share_button;
ImageView qr_image;
    boolean select_flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        share_button=findViewById(R.id.share_button);
        qr_image=findViewById(R.id.qr_image);

        qr_image.setVisibility(View.VISIBLE);


        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://drive.google.com/open?id=1dH4yr6raOf1VN_GsQ8WbcBYCvzw7Gb_4");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });




    }

}

