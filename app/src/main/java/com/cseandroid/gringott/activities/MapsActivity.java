package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.cseandroid.gringott.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latitude,longitude;
    TextView countrytv,statetv,citytv;
    Button helpline;
    long datetime;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Toolbar toolbar=findViewById(R.id.maps_toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        helpline=findViewById(R.id.helpline);
        countrytv=findViewById(R.id.country_tv);
        statetv=findViewById(R.id.state_tv);
        citytv=findViewById(R.id.city_tv);
        Intent intent=getIntent();
        latitude=Double.parseDouble(intent.getStringExtra("latitude"));
        longitude=Double.parseDouble(intent.getStringExtra("longitude"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new LocationTask().execute(String.valueOf(latitude),String.valueOf(longitude));

        helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.sendPasswordResetEmail(currentUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:abc@xyz.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PROBLEM CODE:YOUKNOWWHO");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"My account details seem to be compromised. I would like to reest my account");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng locationOfLogin = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(locationOfLogin).title("Approximate Login Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationOfLogin));
        mMap.getMaxZoomLevel();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
    }


    class LocationTask extends AsyncTask<String, Void, String[]>
    {
        @Override
        protected String[] doInBackground(String... strings) {

            String la=strings[0];
            String lo=strings[1];
            String sURL = "https://us1.locationiq.com/v1/reverse.php?key=ebda90cb88832d&+lat="+la+"&lon="+lo+"&format=json"; //just a string

            // Connect to the URL using java's native library
            URL url = null;
            URLConnection request=null;
            try {
                url = new URL(sURL);
                 request = url.openConnection();
                request.connect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = null; //Convert the input stream to a json element
            try {
                root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String city="";
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
            try {
                city = rootobj.getAsJsonObject("address").get("city").getAsString();
            }
            catch (Exception e)
            {
                city =rootobj.getAsJsonObject("address").get("village").getAsString();
            }

            String state = rootobj.getAsJsonObject("address").get("state").getAsString();
            String country = rootobj.getAsJsonObject("address").get("country").getAsString();

            return new String[]{city, state, country};


        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            citytv.setText("City/Village:"+strings[0]);
            statetv.setText("State:"+strings[1]);
            countrytv.setText("Country:"+strings[2]);
        }
    }
}
