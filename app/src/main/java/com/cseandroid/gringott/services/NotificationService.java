package com.cseandroid.gringott.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.cseandroid.gringott.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    NotificationManager manager;
    int notifyID=1;
    Notification myNotification;

    public NotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        super.onMessageReceived(remoteMessage);
    }

    private void showNotification(String title, String body) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);




        manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.O) {

                    String channel_Id = "my_channel_01";
                    // The id of the channel.
                    CharSequence channelName =
                            "NotifChannel";
                    // The user-visible name of the channel.
                    int channelImportance =
                            NotificationManager.IMPORTANCE_HIGH;



                    // Create a notification and
                    // set the notification channel.
                    NotificationChannel channel =
                            new NotificationChannel
                                    (channel_Id,
                                            channelName,
                                            channelImportance);
                    manager.createNotificationChannel
                            (channel);

                    //Create the intent that’ll fire
                    // when the user taps the notification//
                    Intent intent = new Intent
                            (Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.gringotts.com/"));
                    //PendingIntent.FLAG_UPDATE_CURRENT -
                    // Flag indicating that if the described
                    // PendingIntent already exists,
                    // then keep it but replace its extra data
                    // with what is in this new Intent.
                    PendingIntent pendingIntent =
                            PendingIntent.getActivity
                                    (getApplicationContext(), 1,
                                            intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification.Builder builder  =
                            new Notification.Builder
                                    (getApplicationContext(),channel_Id);
                    builder.setContentTitle(title);
                    //builder.setAutoCancel(true);
                    builder.setOngoing(true);
                    builder.setContentText(body);
                    builder.setSmallIcon(R.drawable.fingerprint);
                    builder.setContentIntent(pendingIntent);
                    myNotification = builder.build();

                    manager.notify(notifyID, myNotification);

                   /*
                   This pair identifies this notification
                   from your app to the system,
                   so that pair should be unique within your app.
                    */
                }
                else{
                    Intent intent =
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.gringotts.com/"));
                    //PendingIntent.FLAG_UPDATE_CURRENT -
                    // Flag indicating that if the described
                    // PendingIntent already exists,
                    // then keep it but replace its
                    // extra data with what is in this new Intent.
                    PendingIntent pendingIntent =
                            PendingIntent.getActivity
                                    (getApplicationContext(), 1,
                                            intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification.Builder builder =
                            new Notification.Builder
                                    (getApplicationContext());
                    builder.setAutoCancel(true);
                    builder.setContentTitle(title);
                    builder.setContentText(body);
                    builder.setSmallIcon(R.drawable.fingerprint);
                    builder.setContentIntent(pendingIntent);
                    builder.setOngoing(true);
                    // builder.setSubText("This is subtext...");   //API level 16
                    // builder.setNumber(100);
                    myNotification= builder.build();
                    //  myNotication = builder.getNotification();
                    // myNotication.flags = Notification.FLAG_AUTO_CANCEL;
                    manager.notify(notifyID,myNotification);



                }

    }
}
