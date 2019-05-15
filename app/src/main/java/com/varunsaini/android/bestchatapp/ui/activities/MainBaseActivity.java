package com.varunsaini.android.bestchatapp.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.FirebaseDatabase;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.onesignal.OneSignal;
import com.varunsaini.android.bestchatapp.AppPreferences;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.network.Firebase;
import com.varunsaini.android.bestchatapp.ui.fragments.SettingsFragment;
import com.varunsaini.android.bestchatapp.ui.fragments.AllChatFragment;
import com.varunsaini.android.bestchatapp.ui.fragments.RandomUserFragment;

public class MainBaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Connectable {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    public static Context contextOfApplication;
    Merlin merlin;
    MerlinsBeard merlinsBeard;
    AlertDialog alert;
    Firebase firebase;
    String myUid;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_base);
        initViews();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("885CB55CB5034BB39E0F3BBA27D72436").build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("885CB55CB5034BB39E0F3BBA27D72436").build());
        firebase= new Firebase(getApplication());
        merlin = new Merlin.Builder().withConnectableCallbacks().build(this);
        merlinsBeard = MerlinsBeard.from(this);
        AppPreferences appPreferences = new AppPreferences(this);
        myUid = appPreferences.readUId();
        contextOfApplication = getApplicationContext();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setFragment(new AllChatFragment());
        putUserAsOnline();
        (getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_abs_layout);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.sendTag("user_name",appPreferences.readUId());
    }

    private void checkInternetConnection() {


        if (merlinsBeard.isConnected()) {
//            Toast.makeText(this, "You are connected to internet", Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(frameLayout,R.string.connect_to_sync,Snackbar.LENGTH_LONG).show();
            }
    }

    private boolean setFragment(Fragment framgent) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, framgent, "frag1");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
        transaction.commit();
        return true;

    }

    private void initViews() {
        frameLayout = findViewById(R.id.frame_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mAdView = findViewById(R.id.adView);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.action_chats:
                fragment = new AllChatFragment();
                break;
            case R.id.action_groups:
                fragment = new RandomUserFragment();
                break;
            case R.id.action_account:
                fragment = new SettingsFragment();
                break;
        }
        return setFragment(fragment);
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
        checkInternetConnection();
        merlin.registerConnectable(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void putUserAsOnline() {
        firebase.putUserAsOnline(myUid);
    }

    @Override
    protected void onPause() {
        merlin.unbind();
        super.onPause();
    }

    private void putUserAsOffline() {
        firebase.putUserAsOffline(myUid);
    }

    @Override
    public void onConnect() {
//        Toast.makeText(this, "You are now connected to internet", Toast.LENGTH_SHORT).show();
//        if(alert.isShowing()) {
//            alert.cancel();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        putUserAsOffline();
    }
}
