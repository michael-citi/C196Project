package com.michaelciti.c196project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotifyMe extends BroadcastReceiver {

    private static final String NOTIFICATION_TITLE = "Title";
    private static final String NOTIFICATION_MESSAGE = "Message";
    private static final String CHANNEL_ID = "c196_channel";
    private static final String CHANNEL_DESCRIPTION = "C196 Notification Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String message = intent.getStringExtra(NOTIFICATION_MESSAGE);

        Intent mIntent = new Intent(context, CourseDetailActivity.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 1, mIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentIntent(pIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel nc = new NotificationChannel(CHANNEL_ID, CHANNEL_DESCRIPTION, NotificationManager.IMPORTANCE_DEFAULT);
        nm.createNotificationChannel(nc);
        nm.notify(0, builder.build());
    }
}
