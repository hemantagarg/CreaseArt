package com.app.creaseart;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.creaseart.activities.Splash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by Hemanta on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCM", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData() != null) {
            //  Log.d("FCM", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            Log.e("body", "*" + remoteMessage.getData());

            sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"));
//            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTag());
        }
    }


    private void sendNotification(String messageBody, String title) {
        PendingIntent pendingIntent;
        Log.e("title", "**" + title);
        Random r = new Random();
        int when = r.nextInt(1000);
        Intent intent2 = new Intent(this, Splash.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent2,
                PendingIntent.FLAG_ONE_SHOT);

        // String title = "Veraxe";
        int currentAPIVersion = Build.VERSION.SDK_INT;
        int icon = 0;
        if (currentAPIVersion == Build.VERSION_CODES.KITKAT) {
            icon = R.drawable.loginlogo;

        } else if (currentAPIVersion >= Build.VERSION_CODES.LOLLIPOP) {
            icon = R.drawable.loginlogo;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(when, notificationBuilder.build());
    }
}