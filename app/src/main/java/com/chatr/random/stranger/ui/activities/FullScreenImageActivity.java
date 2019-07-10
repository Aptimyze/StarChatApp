package com.chatr.random.stranger.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chatr.random.stranger.Constants;
import com.chatr.random.stranger.R;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        String imageUrl = getIntent().getStringExtra(Constants.IMAGE_URL_FOR_FULL_SCREEN);
        if(imageUrl!=null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into((ImageView) findViewById(R.id.fullscreen_content));
        }else{
            Glide.with(this).load(R.drawable.placeholder_person).into((ImageView) findViewById(R.id.fullscreen_content));
        }
    }
}
