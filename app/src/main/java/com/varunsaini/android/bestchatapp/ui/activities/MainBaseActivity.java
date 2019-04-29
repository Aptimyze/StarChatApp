package com.varunsaini.android.bestchatapp.ui.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.ui.fragments.AllChatFragment;
import com.varunsaini.android.bestchatapp.ui.fragments.AllGroupFragment;

public class MainBaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_base);
        initViews();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setFragment(new AllChatFragment());
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
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.action_chats:
                fragment = new AllChatFragment();
                break;
            case R.id.action_groups:
                fragment = new AllGroupFragment();
                break;
        }
        return setFragment(fragment);
    }
}
