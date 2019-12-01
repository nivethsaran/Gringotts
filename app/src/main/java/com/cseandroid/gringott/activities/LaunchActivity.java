package com.cseandroid.gringott.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.cseandroid.gringott.R;

import static java.lang.Thread.sleep;

public class LaunchActivity extends AppCompatActivity {
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        progressBar=findViewById(R.id.progress_bar_circular);
        progressBar.setVisibility(View.VISIBLE);
        //new LaunchTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LaunchTask().execute();
    }

    class LaunchTask extends AsyncTask<Void, Void, Boolean> {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
       protected Boolean doInBackground(Void... voids) {
           try {
               sleep(2000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           if(isNetworkAvailable())
           {
               return true;
           }
           else
           {
               return false;
           }

       }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Boolean netAvl) {
            super.onPostExecute(netAvl);
            progressBar.setVisibility(View.INVISIBLE);
            if(netAvl)
            {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    ActivityCompat.requestPermissions(LaunchActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                else {

                }

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(LaunchActivity.this, R.style.AlertDialogCustom));
                    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),1010);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();

                }
                else{
                Intent intent = new Intent(LaunchActivity.this, PinActivity.class);
                startActivity(intent);
                }
            }
            else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(LaunchActivity.this, R.style.AlertDialogCustom));
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Turn on Internet or Close the Appplication!");
                alertDialog.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new LaunchTask().execute();
                    }
                });
                alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                alertDialog.setTitle("No Internet Connection");
                alertDialog.show();
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
