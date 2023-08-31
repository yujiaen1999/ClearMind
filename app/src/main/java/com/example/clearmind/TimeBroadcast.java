package com.example.clearmind;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class TimeBroadcast extends BroadcastReceiver {

    private String username;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "it reaches here", Toast.LENGTH_SHORT).show();
        Log.d("TimeBroadcast", "Notification triggered");

        this.username = intent.getStringExtra("username");

        // set click response action (Error)
//        Intent newintent = new Intent(context, LearnActivity.class);
//        newintent.putExtra("username", username);
//        PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, newintent, PendingIntent.FLAG_IMMUTABLE);


        // setup notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context, "@time")
                .setContentTitle("ClearMind Reminder")
                .setContentText("Continue your journey with ClearMind.")
                .setSmallIcon(R.drawable.notification_icon)
//                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        manager.notify(1, notification);
    }
}
