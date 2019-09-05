package com.cseandroid.gringott.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.cseandroid.gringott.R;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout changePinLayout;
    TextView changePinToggle;
    Button changePinButton;
    EditText newpin, oldpin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        changePinLayout = findViewById(R.id.changePinLayout);
        changePinToggle = findViewById(R.id.change_pin_toggle);
        changePinButton = findViewById(R.id.changePinButton);
        newpin = findViewById(R.id.newpin_Ed);
        oldpin = findViewById(R.id.oldpin_ed);

        changePinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newpin.getText().toString().equals("") || oldpin.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Both Fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
                    String pin = sp.getString("pin", "XXXX");
                    Log.v("PINREL",pin);
                    if(pin.equals(oldpin.getText().toString()))
                    {
                        if(newpin.getText().toString().length()==4){
                            SharedPreferences spedit = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = spedit.edit();
                            edit.putString("pin", newpin.getText().toString());
                            edit.apply();
                            changePinLayout.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"PIN changed",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Enter a 4 digit pin",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Old Pin is invalid",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        changePinToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePinLayout.setVisibility(View.VISIBLE);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }





    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}