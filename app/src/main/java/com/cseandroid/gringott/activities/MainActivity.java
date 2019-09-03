package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cseandroid.gringott.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    int clickCount = 0;
    private Toolbar toolbar_main;
    private FloatingActionButton fab_add;
    ListView password_listview;
    SQLiteDatabase db;
    TextView welcome_textview;
    String userName;
    List<String> entries = new ArrayList<String>();
    List<String> usernames = new ArrayList<String>();

    int globalPosition;

    int logo[] = {R.drawable.padlock, R.drawable.padlock, R.drawable.padlock, R.drawable.padlock, R.drawable.padlock, R.drawable.padlock, R.drawable.padlock};
    String from[] = {"entries", "usernames", "logo"};
    int to[] = {R.id.list_site_name, R.id.list_user_name, R.id.list_image_view};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome_textview = findViewById(R.id.welcome_messgae_textview);
        fab_add = findViewById(R.id.add_entry);
        userName = "TestUser";
        Log.v("lifecycle", "onCreateView");
        welcome_textview.setText("Welcome " + userName);


        password_listview = findViewById(R.id.lv_password_list);

        password_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
                intent.putExtra("type", "view");
                intent.putExtra("entry", entries.get(position));
                startActivity(intent);
            }
        });

        registerForContextMenu(password_listview);

        password_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                globalPosition = position;
                Toast.makeText(getApplicationContext(), globalPosition + "", Toast.LENGTH_SHORT).show();
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

        toolbar_main = findViewById(R.id.toolbar_main);


        toolbar_main.inflateMenu(R.menu.main_menu);
        toolbar_main.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.menu_password_generator) {
                    Intent intent = new Intent(getApplicationContext(), PasswordGeneratorActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.menu_signout) {
                    Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
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
                } else if (item.getItemId() == R.id.menu_share) {
                    Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });


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
            Toast.makeText(getApplicationContext(), globalPosition + " " + "edit" + "gmail", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
            intent.putExtra("type", "edit");
            intent.putExtra("entry", entries.get(globalPosition));
            startActivity(intent);
        } else if (item.getItemId() == R.id.popup_menu_delete) {
            Toast.makeText(getApplicationContext(), globalPosition + " " + "delete", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you really want to delet the given entry");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        db.execSQL("DELETE FROM password where entryname='" + entries.get(globalPosition) + "'");
                        new LoadTask().execute();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Unavailable", Toast.LENGTH_SHORT).show();
                    }
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
    protected void onStart() {
        password_listview = findViewById(R.id.lv_password_list);
        new LoadTask().execute();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        clickCount++;
        if (clickCount % 2 == 0) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Press back again to signout", Toast.LENGTH_SHORT).show();
        }
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db = getApplicationContext().openOrCreateDatabase
                    ("PasswordDB", Context.MODE_PRIVATE, null);
            db.execSQL
                    ("CREATE TABLE IF NOT EXISTS " +
                            "password(entryname VARCHAR PRIMARY KEY," +
                            "websiteurl VARCHAR,username VARCHAR,password VARCHAR,note VARCHAR,lastmodified VARCHAR);");

            entries.removeAll(entries);
            usernames.removeAll(usernames);
            Cursor c = db.rawQuery("SELECT entryname,username FROM password", null);
            while (c.moveToNext()) {
                entries.add(c.getString(0));
                usernames.add(c.getString(1));
            }


            for (int i = 0; i < entries.size(); i++) {
                map = new HashMap<String, String>();
                map.put("entries", entries.get(i));
                map.put("usernames", usernames.get(i));
                map.put("logo", logo[0] + "");
                list.add(map);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.list_item_main, from, to);
            password_listview.setAdapter(adapter);


        }
    }

}
