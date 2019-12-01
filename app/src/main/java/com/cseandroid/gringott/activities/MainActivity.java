package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cseandroid.gringott.R;
import com.cseandroid.gringott.crypto.AES;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    int clickCount = 0;
    ProgressBar progressBarMain;
    private FirebaseAuth mAuth;
    FirebaseFirestore db_online;
    FirebaseUser currentUser;

    private Toolbar toolbar_main;
    private FloatingActionButton fab_add;
    ListView password_listview;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView welcome_textview;
    String userName;

    int globalPosition;
    String globalEntryName;


    int logo[] = {R.drawable.fingerprint,};
    String from[] = {"entries", "usernames", "logo"};
    int to[] = {R.id.list_site_name, R.id.list_user_name, R.id.list_image_view};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    @Override
    protected void onStart() {

        mAuth = FirebaseAuth.getInstance();
        db_online = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent intent=new Intent(MainActivity.this,AuthenticationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            password_listview = findViewById(R.id.lv_password_list);
            progressBarMain = findViewById(R.id.progressBarMain);
            new LoadTask().execute();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db_online = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        password_listview = findViewById(R.id.lv_password_list);
        progressBarMain = findViewById(R.id.progressBarMain);
        swipeRefreshLayout = findViewById(R.id.swipeRefesh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadTask().execute();
            }
        });


        fab_add = findViewById(R.id.add_entry);
        userName = getIntent().getStringExtra("uname");
        Log.v("lifecycle", "onCreateView");

        welcome_textview = findViewById(R.id.welcome_messgae_textview);
        if (userName != null) {
            welcome_textview.setText("Welcome " + userName);
        } else {
            db_online.collection("users").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            welcome_textview.setText("Welcome " + task.getResult().getData().get("fullname").toString());
                        }
                    });
        }


        password_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView entrynametv = view.findViewById(R.id.list_site_name);
                final Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
                db_online.collection("users").document(currentUser.getUid()).collection("passwords").document(entrynametv.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            String entryname = entrynametv.getText().toString();
                            String websiteurl = task.getResult().getData().get("websiteurl").toString();
                            String username = task.getResult().getData().get("username").toString();
                            String password = task.getResult().getData().get("password").toString();
                            String notes = task.getResult().getData().get("note").toString();
                            String timemodified = task.getResult().getData().get("timemodified").toString();
                            intent.putExtra("type", "view");
                            intent.putExtra("entry", entryname);
                            intent.putExtra("websiteurl", websiteurl);
                            intent.putExtra("username", username);
                            intent.putExtra("password", new AES().decrypt(password));
                            intent.putExtra("notes", notes);
                            intent.putExtra("timemodified", timemodified);
                            startActivity(intent);
                        }

                    }
                });

            }
        });

        registerForContextMenu(password_listview);

        password_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView entrynametv = view.findViewById(R.id.list_site_name);
                globalPosition = position;
                globalEntryName = entrynametv.getText().toString();

                return false;
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
                intent.putExtra("type", "add");
                startActivity(intent);
            }
        });


        //TOOLBAR CODES
        toolbar_main = findViewById(R.id.toolbar_main);
        toolbar_main.inflateMenu(R.menu.main_menu);
        toolbar_main.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_password_generator) {
                    Intent intent = new Intent(getApplicationContext(), PasswordGeneratorActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_signout) {
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), PinActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_log) {
                    Intent intent = new Intent(getApplicationContext(), LocationLogActivity.class);
                    intent.putExtra("uname", userName);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_settings) {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_about) {
                    Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        //END TOOLBARCODES

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basic_popup, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.popup_menu_edit) {
            final Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
            db_online.collection("users").document(currentUser.getUid()).collection("passwords").document(globalEntryName)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        String entryname = globalEntryName;
                        String websiteurl = task.getResult().getData().get("websiteurl").toString();
                        String username = task.getResult().getData().get("username").toString();
                        String password = task.getResult().getData().get("password").toString();
                        String notes = task.getResult().getData().get("note").toString();
                        String timemodified = task.getResult().getData().get("timemodified").toString();
                        intent.putExtra("type", "edit");
                        intent.putExtra("entry", entryname);
                        intent.putExtra("websiteurl", websiteurl);
                        intent.putExtra("username", username);
                        intent.putExtra("password", new AES().decrypt(password));
                        intent.putExtra("notes", notes);
                        intent.putExtra("timemodified", timemodified);

                        startActivity(intent);
                    }

                }
            });
        } else if (item.getItemId() == R.id.popup_menu_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
            builder.setTitle("Confirmation");
            builder.setMessage("Do you really want to delete the given entry");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DocumentReference docRef = db_online.collection("users").document(currentUser.getUid()).collection("passwords")
                            .document(globalEntryName);
                    String temp_del_id = globalEntryName;
// Remove the 'capital' field from the document
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("capital", FieldValue.delete());
                    updates.put("websiteurl", FieldValue.delete());
                    updates.put("username", FieldValue.delete());
                    updates.put("password", FieldValue.delete());
                    updates.put("note", FieldValue.delete());
                    docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                    db_online.collection("users").document(currentUser.getUid()).collection("passwords")
                            .document(temp_del_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            new LoadTask().execute();
                        }
                    });

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
        return super.onContextItemSelected(item);

    }


    @Override
    public void onBackPressed() {
        clickCount++;
        if (clickCount % 2 == 0) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Press back again to signout", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }


    class LoadTask extends AsyncTask<Void, Void, List<Map<String, String>>> {
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map;
        List<String> docsNameList = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarMain.setVisibility(View.VISIBLE);
            password_listview.setVisibility(View.VISIBLE);
            db_online.collection("users").document(currentUser.getUid()).collection("passwords")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (final QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("FIREBASE1", document.getId());
                            docsNameList.add(document.getId());

                        }
//                        Map<String, Object> result_map = documentSnapshot.getData();
                        if(docsNameList.size()==0)
                        {
                            progressBarMain.setVisibility(View.INVISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                            ImageView noresultimv=findViewById(R.id.noresult_imv);
                            noresultimv.setVisibility(View.VISIBLE);
                            password_listview.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            ImageView noresultimv=findViewById(R.id.noresult_imv);
                            noresultimv.setVisibility(View.INVISIBLE);
                        }

                        for (final String docName : docsNameList) {
                            db_online.collection("users").document(currentUser.getUid()).collection("passwords").document(docName)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                map = new HashMap<String, String>();
                                                map.put("entries", docName);
                                                map.put("usernames", task.getResult().getData().get("username").toString());
                                                map.put("logo", logo[0] + "");
//                                                Log.v("FIREBASE1", result_map.get("username").toString());
                                                list.add(map);

                                                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.list_item_main, from, to);
                                                password_listview.setAdapter(adapter);
                                                progressBarMain.setVisibility(View.INVISIBLE);
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


    }


}
