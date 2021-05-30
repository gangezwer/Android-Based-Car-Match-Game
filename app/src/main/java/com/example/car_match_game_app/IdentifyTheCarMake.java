package com.example.car_match_game_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IdentifyTheCarMake extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String[] allCarMakes = {"BMW", "Benz", "Ford", "Toyota", "Honda","Audi"};

    // Arrays that reference to the car images categorized according to their makers.
    String[] imagesOfBMW = {"bmw1221", "bmw1222", "bmw1223", "bmw1224", "bmw1225","bmw1251","bmw1252","bmw1253","bmw1254","bmw1255"};
    String[] imagesOfBenz = {"bnz1226", "bnz1227", "bnz1228", "bnz1229", "bnz1230","bnz1276","bnz1277","bnz1278","bnz1279","bnz1280"};
    String[] imagesOfToyota = {"tyo1231", "tyo1232", "tyo1233", "tyo1234", "tyo1235","tyo1266","tyo1267","tyo1268","tyo1269","tyo1270"};
    String[] imagesOfHonda = {"hon1236", "hon1237", "hon1238", "hon1239", "hon1240","hon1271","hon1272","hon1273","hon1274","hon1275"};
    String[] imagesOfAudi = {"aui1241", "aui1242", "aui1243", "aui1244", "aui1245","aui1256","aui1257","aui1258","aui1259","aui1260"};
    String[] imagesOfFord = {"for1246", "for1247", "for1248", "for1249", "for1250","for1261","for1262","for1263","for1264","for1265"};

    //adding all the displayed images to an arraylist to stop repeating them
    List<String> allDisplayedImages = new ArrayList<>();

    private CountDownTimer roundCountDownTimer;
    private TextView roundCountDownTimerText;
    public String selectedSpinnerLabel;
    private TextView resultMessage;
    private TextView correctAnswer;
    private Button buttonSubmitNext;
    private boolean toggleCountdown;
    private ProgressBar roundCountDownTimerProgress;
    public String randomCarMake;
    public int randomImageIndex;
    public String randomImageOfChosenCarMake;
    private long countdownTime;          // used to pass the remaining countdown time into the saved state when the device is rotated
    private int remainingTime;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_identify_the_car_make);
        resultMessage =  findViewById(R.id.result_text);         // Connecting TextView to variable
        correctAnswer =  findViewById(R.id.correct_car_make_answer);         // Connecting TextView to variable
        buttonSubmitNext = findViewById(R.id.submit_button);         // Connecting Button to variable

        //Checking the toggle is on or off
        toggleCountdown = getIntent().getExtras().getBoolean("Countdown");

        // create the spinner
        Spinner spinner = findViewById(R.id.car_make_spinner);

        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        //Arrayadapter creation using a string array and spinner layout is used for this
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cars_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

        // restoring the state of the activity when the screen is rotated
        if (savedInstanceState != null) {

            randomCarMake = savedInstanceState.getString("displayed_breed");
            randomImageIndex = savedInstanceState.getInt("displayed_index");
            allDisplayedImages = savedInstanceState.getStringArrayList("displayed_images");
            selectedSpinnerLabel = savedInstanceState.getString("spinner_chosen");
            countdownTime = savedInstanceState.getLong("time_left");

            CharSequence correctAns = savedInstanceState.getCharSequence("correct_ans_text");
            correctAnswer.setText(correctAns);

            CharSequence resultText = savedInstanceState.getCharSequence("result_text");
            resultMessage.setText(resultText);

            System.out.println(resultMessage.toString());

            String buttonText = savedInstanceState.getString("button_text");
            System.out.println(buttonText);

            //checking the toggle is on and proceed the  following
            if (toggleCountdown) {
                roundCountDownTimerText = findViewById(R.id.timer_text);
                //letting the countdown timer visible
                roundCountDownTimerText.setVisibility(View.VISIBLE);

                remainingTime = (int) (countdownTime / 1000);
                roundCountDownTimerText.setText(Integer.toString(remainingTime));

                // Re applying circular progress countdown colours
                roundCountDownTimerProgress = findViewById(R.id.circular_progress_timer);        // circular progress bar for countdown
                roundCountDownTimerProgress.setProgress(100);            // resetting progress bar

                // getting the drawable shape of the progress bar
                final GradientDrawable mProgressCircle = (GradientDrawable) roundCountDownTimerProgress.getProgressDrawable();

                if (remainingTime <= 5) {
                    mProgressCircle.setColor(Color.RED);
                } else if (remainingTime <= 10) {
                    mProgressCircle.setColor(Color.parseColor("#ffa000"));
                } else {
                    mProgressCircle.setColor(Color.parseColor("#42bf2d"));
                }

                // show the remaining time for the user
                if (resultText.toString().equals("CORRECT!")) {
                    resultMessage.setTextColor(Color.parseColor("#42bf2d"));
                    roundCountDownTimerText.setText(Integer.toString(remainingTime));
                } else if (resultText.toString().equals("WRONG!")) {
                    resultMessage.setTextColor(Color.RED);
                    roundCountDownTimerText.setText(Integer.toString(remainingTime));
                } else if (resultText.toString().equals("Time's up!")) {
                    resultMessage.setTextColor(Color.YELLOW);
                    roundCountDownTimerText.setText(Integer.toString(remainingTime));
                } else {
                    if (toggleCountdown) {
//                // run the timer only if a result isn't displayed already
                        runTimer(countdownTime);
                    }
                }
            }

            if (buttonText.equals("Next")) {
                buttonSubmitNext.setText("Next");

                if (resultText.toString().equals("CORRECT!")) {
                    resultMessage.setTextColor(Color.parseColor("#42bf2d"));
                } else if(resultText.toString().equals("WRONG!")){
                    resultMessage.setTextColor(Color.RED);
                    correctAnswer.setTextColor(Color.YELLOW);
                }
            } else{
                buttonSubmitNext.setText("Identify");
            }

            // getting the String value that comes with the key "displayed_breed"
            displayRelevantImage(randomCarMake, randomImageIndex);

            // display chosen random image
            ImageView imageCar = findViewById(R.id.random_car_image);
            int resource_id = getResources().getIdentifier(randomImageOfChosenCarMake, "drawable", "com.example.car_match_game_app");
            imageCar.setImageResource(resource_id);

        } else {
            //if activity was created for the first time display random image
            displayRandomImage();

            //if the toggle is on set the timer for 20000 millisec or else follow the normal procedure
            final long SET_TIME = 20000;

            if (toggleCountdown) {
                runTimer(SET_TIME);

            } else {
                // proceed with the normal game flow, without the countdown timer
            }
        }
    }

    @Override
    protected void onDestroy() {
        // when going back to the main menu destroy the current activity and stops the timer
        super.onDestroy();
        if (toggleCountdown) {
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("displayed_breed", randomCarMake);         // saving displayed breed
        outState.putInt("displayed_index", randomImageIndex);       // saving displayed index of chosen image
        outState.putStringArrayList("displayed_images", (ArrayList<String>) allDisplayedImages);            // saving arrayList of displayed images
        outState.putString("spinner_chosen", selectedSpinnerLabel);          // spinner that has been chosen
        outState.putCharSequence("result_text", resultMessage.getText());
        outState.putCharSequence("correct_ans_text", correctAnswer.getText());

        outState.putString("button_text", buttonSubmitNext.getText().toString());     // to make sure that button text isn't reset to "Submit" all the time
        outState.putLong("time_left", countdownTime);          // time left in countdown timer
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedSpinnerLabel = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void displayRandomImage() {           // method used to display a random image of a random breed

        ImageView imageCar = findViewById(R.id.random_car_image);

        do {            // making sure that the same images aren't repeated

            randomCarMake = allCarMakes[getRandomBreed()];           // get a random carmake
            randomImageIndex = getRandomImage();                // get a random image of a particular car make

            // display a chosen image of the car relevent to it's make
            displayRelevantImage(randomCarMake, randomImageIndex);

        } while (allDisplayedImages.contains(randomImageOfChosenCarMake));

        allDisplayedImages.add(randomImageOfChosenCarMake);

        // display chosen random image
        int resource_id = getResources().getIdentifier(randomImageOfChosenCarMake, "drawable", "com.example.car_match_game_app");
        imageCar.setImageResource(resource_id);
    }

    // display a chosen image
    public void displayRelevantImage(String randomCarMake, int randomImageIndex) {
        switch (randomCarMake) {
            case "BMW":
                randomImageOfChosenCarMake = imagesOfBMW[randomImageIndex];     // get a random image reference
                break;
            case "Benz":
                randomImageOfChosenCarMake = imagesOfBenz[randomImageIndex];
                break;
            case "Toyota":
                randomImageOfChosenCarMake = imagesOfToyota[randomImageIndex];
                break;
            case "Honda":
                randomImageOfChosenCarMake = imagesOfHonda[randomImageIndex];
                break;
            case "Audi":
                randomImageOfChosenCarMake = imagesOfAudi[randomImageIndex];
                break;
            case "Ford":
                randomImageOfChosenCarMake = imagesOfFord[randomImageIndex];
        }
    }

    public int getRandomBreed() {
        //get random number between 0-10 (index range for 11 breeds in the array)
        Random r = new Random();
//        displayToast(Integer.toString(getRandomBreed()));         // not needed. generates another random number
        return r.nextInt(6);
    }

    public int getRandomImage() {
        //get random number between 0-9 (index range for 10 image references in the array)
        Random r = new Random();
        return r.nextInt(10);
    }

    public void submitCheck(View view) {
        getResult();            // follow steps to display result
    }

    public void getResult() {         // steps that are followed when the result is required to be shown
        if (buttonSubmitNext.getText().equals("Identify")) {        // Submit has been clicked
            if (selectedSpinnerLabel.equals(randomCarMake)) {
                resultMessage.setText("CORRECT!");
                resultMessage.setTextColor(Color.parseColor("#42bf2d"));
            } else {
                resultMessage.setText("WRONG!");
                resultMessage.setTextColor(Color.RED);

                correctAnswer.setText(randomCarMake);
                correctAnswer.setTextColor(Color.YELLOW);
            }
            buttonSubmitNext.setText("Next");

            // reset the countdown timer, for new image if identify is clicked before timer ends
            if (toggleCountdown) {
                roundCountDownTimer.cancel();
            }

        } else {            // Next has been clicked
            buttonSubmitNext.setText("Identify");
            resultMessage.setText("");
            correctAnswer.setText("");

            if (toggleCountdown) {
//                roundCountDownTimer.start();
                runTimer(20000);
            }
            displayRandomImage();
        }
    }

    public void runTimer(long setTime) {

        roundCountDownTimerText = findViewById(R.id.timer_text);
        roundCountDownTimerText.setVisibility(View.VISIBLE);             // show countdown timer

        roundCountDownTimerProgress = findViewById(R.id.circular_progress_timer);        // circular progress bar for countdown
        roundCountDownTimerProgress.setProgress(100);            // resetting progress bar

        // getting the drawable shape of the progress bar
        final GradientDrawable mProgressCircle = (GradientDrawable) roundCountDownTimerProgress.getProgressDrawable();

        roundCountDownTimer = new CountDownTimer(setTime, 1000) {

            public void onTick(long millisUntilFinished) {
                remainingTime = (int) (1 + (millisUntilFinished / 1000));
                roundCountDownTimerText.setText(Integer.toString(remainingTime));
                // updating progress bar
                roundCountDownTimerProgress.setProgress(remainingTime * 5);

                if (remainingTime <= 5) {
                    mProgressCircle.setColor(Color.RED);
                } else if (remainingTime <= 10) {
                    mProgressCircle.setColor(Color.parseColor("#ffa000"));
                } else {
                    mProgressCircle.setColor(Color.parseColor("#42bf2d"));
                }

                countdownTime = millisUntilFinished;
            }

            public void onFinish() {
                roundCountDownTimerProgress.setProgress(0);            // updating progress bar

                if (buttonSubmitNext.getText().equals("Identify")) {             // checking if Next button was clicked by the user
//                        "Submit" will be shown only if Next was clicked
                    roundCountDownTimerText.setText(Integer.toString(0));
                    getResult();            // follow steps to display result
                    //repeating should be done only when the "Next button is clicked"
                }
            }

        }.start();
    }
}

/*
------------------References used for this activity----------------------

1. https://www.codota.com/code/java/methods/android.graphics.drawable.GradientDrawable/setColor

2. https://stackoverflow.com/questions/20010997/circular-progress-bar-for-a-countdown-timer

3. https://stackoverflow.com/questions/13927269/use-of-ondestroy-in-android

4. https://stackoverflow.com/questions/151777/how-to-save-an-activity-state-using-save-instance-state

5. https://www.tutorialspoint.com/android/android_spinner_control.htm

 */
