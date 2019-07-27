package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cseandroid.gringott.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationLogActivity extends AppCompatActivity {

    LocationListener locationListener;
    LocationManager locationManager;
    TextView disp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_log);
        disp=findViewById(R.id.textView3);
        Bundle b=getIntent().getExtras();
        String ume=b.getString("uname");
        disp.setText("Welcome " +ume);
        String place[] = {"Chennai", "London", "Paris", "New York","Mexico City", "Mumbai","São Paulo","Banglore","Bangkok"};
        String coordi[] = {"Latitude: 13.0827° N   Longitude: 80.2707° E",
                "Latitude: 51.5074° N   Longitude: 0.1278° W",
                "Latitude: 48.8566° N   Longitude: 2.3522° E",
                "Latitude: 40.7128° N   Longitude: 74.0060° W",
                "Latitude: 19.4326° N   Longitude: 99.1332° W",
                "Latitude: 19.0760° N   Longitude: 72.8777° E",
                "Latitude: 23.5505° S   Longitude: 46.6333° W",
                "Latitude: 12.9716° N   Longitude: 77.5946° E",
                "Latitude: 13.7563° N   Longitude: 100.5018° E"};
        String[] from = {"place", "coordi"};
        int[] tid = {android.R.id.text1, android.R.id.text2};
        ListView lv = findViewById(R.id.listv);
        List<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap;
        for (int i = 0; i < place.length; i++) {
            hashMap = new HashMap<>();

            hashMap.put("place", place[i]);
            hashMap.put("coordi", coordi[i]);
            list.add(hashMap);
        }
        SimpleAdapter adapter =
                new SimpleAdapter
                        (this,
                                list,
                                android.R.layout.simple_list_item_2,
                                from, tid);
        lv.setAdapter(adapter);

    }

}
