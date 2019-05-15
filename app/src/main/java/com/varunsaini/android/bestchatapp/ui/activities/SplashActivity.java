package com.varunsaini.android.bestchatapp.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Merlin;
import com.onesignal.OneSignal;
import com.varunsaini.android.bestchatapp.AppPreferences;
import com.varunsaini.android.bestchatapp.Constants;
import com.varunsaini.android.bestchatapp.R;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_splash);

        final AppPreferences appPreferences = new AppPreferences(this);
        if(appPreferences.readUId()!=null){
            Intent i = new Intent(this,MainBaseActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
        MobileAds.initialize(this, Constants.YOUR_AD_MOB_APP_ID);


        finish();
    }
}
