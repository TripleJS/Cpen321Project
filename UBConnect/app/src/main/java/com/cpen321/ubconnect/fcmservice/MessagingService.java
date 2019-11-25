package com.cpen321.ubconnect.fcmservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.cpen321.ubconnect.R;

import com.cpen321.ubconnect.ui.viewothers.ViewOnlyOthersAnswerActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d("Fuck", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        MainViewModel mainViewModel = new MainViewModel();
//        FCMToken fcmToken = new FCMToken();
//        fcmToken.setToken(token);
//        mainViewModel.sendRegistrationToServer(fcmToken);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Fuck", "onMessageReceived: ");

//        Intent intentNotification = new Intent();
//        intentNotification.setAction("com.from.notification");
//        intentNotification.putExtra("data", "newActicity");
//        sendBroadcast(intentNotification);
        Intent intent = new Intent(this, ViewOnlyOthersAnswerActivity.class);
        Map<String, String> data = remoteMessage.getData();
        Log.d("qqqq", "onMessageReceived: " + data.get("questionId") );
        Log.d("qqqq", "onMessageReceived: " + data.get("userId") );
        intent.putExtra("questionId", data.get("questionId"));
        intent.putExtra("userId", data.get("userId"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(0, builder.build());



    }
}
