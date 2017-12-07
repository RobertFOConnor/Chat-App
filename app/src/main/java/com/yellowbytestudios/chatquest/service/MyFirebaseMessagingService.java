package com.yellowbytestudios.chatquest.service;

import android.app.NotificationManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "my_channel_01")
                        .setContentTitle(remoteMessage.getFrom())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setVibrate(new long[]{500, 500})
                        .setLights(Color.BLUE, 3000, 3000)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        int mNotificationId = (int) (Math.random() * 1000);// Set an ID for the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);// Get an instance of NotificationManager service

        if (notificationManager != null) {
            notificationManager.notify(mNotificationId, mBuilder.build()); // Build the notification and issue it.
        }
    }
}
