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

public class AuthenticationActivity extends AppCompatActivity {

    Button login_button;
    TextView forgot_textview,signup_textview;
    EditText username,password;
    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        login_button=findViewById(R.id.login_button);
        signup_textview=findViewById(R.id.signup_textview);
        forgot_textview=findViewById(R.id.forgot_textview);
        username=findViewById(R.id.email_edittext);
        password=findViewById(R.id.password_edittext);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("test")&&password.getText().toString().equals("test")){
                Intent i=new Intent(AuthenticationActivity.this,MainActivity.class);
                i.putExtra("uname",username.getText().toString());
                startActivity(i);}
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Credantials",Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AuthenticationActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}


