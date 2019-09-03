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
ListView lv_share;
List<Boolean> selected=new ArrayList<Boolean>();
Button switch_button;
ImageView qr_image;
    boolean select_flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        lv_share = findViewById(R.id.lv_share);
        switch_button=findViewById(R.id.switch_button);
        qr_image=findViewById(R.id.qr_image);

        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String,String> map ;

        List<String> numbers=new ArrayList<String>();
        if (c.moveToFirst()) {
            do {
//                numbers.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                contacts.add(c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
                map = new HashMap<String, String>();
                if(!numbers.contains( c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))) {
                    map.put("numbers", c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    map.put("contacts", c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
                    numbers.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    list.add(map);
                }
            } while (c.moveToNext());
        }
        String [] from = {"contacts", "numbers"};
        int [] to = {R.id.name_share,
                R.id.number_share};

        for(int i=0;i<numbers.size();i++)
        {
            selected.add(false);
        }

        SimpleAdapter ad = new SimpleAdapter(this, list,R.layout.list_item_contacts, from,to);
        lv_share.setAdapter(ad);
        lv_share.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(select_flag==true)
                {
                    if(selected.get(i)==false)
                    {
                    LinearLayout ll=(LinearLayout)view;
                    ll.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorText));
                    selected.set(i,true);
                    }
                    else
                    {
                        LinearLayout ll=(LinearLayout)view;
                        ll.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bgColorMain));
                    }
                }
                else
                {

                    LinearLayout ll=(LinearLayout)view;
                    ll.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bgColorMain));
                }

            }
        });
        lv_share.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout ll=(LinearLayout)view;
                ll.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorText));
                select_flag=true;
                return true;
            }
        });

        switch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch_button.getText().equals("SHOW CONTACTS LIST"))
                {
                    switch_button.setText("SHOW QR CODE");
                    lv_share.setVisibility(View.VISIBLE);
                    qr_image.setVisibility(View.GONE);


                }
                else if(switch_button.getText().equals("SHOW QR CODE"))
                {
                    switch_button.setText("SHOW CONTACTS LIST");
                    lv_share.setVisibility(View.GONE);
                    qr_image.setVisibility(View.VISIBLE);

                }
            }
        });

    }

}

