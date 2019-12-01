package com.cseandroid.gringott.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cseandroid.gringott.R;
import com.cseandroid.gringott.activities.AuthenticationActivity;
import com.cseandroid.gringott.activities.MainActivity;
import com.cseandroid.gringott.activities.NewEntryActivity;
import com.cseandroid.gringott.activities.PasswordGeneratorActivity;
import com.cseandroid.gringott.crypto.AES;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewEntryFragment extends Fragment {
    EditText ed1, ed2, ed3, ed4, ed5;
    Button save_changes_button, add_entry_button, edit_entry_button;
    CheckBox b4;
    String activityType;
    SQLiteDatabase db;
    private FirebaseAuth mAuth;
    FirebaseFirestore db_online;
    FirebaseUser currentUser;
    public BiometricPrompt biometricPrompt;
    FingerprintManager fingerprintManager;
    TextView timedatetv,generate;
    ImageView logoImage;
    ProgressBar imageProgress;
    public NewEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        db_online = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_entry, container, false);
        Intent typeIntent = getActivity().getIntent();
        activityType = typeIntent.getStringExtra("type");

        imageProgress=view.findViewById(R.id.imageProgress);
        logoImage=view.findViewById(R.id.imageView10);
        ed1 = view.findViewById(R.id.ed1);
        ed2 = view.findViewById(R.id.ed2);
        ed3 = view.findViewById(R.id.ed3);
        ed4 = view.findViewById(R.id.ed4);
        ed5 = view.findViewById(R.id.ed5);
        timedatetv=view.findViewById(R.id.timedatetv);
        save_changes_button = view.findViewById(R.id.save_changes_button);
        add_entry_button = view.findViewById(R.id.add_entry_button);
        edit_entry_button = view.findViewById(R.id.edit_entry_button);
        b4 = view.findViewById(R.id.button4);
        generate=view.findViewById(R.id.generate);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PasswordGeneratorActivity.class);
                startActivity(intent);
            }
        });

        b4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {


                SharedPreferences sp_settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                if (isChecked) {
                    if (sp_settings.getString("fingerprint", "none").equals("dp")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P&&isSensorAvialable()) {
                            Executor executor = Executors.newSingleThreadExecutor();

                            biometricPrompt = new BiometricPrompt(getActivity(), executor, new BiometricPrompt.AuthenticationCallback() {
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
                                    if (isChecked) {
                                        ed4.setTransformationMethod(null);
                                    } else {
                                        ed4.setTransformationMethod(new PasswordTransformationMethod());
                                    }


                                }
                            });

                            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                    .setTitle("View Password")
                                    .setNegativeButtonText("Dismiss")
                                    .build();

                            biometricPrompt.authenticate(promptInfo);
                        } else {
                            if (isChecked) {
                                ed4.setTransformationMethod(null);
                            } else {
                                ed4.setTransformationMethod(new PasswordTransformationMethod());
                            }
                        }
                    } else {
                        if (isChecked) {
                            ed4.setTransformationMethod(null);
                        } else {
                            ed4.setTransformationMethod(new PasswordTransformationMethod());
                        }
                    }

                } else {
                    ed4.setTransformationMethod(new PasswordTransformationMethod());
                }


            }
        });

        save_changes_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals("") || ed3.getText().toString().equals("") || ed4.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter mandatory fields", Toast.LENGTH_SHORT).show();
                } else {
                    String entrynamet = ed1.getText().toString();
                    String websiteurlt = ed2.getText().toString();
                    String usernamet = ed3.getText().toString();
                    String passwordt = ed4.getText().toString();
                    String notet = ed5.getText().toString();
                    String timet = Long.toString(System.currentTimeMillis());


                    currentUser = mAuth.getCurrentUser();
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("websiteurl", ed2.getText().toString());
                    updates.put("username", ed3.getText().toString());
                    updates.put("password", new AES().encrypt(ed4.getText().toString()));
                    updates.put("note", ed5.getText().toString());
                    updates.put("timemodified", System.currentTimeMillis());
                    Log.v("FIREBASE", "STARTING");

                    db_online.collection("users").document(currentUser.getUid()).collection("passwords").document(ed1.getText().toString())
                            .update(updates)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.v("FIREBASE", "FAILED");
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ed1.setText("");
                                    ed2.setText("");
                                    ed3.setText("");
                                    ed4.setText("");
                                    ed5.setText("");
                                    getActivity().onBackPressed();
                                    Log.v("FIREBASE", "CREATED");
                                }

                            });


                }
            }

        });
        add_entry_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals("") || ed3.getText().toString().equals("") || ed4.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter mandatory fields", Toast.LENGTH_SHORT).show();
                } else {
                    String entrynamet = ed1.getText().toString();
                    String websiteurlt = ed2.getText().toString();
                    String usernamet = ed3.getText().toString();
                    String passwordt = ed4.getText().toString();
                    String notet = ed5.getText().toString();
                    String timet = Long.toString(System.currentTimeMillis());


                    currentUser = mAuth.getCurrentUser();
                    Map<String, Object> password = new HashMap<>();
                    password.put("websiteurl", ed2.getText().toString());
                    password.put("username", ed3.getText().toString());
                    password.put("password", new AES().encrypt(ed4.getText().toString()));
                    password.put("note", ed5.getText().toString());
                    password.put("timemodified", System.currentTimeMillis());
                    Log.v("FIREBASE", "STARTING");

                    db_online.collection("users").document(currentUser.getUid()).collection("passwords").document(ed1.getText().toString())
                            .set(password)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.v("FIREBASE", "FAILED");
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ed1.setText("");
                                    ed2.setText("");
                                    ed3.setText("");
                                    ed4.setText("");
                                    ed5.setText("");
                                    getActivity().onBackPressed();
                                    Log.v("FIREBASE", "CREATED");
                                }

                            });


                }
            }
        });
        edit_entry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setEnabled(false);
                ed2.setEnabled(true);
                ed3.setEnabled(true);
                ed4.setEnabled(true);
                ed5.setEnabled(true);
                generate.setVisibility(View.VISIBLE);
                save_changes_button.setVisibility(View.VISIBLE);
                edit_entry_button.setVisibility(View.INVISIBLE);
                add_entry_button.setVisibility(View.INVISIBLE);
            }
        });


        if (activityType.equals("add")) {
            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed4.setText("");
            ed5.setText("");
            add_entry_button.setVisibility(View.VISIBLE);
            save_changes_button.setVisibility(View.INVISIBLE);
            edit_entry_button.setVisibility(View.INVISIBLE);

        } else if (activityType.equals("edit")) {

            edit_entry_button.setVisibility(View.INVISIBLE);
            save_changes_button.setVisibility(View.VISIBLE);
            add_entry_button.setVisibility(View.INVISIBLE);

            String entrynamet = getActivity().getIntent().getStringExtra("entry");
            String websiteurlt = getActivity().getIntent().getStringExtra("websiteurl");
            String usernamet = getActivity().getIntent().getStringExtra("username");
            String passwordt = getActivity().getIntent().getStringExtra("password");
            String notet = getActivity().getIntent().getStringExtra("notes");
            String timemodifiedt= getActivity().getIntent().getStringExtra("timemodified");
            ed1.setText(entrynamet);
            ed2.setText(websiteurlt);
            new ImageLoadTask().execute(ed2.getText().toString());
            ed3.setText(usernamet);
            ed4.setText(passwordt);
            ed5.setText(notet);
            timedatetv.setText("Last Modified:"+dateAsString(timemodifiedt));
            ed1.setEnabled(false);
            ed2.requestFocus();

        } else if (activityType.equals("view")) {
            String entrynamet = getActivity().getIntent().getStringExtra("entry");
            String websiteurlt = getActivity().getIntent().getStringExtra("websiteurl");
            String usernamet = getActivity().getIntent().getStringExtra("username");
            String passwordt = getActivity().getIntent().getStringExtra("password");
            String notet = getActivity().getIntent().getStringExtra("notes");
            String timemodifiedt= getActivity().getIntent().getStringExtra("timemodified");
            ed1.setText(entrynamet);
            ed2.setText(websiteurlt);
            ed3.setText(usernamet);
            ed4.setText(passwordt);
            ed5.setText(notet);
            timedatetv.setText("Last Modified:"+dateAsString(timemodifiedt));
            new ImageLoadTask().execute(ed2.getText().toString());
            ed1.setEnabled(false);
            ed2.setEnabled(false);
            ed3.setEnabled(false);
            ed4.setEnabled(false);
            ed5.setEnabled(false);
            generate.setVisibility(View.INVISIBLE);
            edit_entry_button.setVisibility(View.VISIBLE);
            add_entry_button.setVisibility(View.INVISIBLE);
            save_changes_button.setVisibility(View.INVISIBLE);
        }


        return view;

    }
    private boolean isSensorAvialable() {
        return getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
    }
    private String dateAsString(String dateAsLong)
    {
        Timestamp ts=new Timestamp(Long.parseLong(dateAsLong));
        Date date=new Date(ts.getTime());
        return date.toString();
    }

    class ImageLoadTask extends AsyncTask<String,Void, Bitmap>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter
            // animation
            // rate
            animation.setRepeatCount(4); // Repeat animation
            // infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
            // end so the button will
            // fade back in
            logoImage.startAnimation(animation);
//            imageProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
//            https://i.olsh.me/icon?size=80..120..200&url=www.google.com

            try {
                URL url = new URL("https://i.olsh.me/icon?size=80..120..200&url="+strings[0]+"");
                URLConnection conection =
                        url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile =
                        conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input =
                        new BufferedInputStream
                                (url.openStream(),
                                        10 * 1024);
                Bitmap bm= BitmapFactory.decodeStream(input);
                return bm;

                }
            catch (Exception e)
            {

            }
                return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            imageProgress.setVisibility(View.INVISIBLE);
            logoImage.clearAnimation();
            logoImage.setImageBitmap(bitmap);
            if(bitmap==null)
            {
                Drawable myDrawable = getResources().getDrawable(R.drawable.fingerprint);
                logoImage.setImageDrawable(myDrawable);
            }


        }
    }
}
