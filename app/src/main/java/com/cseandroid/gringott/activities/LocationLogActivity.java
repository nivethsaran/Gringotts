package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cseandroid.gringott.R;
import com.cseandroid.gringott.crypto.AES;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationLogActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db_online;
    FirebaseUser currentUser;
    LocationListener locationListener;
    LocationManager locationManager;
    TextView disp;
    ProgressBar progressBarMain;
    String from[] = {"datetime","datetimecomp", "latitude", "longitude"};
    int to[] = {R.id.datetimetv,R.id.invisibledatetime, R.id.lattv, R.id.longtv};

SwipeRefreshLayout swipeRefreshLayout;
    ListView locationListView;

    @Override
    protected void onStart() {

        mAuth = FirebaseAuth.getInstance();
        db_online = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        locationListView = findViewById(R.id.location_listView);
        progressBarMain = findViewById(R.id.progressBarMain);
        new LoadTask().execute();

        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_log);

        swipeRefreshLayout=findViewById(R.id.swipeRefesh);
        Toolbar toolbar=findViewById(R.id.settings_toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        disp=findViewById(R.id.textView3);
//        Bundle b=getIntent().getExtras();
//        String uname=b.getString("uname");
//        disp.setText("Welcome " +uname);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadTask().execute();
            }
        });


        locationListView = findViewById(R.id.location_listView);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView datetimetv = view.findViewById(R.id.invisibledatetime);
                Log.v("FIREBASE",datetimetv.getText().toString());
                final Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                db_online.collection("users").document(currentUser.getUid()).collection("locations").document(datetimetv.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            String datetime = datetimetv.getText().toString();
                            String longitude = task.getResult().getData().get("longitude").toString();
                            String latitude = task.getResult().getData().get("latitude").toString();
                            intent.putExtra("datetime",datetime );
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            startActivity(intent);
                        }

                    }
                });

            }
        });




    }
    class LoadTask extends AsyncTask<Void, Void, List<Map<String, String>>> {
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map;
        List<String> docsNameList = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMain.setVisibility(View.VISIBLE);
//            password_listview.setVisibility(View.VISIBLE);
            db_online.collection("users").document(currentUser.getUid()).collection("locations")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (final QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("FIREBASE", document.getId());
                            docsNameList.add(document.getId());

                        }
//                        Map<String, Object> result_map = documentSnapshot.getData();
                        if(docsNameList.size()==0)
                        {
                            progressBarMain.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                            ImageView noresultimv=findViewById(R.id.noresult_imv);
                            noresultimv.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            progressBarMain.setVisibility(View.INVISIBLE);
                            ImageView noresultimv=findViewById(R.id.noresult_imv);
                            noresultimv.setVisibility(View.INVISIBLE);
                        }

                        for (final String docName : docsNameList) {
                            db_online.collection("users").document(currentUser.getUid()).collection("locations").document(docName)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                map = new HashMap<String, String>();
                                                map.put("datetimecomp", docName);
                                                map.put("datetime", dateAsString(docName));
                                                map.put("latitude","Latitude:"+ task.getResult().getData().get("latitude").toString());
                                                map.put("longitude","Longitude:"+ task.getResult().getData().get("longitude").toString());
                                                list.add(map);
                                                Collections.sort(list, new Comparator<Map<String, String>>() {
                                                    @Override
                                                    public int compare(Map<String, String> t1, Map<String, String> t2) {
                                                        if(Long.parseLong(t1.get("datetimecomp"))<=Long.parseLong(t2.get("datetimecomp")))
                                                            return 1;
                                                        else
                                                            return -1;
                                                    }
                                                });
                                                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.listitemlocation, from, to);
                                                locationListView.setAdapter(adapter);
//                                                progressBarMain.setVisibility(View.INVISIBLE);
                                                swipeRefreshLayout.setRefreshing(false);
                                            } else {

                                            }

                                        }
                                    });
                        }


                    } else {
                        Log.d("FIREBASE1", "Error getting documents: ", task.getException());
                    }

                }
            });

        }

        @Override
        protected List<Map<String, String>> doInBackground(Void... voids) {


            return list;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> list_param) {
            super.onPostExecute(list_param);


        }
        private String dateAsString(String dateAsLong)
        {String dateAsWord="";
//            Timestamp ts=new Timestamp(Long.parseLong(dateAsLong));
            Date date=new Date(Long.parseLong(dateAsLong));
            return date.toString();

        }

    }


}
