package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cseandroid.gringott.R;

public class AboutActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        listView=findViewById(R.id.lv);
        String[] arr={"FAQ","Developers","Terms of Use","Privacy Policy","Open Source Licences","Tell Your Friends"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(AboutActivity.this,R.layout.support_simple_spinner_dropdown_item,arr){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view= super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView=(TextView)view;
                if(textView.getText().toString().equals("Privacy Policy"))
                {
                    String url="https://www.privacypolicygenerator.info/live.php?token=XbWm9LsHctq4WJGzajKjHVJatlTE5UsZ";
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(intent);
                }
                else if(textView.getText().toString().equals("Terms of Use"))
                {
                    String url="https://www.termsandconditionsgenerator.com/live.php?token=Y8ltBKFWicWRtwfOi5EOCDbaQcEfr6YU";
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(intent);
                }
                else if(textView.getText().toString().equals("FAQ"))
                {
                    Intent intent=new Intent(AboutActivity.this,HelpActivity.class);
                    startActivity(intent);
                }
                else if(textView.getText().toString().equals("Open Source Licences"))
                {
                    String url="https://github.com/nivethsaran/PasswordManager/blob/master/LICENSE.txt";
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(intent);
                }
                else if(textView.getText().toString().equals("Tell Your Friends"))
                {
                    Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                    startActivity(intent);
                }
                else if(textView.getText().toString().equals("Developers"))
                {
                    Intent intent = new Intent(getApplicationContext(), DevelopersActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

}
