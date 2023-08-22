package com.example.clearmind;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class TimeBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "it reaches here", Toast.LENGTH_SHORT).show();
        Log.d("TimeBroadcast", "Notification triggered");

//        Log.d("TimeBroadcast", "10 seconds interval");

//        Intent repeating_intent = new Intent()
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context, "@time")
                .setContentTitle("ClearMind Reminder")
                .setContentText("Hi, it's time to continue your journey to overcoming the procrastination with ACT in ClearMind.")
                .setSmallIcon(R.drawable.notification_icon)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        manager.notify(1, notification);
    }
}
