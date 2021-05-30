package com.example.car_match_game_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import static android.view.WindowManager.*;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mSwitchCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        try {
            this.getSupportActionBar().hide();              // remove title bar of app
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_main);

        mSwitchCountdown = findViewById(R.id.switch1);

        Button identifyBreedButton = findViewById(R.id.button);
        identifyBreedButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, IdentifyTheCarMake.class);

            // let the activity know whether the countdown is toggled on or off
            intent.putExtra("Countdown", mSwitchCountdown.isChecked());

            startActivity(intent);
        });

        Button identifyCarImageButton = findViewById(R.id.button5);
        identifyCarImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, IdentifyTheCarImage.class);

            intent.putExtra("Countdown", mSwitchCountdown.isChecked());

            startActivity(intent);
        });

        Button hintsButton = findViewById(R.id.button4);
        hintsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Hints.class);

            intent.putExtra("Countdown", mSwitchCountdown.isChecked());

            startActivity(intent);
        });

        Button advanceLevelButton = findViewById(R.id.button3);
        advanceLevelButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AdvanceLevel.class);

            intent.putExtra("Countdown", mSwitchCountdown.isChecked());

            startActivity(intent);
        });

    }


    //exiting the running program and restart the application
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        System.exit(Integer.parseInt("0"));
    }
}

/*
------------------References used for this activity----------------------

1.https://stackoverflow.com/questions/6413700/android-proper-way-to-use-onbackpressed-with-toast

2.https://stackoverflow.com/questions/4186021/how-to-start-new-activity-on-button-click

3. https://stackoverflow.com/questions/5431365/how-to-hide-status-bar-in-android
 */