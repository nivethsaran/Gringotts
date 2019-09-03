package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cseandroid.gringott.R;

public class PinActivity extends AppCompatActivity {
    EditText edp1, edp2, edp3, edp4;
    TextView tv_desc, clear_tv, error_tv;
    Button save, go;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        edp1 = findViewById(R.id.edp1);
        edp2 = findViewById(R.id.edp2);
        edp3 = findViewById(R.id.edp3);
        edp4 = findViewById(R.id.edp4);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        tv_desc = findViewById(R.id.tv_desc);
//        clear_tv=findViewById(R.id.clear_tv);
        error_tv = findViewById(R.id.error_tv);

        save = findViewById(R.id.save_button);
        go = findViewById(R.id.go_button);

        SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
        String name = sp.getString("pin", "XXXX");

        if (name.equals("XXXX")) {
            tv_desc.setText("Enter New Pin" + name);
            save.setVisibility(View.VISIBLE);
            go.setVisibility(View.INVISIBLE);
        } else {
            tv_desc.setText("Enter Your Pin" + name);
            go.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
        }


        edp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edp1.getText().toString().equals(""))
                    edp2.requestFocus();

                error_tv.setVisibility(View.INVISIBLE);

            }
        });
        edp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edp2.getText().toString().equals(""))
                    edp3.requestFocus();
            }
        });

        edp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edp3.getText().toString().equals(""))
                    edp4.requestFocus();

            }
        });
        edp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edp4.getText().toString().equals("")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edp4.getWindowToken(), 0);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = edp1.getText().toString() + edp2.getText().toString() + edp3.getText().toString() + edp4.getText().toString();
                if (pin.length() != 4) {

                } else {
                    SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("pin", pin);
                    edit.apply();
                    finish();
                    startActivity(getIntent());
                }


            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                String pin = edp1.getText().toString() + edp2.getText().toString() + edp3.getText().toString() + edp4.getText().toString();
                SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
                String name = sp.getString("pin", "XXXX");
                if (pin.equals(name)) {
                    Toast.makeText(getApplicationContext(), "YESSS", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PinActivity.this, AuthenticationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "NOOOO", Toast.LENGTH_SHORT).show();
                    edp1.setText("");
                    edp2.setText("");
                    edp3.setText("");
                    edp4.setText("");
                    edp1.requestFocus();
                    vibrator.vibrate(400);
                    edp1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
                    edp2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
                    edp3.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
                    edp4.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
                    error_tv.setVisibility(View.VISIBLE);
                }
            }
        });

//        clear_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean retvalue = super.onKeyDown(keyCode, event);

        if (keyCode == KeyEvent.KEYCODE_DEL && event.getRepeatCount() == 0) {
            // do something on back.
            if (getCurrentFocus() == edp1) {
                edp1.setText("");
            } else if (getCurrentFocus() == edp2) {
                edp1.setText("");
                edp1.requestFocus();
            } else if (getCurrentFocus() == edp3) {
                edp2.setText("");
                edp2.requestFocus();
            } else if (getCurrentFocus() == edp4) {
                edp3.setText("");
                edp3.requestFocus();
            }
            return false;
        }
        return retvalue;
    }

    @Override
    protected void onStart() {
        super.onStart();
        edp1.setText("");
        edp2.setText("");
        edp3.setText("");
        edp4.setText("");
        edp1.requestFocus();
    }
}
