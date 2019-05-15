package com.varunsaini.android.bestchatapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.novoda.merlin.Merlin;
import com.onesignal.OneSignal;

public class ChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        AppPreferences appPreferences  = new AppPreferences(this);

        if(appPreferences.readNotificationState().equals("false")) {
            OneSignal.setSubscription(false);
        }
      OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
    }


}
