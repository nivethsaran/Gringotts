package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cseandroid.gringott.R;

import java.util.Random;

public class PasswordGeneratorActivity extends AppCompatActivity {
    Button b1;
    SeekBar sb;
    CheckBox c1,c2,c3,c4,c5;
    EditText e1;
    TextView t1;
    int length;
    String symbols;
    String pattern="-";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);

        Toolbar toolbar=findViewById(R.id.password_generator_toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        b1 = findViewById(R.id.button);
//        sb = findViewById(R.id.seekBar);
//        c1 = findViewById(R.id.checkBox);
//        c2 = findViewById(R.id.checkBox2);
//        c3 = findViewById(R.id.checkBox3);
//
//        c4 = findViewById(R.id.checkBox4);
//        e1 = findViewById(R.id.editText);
//        t1 = findViewById(R.id.textView);
//
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(c1.isChecked())
//                {
//                    pattern.concat("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
//                }
//                if(c2.isChecked())
//                {
//                    pattern.concat("abcdefghijklmnopqrstuvwxyz");
//
//                }
//                if(c3.isChecked())
//                {
//                    pattern.concat("0123456789");
//
//                }
//                if(c4.isChecked())
//                {
//                    pattern.concat("!@#$%^&*_=+-/.?<>)");
//
//                }
//                if(e1.getText().toString().equals("")) {
//
//                    Toast.makeText(getApplicationContext(), "Enter Length", Toast.LENGTH_SHORT).show();
//
//                }
//                else if(!c1.isChecked()&&!c2.isChecked()&&!c3.isChecked()&&!c4.isChecked())
//                {
//                    Toast.makeText(getApplicationContext(), "Select atleast one of the options", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    length=Integer.parseInt(e1.getText().toString());
//                    char[] password =generate_Password(length,pattern);
//                    Log.d("TAG",length+"");
//                    t1.setText(password.toString());
//                    t1.setTextSize(25);
//                }
//
//            }
//        });
//
//
//    }
//    public char[] generate_Password(int len,String parameter)
//    {
//
//
//        // Using random method
//
//        char[] password = new char[len];
//
//        for (int i = 0; i < len; i++)
//        {
//            // Use of charAt() method : to get character value
//            // Use of nextInt() as it is scanning the value as int
//            password[i] =
//                    parameter.charAt((int)(Math.random()*parameter.length()));
//
//        }
//        return password;
    }
}
