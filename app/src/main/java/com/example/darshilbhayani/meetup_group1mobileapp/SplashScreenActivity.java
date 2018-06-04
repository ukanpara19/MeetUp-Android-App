package com.example.darshilbhayani.meetup_group1mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class  SplashScreenActivity extends AppCompatActivity {

    public static final int DELAY_MILLIS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* ((AnimationDrawable) getWindow().getDecorView().getBackground()).start();*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_MILLIS);

    }
}

