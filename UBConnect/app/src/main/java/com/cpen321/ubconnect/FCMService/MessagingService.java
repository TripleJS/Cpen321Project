package com.cpen321.ubconnect.FCMService;

import android.util.Log;

import com.cpen321.ubconnect.model.data.FCMToken;
import com.cpen321.ubconnect.viewModel.MainViewModel;
import com.google.firebase.messaging.FirebaseMessagingService;

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
}
