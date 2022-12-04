package com.xa.xpensauditor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
        final String CHANNEL_ID = "ABCD";

        System.out.println("Notification recieved!!! " + title + " " + text);


        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Heads up ABCD",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notificatiion  = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_action_group)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, notificatiion.build());

        super.onMessageReceived(message);
    }
}
