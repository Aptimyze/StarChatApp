package com.chatr.random.stranger.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.FirebaseDatabase;
import com.chatr.random.stranger.AppPreferences;
import com.chatr.random.stranger.Constants;
import com.chatr.random.stranger.R;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_splash);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final AppPreferences appPreferences = new AppPreferences(SplashActivity.this);
//                if(appPreferences.readUId()!=null){
//                    Intent i = new Intent(SplashActivity.this,MainBaseActivity.class);
//                    startActivity(i);
//                }else{
//                    Intent i = new Intent(SplashActivity.this,LoginActivity.class);
//                    startActivity(i);
//                }
//                MobileAds.initialize(SplashActivity.this, Constants.YOUR_AD_MOB_APP_ID);
//                finish();
//            }
//        },2000);


        final AppPreferences appPreferences = new AppPreferences(this);
        if(appPreferences.readUId()!=null){
            Intent i = new Intent(this,MainBaseActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
        MobileAds.initialize(this, Constants.YOUR_AD_MOB_APP_ID);
//
//
    }
}
