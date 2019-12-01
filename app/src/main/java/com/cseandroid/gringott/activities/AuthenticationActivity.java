package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricFragment;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.cseandroid.gringott.R;
import com.cseandroid.gringott.crypto.AES;
import com.cseandroid.gringott.services.TimeoutService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthenticationActivity extends AppCompatActivity {

    Button login_button;
    TextView forgot_textview, signup_textview;
    EditText username, password;
    CheckBox show_password,remember_me;
    public BiometricPrompt biometricPrompt;
    FingerprintManager fingerprintManager;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;



    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
        username.setText(sp.getString("loginuname",""));
        password.setText(sp.getString("loginpassword",""));
        remember_me.setChecked(sp.getBoolean("remembermestatus",false));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        dialog = new ProgressDialog(new ContextThemeWrapper(AuthenticationActivity.this, R.style.MyProgressDialog));
        login_button = findViewById(R.id.login_button);
        signup_textview = findViewById(R.id.signup_textview);
        forgot_textview = findViewById(R.id.forgot_textview);
        username = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        show_password = findViewById(R.id.show_password);
        remember_me=findViewById(R.id.remember_me);




        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(null);
                } else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(remember_me.isChecked())
                {
                SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("loginuname", username.getText().toString());
                edit.putString("loginpassword",password.getText().toString());
                edit.putBoolean("remembermestatus",true);
                edit.apply();
                }
                else
                {
                    SharedPreferences sp = getSharedPreferences("mycredentials", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("loginuname", "");
                    edit.putString("loginpassword","");
                    edit.putBoolean("remembermestatus",false);
                    edit.apply();
                }
                dialog.setMessage("Logging In...");
                dialog.show();
                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        currentUser = mAuth.getCurrentUser();
                                        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    final Intent i = new Intent(AuthenticationActivity.this, MainActivity.class);
                                                    i.putExtra("uname", document.get("fullname").toString());
                                                    if (dialog.isShowing()) {
                                                        dialog.dismiss();
                                                    }
                                                    username.setText("");
                                                    password.setText("");
                                                    getLocationLatLong();



                                                    final SharedPreferences sp_settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                                                    if (sp_settings.getString("fingerprint", "none").equals("dl")) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                                            Executor executor = Executors.newSingleThreadExecutor();
                                                            biometricPrompt = new BiometricPrompt(AuthenticationActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                                                                @Override
                                                                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                                                    super.onAuthenticationError(errorCode, errString);
                                                                }

                                                                @Override
                                                                public void onAuthenticationFailed() {
                                                                    super.onAuthenticationFailed();
                                                                }

                                                                @Override
                                                                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                                                    super.onAuthenticationSucceeded(result);
                                                                    if(sp_settings.getBoolean("timeout",false))
                                                                    {
                                                                        Intent si=new Intent(AuthenticationActivity.this,TimeoutService.class);
                                                                        startService(si);
                                                                    }
                                                                    startActivity(i);


                                                                }
                                                            });

                                                            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                                                    .setTitle("Login Using Fingerprint")
                                                                    .setNegativeButtonText("Dismiss")
                                                                    .build();

                                                            biometricPrompt.authenticate(promptInfo);
                                                        } else {
                                                            if(sp_settings.getBoolean("timeout",false))
                                                            {
                                                                Intent si=new Intent(AuthenticationActivity.this,TimeoutService.class);
                                                                startService(si);
                                                            }
                                                            startActivity(i);
                                                        }
                                                    } else {
                                                        if(sp_settings.getBoolean("timeout",false))
                                                        {
                                                            Intent si=new Intent(AuthenticationActivity.this,TimeoutService.class);
                                                            startService(si);
                                                        }
                                                        startActivity(i);
                                                    }


                                                } else {
                                                    Log.d("FIREBASE", "get failed with ", task.getException());
                                                }
                                            }
                                        });


                                    } else {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Invalid Credantials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        forgot_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(username.getText().toString());
            }


        });

        signup_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthenticationActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getLocationLatLong() {


         final LocationManager locationManager;

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                        Map<String, Object> locationMap = new HashMap<>();
                        locationMap.put("latitude",location.getLatitude());
                        locationMap.put("longitude",location.getLongitude());
        db.collection("users").document(currentUser.getUid()).collection("locations").document(String.valueOf(System.currentTimeMillis()))
                .set(locationMap)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("FIREBASE", "FAILED");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("FIREBASE", "CREATED");

                    }

                });
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }









}




