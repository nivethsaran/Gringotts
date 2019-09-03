package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cseandroid.gringott.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
EditText fullname_ed,email_ed,password_ed,confirmpassword_ed,phone_ed;
Button signup_btn;
TextView loginredirect;
private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    private ProgressDialog dialog;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
         currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dialog = new ProgressDialog(SignUpActivity.this);
        fullname_ed=findViewById(R.id.fullname_edittext);
        email_ed=findViewById(R.id.email_edittext);
        password_ed=findViewById(R.id.password_edittext);
        confirmpassword_ed=findViewById(R.id.password_confirm_edittext);
        phone_ed=findViewById(R.id.phoneedittext);
        loginredirect=findViewById(R.id.login_redirect);
        signup_btn=findViewById(R.id.signup_button);


        loginredirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,AuthenticationActivity.class);
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Doing something, please wait.");
                dialog.show();
                if(fullname_ed.getText().toString()==""||phone_ed.getText().toString()==""||email_ed.getText().toString()==""||confirmpassword_ed.getText().toString()==""||password_ed.getText().toString()=="")
                {
                    Toast.makeText(getApplicationContext(),"Enter data in all fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final String email=email_ed.getText().toString();
                    final String password=password_ed.getText().toString();
                    if(password_ed.getText().toString().equals(confirmpassword_ed.getText().toString()))
                    {
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {currentUser=mAuth.getCurrentUser();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("fullname", fullname_ed.getText().toString());
                                    user.put("email",email_ed.getText().toString());
                                    user.put("phone",phone_ed.getText().toString());

                                    Log.v("FIREBASE","Created");
                                    db.collection("users").document(currentUser.getUid())
                                            .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.v("FIREBASE","CREATED");
                                            fullname_ed.setText("");
                                            email_ed.setText("");
                                            phone_ed.setText("");
                                            password_ed.setText("");
                                            confirmpassword_ed.setText("");
                                            if (dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                            onBackPressed();
                                        }
                                    });

                                }
                                else
                                {

                                }
                            }
                        });
                    }

                }
            }
        });
    }
}
