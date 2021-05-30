package com.example.car_match_game_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hints extends AppCompatActivity {

    public String[] allCarMakes = {"BMW", "Benz", "Ford", "Toyota", "Honda","Audi"};

    // Arrays that reference to the car images categorized according to their makers.
    String[] imagesOfBMW = {"bmw1221", "bmw1222", "bmw1223", "bmw1224", "bmw1225","bmw1251","bmw1252","bmw1253","bmw1254","bmw1255"};
    String[] imagesOfBenz = {"bnz1226", "bnz1227", "bnz1228", "bnz1229", "bnz1230","bnz1276","bnz1277","bnz1278","bnz1279","bnz1280"};
    String[] imagesOfToyota = {"tyo1231", "tyo1232", "tyo1233", "tyo1234", "tyo1235","tyo1266","tyo1267","tyo1268","tyo1269","tyo1270"};
    String[] imagesOfHonda = {"hon1236", "hon1237", "hon1238", "hon1239", "hon1240","hon1271","hon1272","hon1273","hon1274","hon1275"};
    String[] imagesOfAudi = {"aui1241", "aui1242", "aui1243", "aui1244", "aui1245","aui1256","aui1257","aui1258","aui1259","aui1260"};
    String[] imagesOfFord = {"for1246", "for1247", "for1248", "for1249", "for1250","for1261","for1262","for1263","for1264","for1265"};

    List<String> allDisplayedImages = new ArrayList<>();        // all the displayed images are added here. To make sure that images aren't repeated
    private List<String> displayingCarMakes = new ArrayList<>();
    private char[] carMakeName;
    private char[] hintLetters;

    private TextView resultMessage;
    private TextView correctAnswer;
    private Button buttonSubmitNext;
    private boolean toggleCountdown;
    public String randomCarMake;
    public int randomImageIndex;
    public int wrongCount = 0;
    public String randomImageOfChosenCarMake;
    private long countdownTime;          // used to pass the remaining countdown time into the saved state when the device is rotated
    private int remainingTime;
    private String s;
    private CountDownTimer roundCountDownTimer;
    private TextView roundCountDownTimerText;
    private ProgressBar roundCountDownTimerProgress;
    private TextView mprintName;
    private TextView mcharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hints);
        resultMessage = /*(TextView)*/ findViewById(R.id.result_text);         // Connecting TextView to variable
        correctAnswer = /*(TextView)*/ findViewById(R.id.correct_car_make_answer);         // Connecting TextView to variable
        buttonSubmitNext = /*(Button)*/ findViewById(R.id.hints_submit_button);         // Connecting Button to variable
        mprintName = findViewById(R.id.printName);
        mcharacter = findViewById(R.id.character_edittext);

        toggleCountdown = getIntent().getExtras().getBoolean("Countdown");         // getting the status of the switch in the main screen

        // restore the state
        if (savedInstanceState != null) {           // if screen was rotated and activity was restarted

            randomCarMake = savedInstanceState.getString("displayed_car_make");
            randomImageIndex = savedInstanceState.getInt("displayed_index");
            allDisplayedImages = savedInstanceState.getStringArrayList("displayed_images");
            carMakeName = savedInstanceState.getCharArray("car_name");
            hintLetters = savedInstanceState.getCharArray("guessed_letters");
            countdownTime = savedInstanceState.getLong("time_left");
            wrongCount = savedInstanceState.getInt("incorrect_guesses");

            CharSequence correctAns = savedInstanceState.getCharSequence("correct_ans_text");
            correctAnswer.setText(correctAns);

            CharSequence resultText = savedInstanceState.getCharSequence("result_text");
            resultMessage.setText(resultText);

            CharSequence guessedWrods = savedInstanceState.getCharSequence("guessed_words");
            mprintName.setText(guessedWrods);

            System.out.println(resultMessage.toString());

            String buttonText = savedInstanceState.getString("button_text");
            System.out.println(buttonText);

            if (toggleCountdown) {
                roundCountDownTimerText = findViewById(R.id.timer_text);
                roundCountDownTimerText.setVisibility(View.VISIBLE);             // show countdown timer

                remainingTime = (int) (countdownTime / 1000);
                roundCountDownTimerText.setText(Integer.toString(remainingTime));

                // Re applying circular progress countdown colours
                roundCountDownTimerProgress = findViewById(R.id.circular_progress_timer);        // circular progress bar for countdown
                roundCountDownTimerProgress.setProgress(100);            // resetting progress bar

                final GradientDrawable mProgressCircle = (GradientDrawable) roundCountDownTimerProgress.getProgressDrawable();            // getting the drawable shape of the progress bar

                if (remainingTime <= 5) {
                    mProgressCircle.setColor(Color.RED);
                } else if (remainingTime <= 10) {
                    mProgressCircle.setColor(Color.parseColor("#ffa000"));
                } else {
                    mProgressCircle.setColor(Color.parseColor("#42bf2d"));
                }

                if (resultText.toString().equals("CORRECT!")) {
                    resultMessage.setTextColor(Color.parseColor("#42bf2d"));
                    roundCountDownTimerText.setText(Integer.toString(remainingTime));     // show time that was left
                } else if (resultText.toString().equals("WRONG!")) {
                    resultMessage.setTextColor(Color.RED);
                    roundCountDownTimerText.setText(Integer.toString(remainingTime));     // show time that was left
                } else if (resultText.toString().equals("Time's up!")) {
                    resultMessage.setTextColor(Color.YELLOW);
                    roundCountDownTimerText.setText(Integer.toString(remainingTime));     // show time that was left
                }else {
                    if (toggleCountdown) {
                 // run the timer only if a result isn't displayed already
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
                }else if (resultText.toString().equals("Incorrect Character!")){
                    resultMessage.setTextColor(Color.parseColor("#FFFF00"));
                }
            } else{
                buttonSubmitNext.setText("Submit");
            }

            // getting the String value that comes with the key ""
            displayRelevantImage(randomCarMake, randomImageIndex);

            // display chosen random image
            ImageView carImage = findViewById(R.id.random_car_image);
            int resource_id = getResources().getIdentifier(randomImageOfChosenCarMake, "drawable", "com.example.car_match_game_app");
            carImage.setImageResource(resource_id);

        } else {
            displayRandomImage();
            loadNewQuestion();

            final long SET_TIME = 20000;

            if (toggleCountdown) {
                runTimer(SET_TIME);
            } else {
            }
        }

        buttonSubmitNext.setOnClickListener(view -> {
            if (buttonSubmitNext.getText().equals("Submit")) {
                getResult();
            } else {
                resultMessage.setText("");
                correctAnswer.setText("");
                displayRandomImage();
                loadNewQuestion();

                if (toggleCountdown) {
                    if (toggleCountdown) {
                        roundCountDownTimer.cancel();           // reset the countdown timer, for new image, if "Submit" was clicked before the countdown ended
                    }
                    runTimer(20000);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {                // when going back to the main menu
        super.onDestroy();

        if (toggleCountdown) {         // only if the countdown toggle had been turned on
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();           // stopping the countdown running in the background
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("displayed_car_make", randomCarMake);         // saving displayed breed
        outState.putInt("displayed_index", randomImageIndex);       // saving displayed index of chosen image
        outState.putStringArrayList("displayed_images", (ArrayList<String>) allDisplayedImages);            // saving arrayList of displayed images
        outState.putCharSequence("result_text", resultMessage.getText());
        outState.putCharSequence("correct_ans_text", correctAnswer.getText());
        outState.putCharArray("car_name",(char[])carMakeName);
        outState.putCharArray("guessed_letters",(char[])hintLetters);
        outState.putInt("incorrect_guesses",wrongCount);
        outState.putString("button_text", buttonSubmitNext.getText().toString());     // to make sure that button text isn't reset to "Submit" all the time
        outState.putString("entered_words",mprintName.getText().toString());
        outState.putLong("time_left", countdownTime);          // time left in countdown timer
    }


    public void displayRandomImage() {           // method used to display a random image of a random breed

        ImageView carImage = findViewById(R.id.random_car_image);

        do {            // making sure that the same images aren't repeated
            // this will run in an infinite loop once all the images are displayed.
            randomCarMake = allCarMakes[getRandomBreed()];// get a random breed
            randomImageIndex = getRandomImage();                // get a random image of a particular breed


            displayRelevantImage(randomCarMake, randomImageIndex);            // display a chosen image
            loadNewQuestion();

        } while (displayingCarMakes.contains(randomCarMake) || allDisplayedImages.contains(randomImageOfChosenCarMake));

        allDisplayedImages.add(randomImageOfChosenCarMake);

        // display chosen random image
        int resource_id = getResources().getIdentifier(randomImageOfChosenCarMake, "drawable", "com.example.car_match_game_app");
        carImage.setImageResource(resource_id);
    }

    void loadNewQuestion() {
        wrongCount = 0;

        carMakeName = new char[randomCarMake.length()];
        //set a char array for hint letters
        hintLetters = new char[carMakeName.length];

        //shows country name in capital
        String ss = randomCarMake.toUpperCase();
        carMakeName = ss.toCharArray();

        //In hint letters, for every character replace a hyphen instead of a space
        //when starting a hint letters firstly it'll replace as a hyphen
        for (int k = 0; k < carMakeName.length; k++) {
            char c = carMakeName[k];

            if (!(String.valueOf(c).equals(" "))) {
                String s = "-";
                hintLetters[k] = s.charAt(0);
            } else {
                String s = " ";
                hintLetters[k] = s.charAt(0);
            }
        }

        System.out.println(carMakeName);

        mprintName.setText(String.valueOf(hintLetters));
        correctAnswer.setText("");
        resultMessage.setText("");
        buttonSubmitNext.setText("Submit");
    }

    void incorrectCharacter(){
        wrongCount += 1;

        resultMessage.setTextColor(Color.parseColor("#FFFF00"));
        resultMessage.setText("Incorrect Character!");

        if (toggleCountdown) {
            if (roundCountDownTimer != null) {          // if next is touched repeatedly, this will prevent the countdown timer messing up
                roundCountDownTimer.cancel();
            }
            runTimer(20000);
        }
    }

    public void getResult(){
        resultMessage.setText("");
        s = mcharacter.getText().toString().toUpperCase();

        //user is only allowed up to 3 incorrect character guesses
        if (wrongCount >= 2) {
            if (s.length() != 1) {
                Toast.makeText(this, "You can enter only one character at a time!", Toast.LENGTH_LONG).show();
                mcharacter.setText("");
                wrong();
            } else {
                ArrayList<Integer> indexes = new ArrayList();
                //boolean for found the entered character in somewhere through country name
                boolean foundAtLeastOneCharacter = false;
                //check whether the character is there
                for (int i = 0; i < carMakeName.length; i++) {
                    char c = carMakeName[i];

                    //if character is there, position of the character add to the array list
                    if (s.charAt(0) == c) {
                        indexes.add(i);
                        foundAtLeastOneCharacter = true;
                    }
                }

                //if at least one character is there,
                if (foundAtLeastOneCharacter) {
                    //add entered character to the hyphen
                    for (int i : indexes) {
                        hintLetters[i] = s.charAt(0);
                    }

                    //shows hint letters in text view
                    mcharacter.setText("");
                    mprintName.setText(String.valueOf(hintLetters));
                } else {
                    wrong();
                }

                boolean hintsComplete = true;

                //for loop for check whether the hyphens are empty
                for (char c : hintLetters) {
                    String ss = String.valueOf(c);

                    if (ss.equals("-")) {
                        hintsComplete = false;
                        break;
                    }
                }
                //if hyphens are not empty the word is complete and calling correct method
                if (hintsComplete) {
                    correct();
                }
            }

        } else {
            //if user enter a character more than 1
            if (s.length() != 1) {
                Toast.makeText(this, "You can enter only one character at a time!", Toast.LENGTH_LONG).show();
                mcharacter.setText("");
               incorrectCharacter();

            } else {
                //create an array list for check whether the how many characters for one country name
                ArrayList<Integer> indexes = new ArrayList();
                //boolean for found the entered character in somewhere through country name
                boolean foundAtLeastOneCharacter = false;
                //check whether the character is there
                for (int i = 0; i < carMakeName.length; i++) {
                    char c = carMakeName[i];

                    //if character is there, position of the character add to the array list
                    if (s.charAt(0) == c) {
                        indexes.add(i);
                        foundAtLeastOneCharacter = true;
                    }
                }

                //if at least one character is there,
                if (foundAtLeastOneCharacter) {
                    //add entered character to the hyphen
                    for (int i : indexes) {
                        hintLetters[i] = s.charAt(0);
                    }

                    //shows hint letters in text view
                    mcharacter.setText("");
                    mprintName.setText(String.valueOf(hintLetters));
                    if (toggleCountdown) {
                        if (roundCountDownTimer != null) {          // if next is touched repeatedly, this will prevent the countdown timer messing up
                            roundCountDownTimer.cancel();
                        }
                        runTimer(20000);
                    }
                } else {
                    incorrectCharacter();
                }

                boolean hintsComplete = true;

                //for loop for check whether the hyphens are empty
                for (char c : hintLetters) {
                    String ss = String.valueOf(c);

                    if (ss.equals("-")) {
                        hintsComplete = false;
                        break;
                    }
                }
                //if hyphens are not empty the word is complete and calling correct method
                if (hintsComplete) {
                    correct();
                }
            }
        }
    }

    public void displayRelevantImage(String randomCarMake, int randomImageIndex) {
        // get a random image reference
        switch (randomCarMake) {
            case "BMW":
                randomImageOfChosenCarMake = imagesOfBMW[randomImageIndex];
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
        Random r = new Random();
        return r.nextInt(6);
    }

    public int getRandomImage() {
        Random r = new Random();
        return r.nextInt(10);
    }

    public void submitCheck(View view) {
        resultMessage.setText("");
        getResult();            // follow steps to display result
    }

    void correct() {
        resultMessage.setTextColor(Color.parseColor("#008000"));
        resultMessage.setText("Correct!");
        buttonSubmitNext.setText("Next");
        if (toggleCountdown) {
            if (roundCountDownTimer != null) {          // if next is touched repeatedly, this will prevent the countdown timer messing up
                roundCountDownTimer.cancel();
            }
        }
    }

    void wrong() {
        resultMessage.setTextColor(Color.parseColor("#FF0000"));
        resultMessage.setText("Wrong!");
        correctAnswer.setText(randomCarMake);
        correctAnswer.setTextColor(Color.YELLOW);
        buttonSubmitNext.setText("Next");
        if (toggleCountdown) {         // only if the countdown toggle had been turned on
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();           // stopping the countdown running in the background
            }
        }
    }


    public void runTimer(long setTime) {

        roundCountDownTimerText = findViewById(R.id.timer_text);
        roundCountDownTimerText.setVisibility(View.VISIBLE);

        roundCountDownTimerProgress = findViewById(R.id.circular_progress_timer);        // circular progress bar for countdown
        roundCountDownTimerProgress.setProgress(100);

        final GradientDrawable mProgressCircle = (GradientDrawable) roundCountDownTimerProgress.getProgressDrawable();


        roundCountDownTimer = new CountDownTimer(setTime, 1000) {

            public void onTick(long millisUntilFinished) {
                remainingTime = (int) (1 + (millisUntilFinished / 1000));
                roundCountDownTimerText.setText(Integer.toString(remainingTime));
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

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                roundCountDownTimerProgress.setProgress(0);

                roundCountDownTimerText.setText(Integer.toString(0));
                getResult();

            }
        }.start();
    }
}

/*
------------------References used for this activity----------------------

1.https://stackoverflow.com/questions/4988143/simple-java-hangman-assignment
 */
