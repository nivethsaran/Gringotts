package com.cseandroid.gringott.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricFragment;

import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.cseandroid.gringott.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthenticationActivity extends AppCompatActivity{

    Button login_button;
    TextView forgot_textview, signup_textview;
    EditText username, password;
    CheckBox show_password;
   public BiometricPrompt biometricPrompt;
   FingerprintManager fingerprintManager;

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        login_button = findViewById(R.id.login_button);
        signup_textview = findViewById(R.id.signup_textview);
        forgot_textview = findViewById(R.id.forgot_textview);
        username = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        show_password = findViewById(R.id.show_password);

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
                if (username.getText().toString().equals("test") && password.getText().toString().equals("test")) {
                    Intent i = new Intent(AuthenticationActivity.this, MainActivity.class);
                    i.putExtra("uname", username.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credantials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AuthenticationActivity.this, SignUpActivity.class);
//                startActivity(intent);

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

                        }
                    });

                    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Hello")
                            .setNegativeButtonText("Hello")
                            .build();

                    biometricPrompt.authenticate(promptInfo);
                }
                else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                {
                    fingerprintManager=(FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
                        if(fingerprintManager.isHardwareDetected())
                        {
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED)
                            {
                                if(fingerprintManager.hasEnrolledFingerprints())
                                {

                                }
                                else
                                {
                                }
                            }
                            else
                            {
                                requestPermissions(new String[]{Manifest.permission.USE_FINGERPRINT},101);
                                return;
                            }


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();
                        }

                }
                else
                {

                    Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();
                }
            }

//    private boolean canAuthenticateWithBiometrics() {
//        // Check whether the fingerprint can be used for authentication (Android M to P)
//        if (Build.VERSION.SDK_INT < 29) {
//            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);
//            return fingerprintManagerCompat.hasEnrolledFingerprints() && fingerprintManagerCompat.isHardwareDetected();
//        } else {    // Check biometric manager (from Android Q)
//            BiometricManager biometricManager = this.getSystemService(BiometricManager.class);
//            if (biometricManager != null) {
//                return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
//            }
//            return false;
//        }
//    }


        });
    }


}


