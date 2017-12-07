package com.yellowbytestudios.chatquest.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yellowbytestudios.chatquest.chatmessage.ChatMessage;
import com.yellowbytestudios.chatquest.MainActivity;
import com.yellowbytestudios.chatquest.MyChildEventListener;
import com.yellowbytestudios.chatquest.R;

public class ChatMessageService extends Service {

    public ChatMessageService() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("newRef");

        ref.addChildEventListener(new MyChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!MainActivity.active) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    triggerNotification(message);
                }
            }
        });
    }

    private void triggerNotification(ChatMessage message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "my_channel_01")
                        .setContentTitle(message.getMessageUser())
                        .setContentText(message.getMessageText())
                        .setVibrate(new long[]{500, 500})
                        .setSmallIcon(R.drawable.profile_pic_sample)
                        .setLights(Color.BLUE, 3000, 3000)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        int mNotificationId = (int) (Math.random() * 1000);// Set an ID for the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);// Get an instance of NotificationManager service

        if (notificationManager != null) {
            notificationManager.notify(mNotificationId, mBuilder.build()); // Build the notification and issue it.
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
