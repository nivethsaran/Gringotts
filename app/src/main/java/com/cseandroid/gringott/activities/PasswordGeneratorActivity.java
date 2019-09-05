package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cseandroid.gringott.R;

import java.util.Random;

public class PasswordGeneratorActivity extends AppCompatActivity {
    Button generate_btn,copy_btn;
    SeekBar sb;
    ProgressBar progressBar;
    CheckBox c1,c2,c3,c4;
    EditText length_ed;
    EditText t1;
    int length;
    int score;
    String symbols;
    String pattern="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);

        Toolbar toolbar = findViewById(R.id.password_generator_toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        copy_btn=findViewById(R.id.copy_button);
        generate_btn = findViewById(R.id.generateButton);
//        sb = findViewById(R.id.seekBar);
        c1 = findViewById(R.id.checkBox1);
        c2 = findViewById(R.id.checkBox2);
        c3 = findViewById(R.id.checkBox3);
        c4 = findViewById(R.id.checkBox4);
        length_ed = findViewById(R.id.length_ed);
        progressBar=findViewById(R.id.progressBar);
        t1 = findViewById(R.id.display_ed);
//


        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", t1.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Copied to Clipboard",Toast.LENGTH_SHORT).show();

            }
        });
        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pattern="";
                if (c1.isChecked()) {
                    pattern = pattern.concat("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                }
                if (c2.isChecked()) {
                    pattern = pattern.concat("abcdefghijklmnopqrstuvwxyz");

                }
                if (c3.isChecked()) {
                    pattern = pattern.concat("0123456789");

                }
                if (c4.isChecked()) {
                    pattern = pattern.concat("!@#$%^&*_=+-/.?<>)");

                }
                if (length_ed.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Enter Length", Toast.LENGTH_SHORT).show();

                } else if (!c1.isChecked() && !c2.isChecked() && !c3.isChecked() && !c4.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Select atleast one of the options", Toast.LENGTH_SHORT).show();
                } else {
                    length = Integer.parseInt(length_ed.getText().toString());
                    Toast.makeText(getApplicationContext(),pattern,Toast.LENGTH_SHORT).show();
                    String password = generate_Password(length, pattern);
                    Log.d("TAG", length + "");
                    t1.setText(password);
                    score=calculatePasswordStrength(password);
                    final Handler progressBarHandler = new Handler();
                    progressBarHandler .post(new Runnable() {

                        public void run() {
                            progressBar.setProgress(score*10);
                            if(score<5)
                            {
                                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

                            }
                            else if(score<8)
                            {
                                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                            }
                            else
                            {
                                progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                            }
                        }
                    });

                    t1.setTextSize(25);
                }

            }
        });


    }

    private static int calculatePasswordStrength(String password){

        //total score of password
        int iPasswordScore = 0;

        if( password.length() < 8 )
            return 0;
        else if( password.length() >= 10 )
            iPasswordScore += 2;
        else
            iPasswordScore += 1;

        //if it contains one digit, add 2 to total score
        if( password.matches("(?=.*[0-9]).*") )
            iPasswordScore += 2;

        //if it contains one lower case letter, add 2 to total score
        if( password.matches("(?=.*[a-z]).*") )
            iPasswordScore += 2;

        //if it contains one upper case letter, add 2 to total score
        if( password.matches("(?=.*[A-Z]).*") )
            iPasswordScore += 2;

        //if it contains one special character, add 2 to total score
        if( password.matches("(?=.*[~!@#$%^&*()_-]).*") )
            iPasswordScore += 2;
        Log.v("PASSWORD",iPasswordScore+"");
        return iPasswordScore;

    }

    public String generate_Password ( int len, String parameter)
    {


        // Using random method

        String password="";

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            password+= parameter.charAt((int) (Math.random() * parameter.length()));
//            Log.v("PASSWORD",password.toString());

        }
        return password;
    }
}
