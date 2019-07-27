package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cseandroid.gringott.R;

public class SignUpActivity extends AppCompatActivity {
EditText ed_un,ed_pw;
Button signup;
TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ed_un=findViewById(R.id.username_edt);
        ed_pw=findViewById(R.id.pass_edt);
        signup=findViewById(R.id.signup_button);
        login=findViewById(R.id.login_redirect);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,AuthenticationActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_un.getText().toString().equals("")||ed_pw.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter valid Credentials",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent(SignUpActivity.this,AuthenticationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
