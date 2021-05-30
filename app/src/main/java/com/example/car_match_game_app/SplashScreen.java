package com.example.car_match_game_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        //hiding the action bar
        try {
            this.getSupportActionBar().hide();              // remove title bar of app
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_splash_screen);

        //creating the animation for splash screen with time of 3000 milli seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
        }, 3000);

    }
}
/*
------------------References used for this activity----------------------

1. https://stackoverflow.com/questions/50095360/animated-gif-not-playing

2. https://stackoverflow.com/questions/37293082/uses-sdkminsdkversion-15-cannot-be-smaller-than-version-16-declared-in-library

3. https://stackoverflow.com/questions/5431365/how-to-hide-status-bar-in-android

 */