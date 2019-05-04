package com.varunsaini.android.bestchatapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.varunsaini.android.bestchatapp.AppPreferences;
import com.varunsaini.android.bestchatapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppPreferences appPreferences = new AppPreferences(this);
        if(appPreferences.readUId()!=null){
            Intent i = new Intent(this,MainBaseActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
        finish();

    }
}
