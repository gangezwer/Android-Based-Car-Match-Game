package com.example.car_match_game_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IdentifyTheCarImage extends AppCompatActivity {

    public String[] allCarMakes = {"BMW", "Benz", "Ford", "Toyota", "Honda","Audi"};

    // Arrays that reference to the car images categorized according to their makers.
    String[] imagesOfBMW = {"bmw1221", "bmw1222", "bmw1223", "bmw1224", "bmw1225","bmw1251","bmw1252","bmw1253","bmw1254","bmw1255"};
    String[] imagesOfBenz = {"bnz1226", "bnz1227", "bnz1228", "bnz1229", "bnz1230","bnz1276","bnz1277","bnz1278","bnz1279","bnz1280"};
    String[] imagesOfToyota = {"tyo1231", "tyo1232", "tyo1233", "tyo1234", "tyo1235","tyo1266","tyo1267","tyo1268","tyo1269","tyo1270"};
    String[] imagesOfHonda = {"hon1236", "hon1237", "hon1238", "hon1239", "hon1240","hon1271","hon1272","hon1273","hon1274","hon1275"};
    String[] imagesOfAudi = {"aui1241", "aui1242", "aui1243", "aui1244", "aui1245","aui1256","aui1257","aui1258","aui1259","aui1260"};
    String[] imagesOfFord = {"for1246", "for1247", "for1248", "for1249", "for1250","for1261","for1262","for1263","for1264","for1265"};


    List<String> allDisplayedImages = new ArrayList<>();        // all the displayed images are added here. To make sure that images aren't repeated
    private List<String> displayingCarMakes = new ArrayList<>();        // the 3 displayed car makes will be here. To make sure that multiple images of the same breed won't be shown together
    private List<Integer> displayingImageIndexes = new ArrayList<>();        // the 3 displayed image indexes will be here. To reload the images when the device is rotated.

    public String randomCarMake;
    public String randomImageOfChosenCarMake;
    // car make that is displayed as the question
    private String questionCarMake;
    //to makesure that user can't pick up images multiple times
    private boolean userPickedCarImage;
    private long countdownTime;
    private int remainingTime;

    private TextView carMakeNameTitle;
    private ImageView userChoosenImage;
    private TextView resultMessage;
    private boolean toggleCountdown;
    private CountDownTimer roundCountDownTimer;
    private TextView roundCountDownTimerText;
    private ProgressBar roundCountDownTimerProgress;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_identify_the_car_image);
        carMakeNameTitle = /*(TextView)*/ findViewById(R.id.car_make_name_label);         // Connecting TextView to variable
        resultMessage = /*(TextView)*/ findViewById(R.id.result_text);         // Connecting TextView to variable

        toggleCountdown = getIntent().getExtras().getBoolean("Countdown");         // getting the status of the switch in the main screen


        // restore the state
        if (savedInstanceState != null) {

            countdownTime = savedInstanceState.getLong("time_left");
            questionCarMake = savedInstanceState.getString("question_breed");
            displayingCarMakes = savedInstanceState.getStringArrayList("displaying_three");
            allDisplayedImages = savedInstanceState.getStringArrayList("all_displayed_images");
            displayingImageIndexes = savedInstanceState.getIntegerArrayList("all_displayed_image_indexes");

            CharSequence resultText = savedInstanceState.getCharSequence("result_text");

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
                } else {
                    if (toggleCountdown) {
//                // run the timer only if a result isn't displayed already
                        runTimer(countdownTime);
                    }
                }
            }

            resultMessage.setText(resultText);
            carMakeNameTitle.setText(questionCarMake);

            // Display images that were already shown ------------------
            ImageView firstCarImage = findViewById(R.id.first_car_image);
            ImageView secondCarImage = findViewById(R.id.third_car_image);
            ImageView thirdCarImage = findViewById(R.id.second_car_image);

            String imageOne = displayRelevantImage(displayingCarMakes.get(0), displayingImageIndexes.get(0));
            String imageTwo = displayRelevantImage(displayingCarMakes.get(1), displayingImageIndexes.get(1));
            String imageThree = displayRelevantImage(displayingCarMakes.get(2), displayingImageIndexes.get(2));

            firstCarImage.setImageResource(getResources().getIdentifier(imageOne, "drawable", "com.example.car_match_game_app"));
            firstCarImage.setTag(displayingCarMakes.get(0));          // displayingBreeds ArrayList will have the Breed names in order of adding
            secondCarImage.setImageResource(getResources().getIdentifier(imageTwo, "drawable", "com.example.car_match_game_app"));
            secondCarImage.setTag(displayingCarMakes.get(1));
            thirdCarImage.setImageResource(getResources().getIdentifier(imageThree, "drawable", "com.example.car_match_game_app"));
            thirdCarImage.setTag(displayingCarMakes.get(2));

        } else {

            showImageSet();         // display initial set of images

            // check if the countdown timer is on and run the countdown timer here, else follow the normal method
            long SET_TIME = 20000;

            if (toggleCountdown) {
                runTimer(SET_TIME);

            } else {
                // proceed with the normal game flow, without the countdown timer
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("time_left", countdownTime);          // time left in countdown timer
        outState.putString("question_breed", questionCarMake);
        outState.putStringArrayList("displaying_three", (ArrayList<String>) displayingCarMakes);
        outState.putStringArrayList("all_displayed_images", (ArrayList<String>) allDisplayedImages);
        outState.putIntegerArrayList("all_displayed_image_indexes", (ArrayList<Integer>) displayingImageIndexes);
        outState.putCharSequence("result_text", resultMessage.getText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toggleCountdown) {
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();
            }
        }
    }

    public void showImageSet() {
        ImageView firstCarImage = findViewById(R.id.first_car_image);
        ImageView secondCarImage = findViewById(R.id.third_car_image);
        ImageView thirdCarImage = findViewById(R.id.second_car_image);

        firstCarImage.setImageResource(displayRandomImage());
        firstCarImage.setTag(displayingCarMakes.get(0));          // displayingBreeds ArrayList will have the Breed names in order of adding
        secondCarImage.setImageResource(displayRandomImage());
        secondCarImage.setTag(displayingCarMakes.get(1));
        thirdCarImage.setImageResource(displayRandomImage());
        thirdCarImage.setTag(displayingCarMakes.get(2));

        resultMessage.setText("");
        // display car make to be identified
        chooseBreedToBeIdentified();
    }


    public int displayRandomImage() {           // method used to display a random image of a random breed

        int randomImageIndex;

        do
        {            // making sure that the same images aren't repeated & images of the same car makes aren't shown together

            randomCarMake = allCarMakes[getRandomBreed()];           // get a random breed
            randomImageIndex = getRandomImage();                // get a random image of a particular breed

            randomImageOfChosenCarMake = displayRelevantImage(randomCarMake, randomImageIndex);

        } while (displayingCarMakes.contains(randomCarMake) || allDisplayedImages.contains(randomImageOfChosenCarMake));

        allDisplayedImages.add(randomImageOfChosenCarMake);           // to make sure that the image isn't repeated
        displayingCarMakes.add(randomCarMake);                          // to make sure that images of the same breed aren't shown at once
        displayingImageIndexes.add(randomImageIndex);               // to recall indexes when the device is rotated

        // return chosen random image
        return getResources().getIdentifier(randomImageOfChosenCarMake, "drawable", "com.example.car_match_game_app");
    }

    public String displayRelevantImage(String randomCarMake, int randomImageIndex) {         // display a chosen image
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
        // return chosen random image
        return randomImageOfChosenCarMake;
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

    private void chooseBreedToBeIdentified() {           // used to pick the breed to be identified
        Random r = new Random();
        int questionBreedIndex = r.nextInt(3);

        questionCarMake = displayingCarMakes.get(questionBreedIndex);
        carMakeNameTitle.setText(questionCarMake);
    }

    //when "Next" button is clicked show new set of images and makes
    public void showNextImageSet(View view) {       // when "Next" button is clicked, shows new set of images
        displayingCarMakes.clear();
        displayingImageIndexes.clear();

        // resetting the car image for picking an image again
        userPickedCarImage = false;

        // if next is touched repeatedly, this will prevent the countdown timer messing up
        if (toggleCountdown) {
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();
            }
            runTimer(20000);
        }
        showImageSet();
    }

    public void checkAnswerFirst(View view) {            // when the first image is clicked, checks if the answer is correct
        userChoosenImage = findViewById(R.id.first_car_image);

        if (!resultMessage.getText().equals("Time's up!")) {            // making sure that the user can't choose an image after the timer is over
            displayResult();
        }
    }

    public void checkAnswerSecond(View view) {
        userChoosenImage = findViewById(R.id.third_car_image);

        if (!resultMessage.getText().equals("Time's up!")) {
            displayResult();
        }
    }

    public void checkAnswerThird(View view) {
        userChoosenImage = findViewById(R.id.second_car_image);

        if (!resultMessage.getText().equals("Time's up!")) {
            displayResult();
        }
    }

    public void displayResult() {

        try {
            System.out.println(userChoosenImage.getTag());          // To check whether the chosen image gave the correct tag

            if (!userPickedCarImage) {         // allowing the user to take only one attempt
                userPickedCarImage = true;
                if (userChoosenImage.getTag().equals(questionCarMake)) {        // If the displayed breed was picked properly
                    resultMessage.setText("CORRECT!");
                    resultMessage.setTextColor(Color.parseColor("#42bf2d"));

                } else {
                    resultMessage.setText("WRONG!");
                    resultMessage.setTextColor(Color.RED);
                }
            }
            userChoosenImage = null;        // need for countdown game -> otherwise previous image selected position will be taken

            // can't do with set tag as it's done when the images are loaded
        } catch (Exception e) {          // will come here, if no image was chosen -> needed for the countdown game
            resultMessage.setText("Time's up!");
            resultMessage.setTextColor(Color.YELLOW);
        }

        // reset the countdown timer, for new image, if "Submit" was clicked before the countdown ended
        if (toggleCountdown) {
            roundCountDownTimer.cancel();
        }
    }

    public void runTimer(long setTime) {
        roundCountDownTimerText = findViewById(R.id.timer_text);
        roundCountDownTimerText.setVisibility(View.VISIBLE);

        roundCountDownTimerProgress = findViewById(R.id.circular_progress_timer);        // circular progress bar for countdown
        roundCountDownTimerProgress.setProgress(100);            // resetting progress bar

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

            public void onFinish() {
                roundCountDownTimerProgress.setProgress(0);

                roundCountDownTimerText.setText(Integer.toString(0));
                displayResult();            // follow steps to display result

                //repeating should be done only when the "Next button is clicked"
            }
        }.start();
    }
}

/*
------------------References used for this activity----------------------

https://stackoverflow.com/questions/6449654/how-to-get-image-resource-name/14028623

https://stackoverflow.com/questions/15213974/add-multiple-items-to-already-initialized-arraylist-in-java

https://stackoverflow.com/questions/6751564/how-to-pass-a-boolean-between-intents

Dynamically change colour of drawable
https://www.codota.com/code/java/methods/android.graphics.drawable.GradientDrawable/setColor

 */