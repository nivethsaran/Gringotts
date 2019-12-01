package com.cseandroid.gringott.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.cseandroid.gringott.receiver.LogoutReceiver;

public class TimeoutService extends Service {
    public static CountDownTimer timer;
    public TimeoutService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("SERVICE","onStartCommand");
            setAlarm(10*1000);
            Log.v("SERVICE","alarmSet");


        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("SERVICE","onCreate");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("SERVICE","onDestroy");

    }

    public void setAlarm(long triggerTime) {
        final Context context = getApplicationContext();
        Intent intent = new Intent(context, LogoutReceiver.class);
        intent.setAction("TIMEOUT");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setExact(
                AlarmManager.RTC,
                System.currentTimeMillis()+triggerTime,
                pendingIntent);
        stopSelf();

    }
}
