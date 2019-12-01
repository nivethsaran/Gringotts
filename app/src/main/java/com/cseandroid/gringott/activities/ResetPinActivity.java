package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cseandroid.gringott.R;

public class ResetPinActivity extends AppCompatActivity {
Button go,save,copy;
EditText resetCode;
TextView helptv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);

        copy=findViewById(R.id.copy_button);
        helptv=findViewById(R.id.helptv);
        resetCode=findViewById(R.id.code_ed);
        go=findViewById(R.id.go_button);
        save=findViewById(R.id.save_button);
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");


        SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
        String code = sp.getString("code", "XXXXXXXXXXXX");


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", resetCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Copied to Clipboard",Toast.LENGTH_SHORT).show();
            }
        });
        if(code.equals("XXXXXXXXXXXX"))
        {
            String randomAlpha="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            code="";
            for(int i=0;i<12;i++) {
                code = code + "" + randomAlpha.charAt((int) (Math.random() * 12));
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("code", code);
            edit.apply();
            resetCode.setText(code);
            helptv.setText("Write this code somehwere safe. PIN cannot be recovered otherwise");
            resetCode.setEnabled(false);
        }
        else
        {
            resetCode.setText("");
            resetCode.setEnabled(true);
        }


        if(type.equals("forgotpin"))
        {
            save.setVisibility(View.VISIBLE);
            go.setVisibility(View.INVISIBLE);
            copy.setVisibility(View.INVISIBLE);
        }
        else if(type.equals("showcode"))
        {
            go.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
            copy.setVisibility(View.VISIBLE);
        }

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ResetPinActivity.this,AuthenticationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
                if(sp.getString("code","XXXXXXXXXXXX").equals(resetCode.getText().toString()))
                {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("pin", "XXXX");
                    edit.apply();
                    Intent intent=new Intent(ResetPinActivity.this,PinActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Wrong Code",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ResetPinActivity.this,PinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
