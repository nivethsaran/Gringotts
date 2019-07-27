package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
private Toolbar toolbar_main;
ListView password_listview;
TextView welcome_textview;
String userName;
String sites[]={"Google","Facebook","Instagram","Linkedin","Ola","Uber","Swiggy"};
String usernames[]={"usernamenive","usernamenive","usernamenive","usernamenive","usernamenive","usernamenive","usernamenive"};
int logo[]={R.drawable.padlock,R.drawable.padlock,R.drawable.padlock,R.drawable.padlock,R.drawable.padlock,R.drawable.padlock,R.drawable.padlock};
String from[]={"sites","usernames","logo"};
int to[]={R.id.list_site_name,R.id.list_user_name,R.id.list_image_view};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome_textview=findViewById(R.id.welcome_messgae_textview);
        userName="TestUser";
        welcome_textview.setText("Welcome "+userName);

        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String,String> map ;
        for(int i = 0 ; i < sites.length; i++) {
            map = new HashMap<String, String>();
            map.put("sites", sites[i]);
            map.put("usernames", usernames[i]);
            map.put("logo",logo[i]+"");
            list.add(map);
        }

        toolbar_main=findViewById(R.id.toolbar_main);
        password_listview=findViewById(R.id.lv_password_list);
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_item_main, from, to);
        password_listview.setAdapter(adapter);

        password_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(MainActivity.this,NewEntryActivity.class);
                intent.putExtra("type","view");
                intent.putExtra("username",usernames[position]);
                intent.putExtra("site",sites[position]);
                startActivity(intent);
            }
        });
        password_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final TextView tv1=view.findViewById(R.id.list_site_name);
                TextView tv2=view.findViewById(R.id.list_user_name);
                PopupMenu popup = new PopupMenu(MainActivity.this, view, Gravity.RIGHT);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.basic_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.popup_menu_edit)
                        {
                            Toast.makeText(getApplicationContext(),position+" "+"edit"+tv1.getText(),Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,NewEntryActivity.class);
                            intent.putExtra("type","edit");
                            intent.putExtra("username",usernames[position]);
                            intent.putExtra("site",sites[position]);
                            startActivity(intent);
                        }
                        else if(item.getItemId()==R.id.popup_menu_delete)
                        {
                            Toast.makeText(getApplicationContext(),position+" "+"delete",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Confirmation");
                            builder.setMessage("Do you really want to delet the given entry");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),position+" "+"delete",Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                        return true;

                    }
                });
                popup.show();
                return true;
            }
        });

        toolbar_main.inflateMenu(R.menu.main_menu);
        toolbar_main.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.menu_password_generator)
                {
                    Intent intent=new Intent(MainActivity.this,PasswordGeneratorActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.menu_signout)
                {
                    Intent intent=new Intent(MainActivity.this,AuthenticationActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.menu_add)
                {
                    Intent intent=new Intent(MainActivity.this,NewEntryActivity.class);
                    intent.putExtra("type","add");
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.menu_log)
                {
                    Intent intent=new Intent(MainActivity.this,LocationLogActivity.class);
                    intent.putExtra("uname",userName);
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.menu_settings)
                {
                    Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.menu_about)
                {
                    Intent intent=new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });


    }



}
