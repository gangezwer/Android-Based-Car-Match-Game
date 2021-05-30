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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdvanceLevel extends AppCompatActivity {

    public String[] allCarMakes = {"BMW", "Benz", "Ford", "Toyota", "Honda", "Audi"};

    // Arrays that reference to the car images categorized according to their makers.
    String[] imagesOfBMW = {"bmw1221", "bmw1222", "bmw1223", "bmw1224", "bmw1225","bmw1251","bmw1252","bmw1253","bmw1254","bmw1255"};
    String[] imagesOfBenz = {"bnz1226", "bnz1227", "bnz1228", "bnz1229", "bnz1230","bnz1276","bnz1277","bnz1278","bnz1279","bnz1280"};
    String[] imagesOfToyota = {"tyo1231", "tyo1232", "tyo1233", "tyo1234", "tyo1235","tyo1266","tyo1267","tyo1268","tyo1269","tyo1270"};
    String[] imagesOfHonda = {"hon1236", "hon1237", "hon1238", "hon1239", "hon1240","hon1271","hon1272","hon1273","hon1274","hon1275"};
    String[] imagesOfAudi = {"aui1241", "aui1242", "aui1243", "aui1244", "aui1245","aui1256","aui1257","aui1258","aui1259","aui1260"};
    String[] imagesOfFord = {"for1246", "for1247", "for1248", "for1249", "for1250","for1261","for1262","for1263","for1264","for1265"};

    List<String> allDisplayedImages = new ArrayList<>();
    private List<String> displayingCarMakes = new ArrayList<>();
    private List<Integer> displayingImageIndexes = new ArrayList<>();

    public String randomCarMake;
    public String randomImageOfChosenCarMake;
    private long countdownTime;
    private int remainingTime;
    private String imageOne, imageTwo, imageThree;
    private String answer1,answer2,answer3;

    private ImageView carImageFirst, carImageSecond, carImageThird;
    private Button mButtonSubNext;
    private int score;
    private int wrongCount = 0;
    private String questionCarMake;
    private TextView resultMessage;
    private boolean toggleCountdown;
    private CountDownTimer roundCountDownTimer;
    private TextView roundCountDownTimerText;
    private ProgressBar roundCountDownTimerProgress;
    private TextView mFinalScore;
    private EditText userAnswer1, userAnswer2, userAnswer3;
    private TextView mCorrectans1, mCorrectans2, mCorrectans3;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_level2);

        toggleCountdown = getIntent().getExtras().getBoolean("Countdown");         // getting the status of the switch in the main screen
        mFinalScore = findViewById(R.id.finalAdvanceScore);
        resultMessage = findViewById(R.id.final_results);
        mCorrectans1 = findViewById(R.id.correctmake_ans1);
        mCorrectans2 = findViewById(R.id.correctmake_ans2);
        mCorrectans3 = findViewById(R.id.correctmake_ans3);
        userAnswer1 = findViewById(R.id.user_ans1);
        userAnswer2 = findViewById(R.id.user_ans2);
        userAnswer3 = findViewById(R.id.user_ans3);
        mButtonSubNext = findViewById(R.id.submit_answer_button);         // Connecting Button to variable


        if (savedInstanceState != null) {

            countdownTime = savedInstanceState.getLong("time_left");
            displayingCarMakes = savedInstanceState.getStringArrayList("displaying_three");
            allDisplayedImages = savedInstanceState.getStringArrayList("all_displayed_images");
            displayingImageIndexes = savedInstanceState.getIntegerArrayList("all_displayed_image_indexes");
            score = savedInstanceState.getInt("points_earned");
            wrongCount = savedInstanceState.getInt("incorrect_guesses");
            
            CharSequence correctAns1 = savedInstanceState.getCharSequence("correct_answer_one");
            mCorrectans1.setText(correctAns1);

            CharSequence correctAns2 = savedInstanceState.getCharSequence("correct_answer_two");
            mCorrectans2.setText(correctAns2);

            CharSequence correctAns3 = savedInstanceState.getCharSequence("correct_answer_three");
            mCorrectans3.setText(correctAns3);

            CharSequence resultText = savedInstanceState.getCharSequence("result_text");
            resultMessage.setText(resultText);

            System.out.println(resultMessage.toString());
            
            String buttonText = savedInstanceState.getString("button_text");

            String userAnswerOne = savedInstanceState.getString("answer_for_image_one");
            userAnswer1.setText(userAnswerOne);

            String userAnswerTwo = savedInstanceState.getString("answer_for_image_one");
            userAnswer2.setText(userAnswerTwo);

            String userAnswerThree = savedInstanceState.getString("answer_for_image_one");
            userAnswer3.setText(userAnswerThree);


            if (toggleCountdown) {
                roundCountDownTimerText = findViewById(R.id.timer_text);
                roundCountDownTimerText.setVisibility(View.VISIBLE);

                remainingTime = (int) (countdownTime / 1000);
                roundCountDownTimerText.setText(Integer.toString(remainingTime));

                // Re applying circular progress countdown colours
                roundCountDownTimerProgress = findViewById(R.id.circular_progress_timer);
                roundCountDownTimerProgress.setProgress(100);            // resetting progress bar

                final GradientDrawable mProgressCircle = (GradientDrawable) roundCountDownTimerProgress.getProgressDrawable();

                if (remainingTime <= 5) {
                    mProgressCircle.setColor(Color.RED);
                } else if (remainingTime <= 10) {
                    mProgressCircle.setColor(Color.parseColor("#ffa000"));
                } else {
                    mProgressCircle.setColor(Color.parseColor("#42bf2d"));
                }

                if (resultText.toString().equals("CORRECT!")) {
                    resultMessage.setTextColor(Color.parseColor("#42bf2d"));
                    // show time that was left
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

            if (wrongCount >= 2) {
                if (!(userAnswerOne.equals(displayingCarMakes.get(0).toUpperCase()))) {
                    int questionBreedIndex = 0;

                    correctAns1 = displayingCarMakes.get(questionBreedIndex);

                    mCorrectans1.setText(correctAns1);
                    mCorrectans1.setTextColor(Color.YELLOW);
                }

                if (!(userAnswerTwo.equals(displayingCarMakes.get(2).toUpperCase()))) {
                    int questionBreedIndex = 2;

                    correctAns2 = displayingCarMakes.get(questionBreedIndex);

                    mCorrectans2.setText(correctAns2);
                    mCorrectans2.setTextColor(Color.YELLOW);
                }

                if (!(userAnswerThree.equals(displayingCarMakes.get(1).toUpperCase()))) {
                    int questionBreedIndex = 1;

                    correctAns3 = displayingCarMakes.get(questionBreedIndex);

                    mCorrectans3.setText(correctAns3);
                    mCorrectans3.setTextColor(Color.YELLOW);
                }
            }

                if (buttonText.equals("Next")) {
                    mButtonSubNext.setText("Next");
                    mFinalScore.setText(Integer.toString(score));
                    if (resultText.toString().equals("CORRECT!")) {
                        resultMessage.setTextColor(Color.parseColor("#42bf2d"));
                       /* userAnswer1.setTextColor(Color.parseColor("#42bf2d"));
                        userAnswer2.setTextColor(Color.parseColor("#42bf2d"));
                        userAnswer3.setTextColor(Color.parseColor("#42bf2d"));*/
                    } else if (resultText.toString().equals("WRONG!")) {
                        resultMessage.setTextColor(Color.RED);
                    }
                } else {
                    mButtonSubNext.setText("Submit");
                    if (resultText.toString().equals("CORRECT!")) {
                        resultMessage.setTextColor(Color.parseColor("#42bf2d"));
                    } else if (resultText.toString().equals("WRONG!")) {
                        resultMessage.setTextColor(Color.RED);
                    }
                    mFinalScore.setText(Integer.toString(score));
                }

            // Display images that were already shown
            carImageFirst = findViewById(R.id.first_car_image);
            carImageSecond = findViewById(R.id.third_car_image);
            carImageThird = findViewById(R.id.second_car_image);

            imageOne = displayRelevantImage(displayingCarMakes.get(0), displayingImageIndexes.get(0));
            imageTwo = displayRelevantImage(displayingCarMakes.get(1), displayingImageIndexes.get(1));
            imageThree = displayRelevantImage(displayingCarMakes.get(2), displayingImageIndexes.get(2));

            carImageFirst.setImageResource(getResources().getIdentifier(imageOne, "drawable", "com.example.car_match_game_app"));
            carImageFirst.setTag(displayingCarMakes.get(0));          // displayingBreeds ArrayList will have the Breed names in order of adding
            carImageSecond.setImageResource(getResources().getIdentifier(imageTwo, "drawable", "com.example.car_match_game_app"));
            carImageSecond.setTag(displayingCarMakes.get(1));
            carImageThird.setImageResource(getResources().getIdentifier(imageThree, "drawable", "com.example.car_match_game_app"));
            carImageThird.setTag(displayingCarMakes.get(2));

        } else {
            showImageSet();

            long SET_TIME = 20000;

            if (toggleCountdown) {
                runTimer(SET_TIME);

            } else {
                // proceed with the normal game flow, without the countdown timer
            }
        }

        //setting the default value for score as "0"
        mFinalScore.setText("0");

        //setting onclick event handle for button
        mButtonSubNext.setOnClickListener(view -> {
            if (mButtonSubNext.getText().equals("Submit")) {
                getResult();
            } else {
                resultMessage.setText("");
                mCorrectans1.setText("");
                mCorrectans2.setText("");
                mCorrectans3.setText("");
                wrongCount = 0;
                displayingCarMakes.clear();           // for a new set of  car make of images
                displayingImageIndexes.clear();           // for a new set of images
                userAnswer1.setText("");
                mFinalScore.setText("0");
                userAnswer2.setText("");
                userAnswer3.setText("");
                showImageSet();
                if (toggleCountdown) {
                    if (roundCountDownTimer != null) {
                        roundCountDownTimer.cancel();
                    }
                    // start the count down timer
                    runTimer(20000);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("time_left", countdownTime);          // time left in countdown timer
        outState.putStringArrayList("displaying_three", (ArrayList<String>) displayingCarMakes);
        outState.putStringArrayList("all_displayed_images", (ArrayList<String>) allDisplayedImages);
        outState.putIntegerArrayList("all_displayed_image_indexes", (ArrayList<Integer>) displayingImageIndexes);
        outState.putString("answer_for_image_one",userAnswer1.getText().toString());
        outState.putString("answer_for_image_two",userAnswer2.getText().toString());
        outState.putString("answer_for_image_three",userAnswer3.getText().toString());
        outState.putCharSequence("correctAns1",mCorrectans1.getText().toString());
        outState.putCharSequence("correctAns2",mCorrectans2.getText());
        outState.putCharSequence("correctAns3",mCorrectans3.getText());
        outState.putInt("incorrect_guesses",wrongCount);
        outState.putString("button_text", mButtonSubNext.getText().toString());     // to make sure that button text isn't reset to "Submit" all the time
        outState.putCharSequence("result_text", resultMessage.getText().toString());
        outState.putString("points_scored", mFinalScore.getText().toString());
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
        carImageFirst = findViewById(R.id.first_car_image);
        carImageSecond = findViewById(R.id.third_car_image);
        carImageThird = findViewById(R.id.second_car_image);

        // displayingBreeds ArrayList will have the car makes names in order of adding
        carImageFirst.setImageResource(displayRandomImage());
        carImageFirst.setTag(displayingCarMakes.get(0));
        carImageSecond.setImageResource(displayRandomImage());
        carImageSecond.setTag(displayingCarMakes.get(1));
        carImageThird.setImageResource(displayRandomImage());
        carImageThird.setTag(displayingCarMakes.get(2));

        resultMessage.setText("");
        userAnswer1.setText("");
        userAnswer2.setText("");
        userAnswer3.setText("");
        mCorrectans1.setText("");
        mCorrectans2.setText("");
        mButtonSubNext.setText("Submit");
        mCorrectans3.setText("");
        userAnswer1.setTextColor(Color.parseColor("black"));
        userAnswer2.setTextColor(Color.parseColor("black"));
        userAnswer3.setTextColor(Color.parseColor("black"));
    }

    public int displayRandomImage() {           // method used to display a random image of a random breed

        int randomImageIndex;

        // making sure that the same images aren't repeated & images of the same car make aren't shown together
        do {
            randomCarMake = allCarMakes[getRandomBreed()];
            randomImageIndex = getRandomImage();

            randomImageOfChosenCarMake = displayRelevantImage(randomCarMake, randomImageIndex);

        } while (displayingCarMakes.contains(randomCarMake) || allDisplayedImages.contains(randomImageOfChosenCarMake));

        // to make sure that the image isn't repeated
        allDisplayedImages.add(randomImageOfChosenCarMake);
        // to make sure that images of the same car make aren't shown at once
        displayingCarMakes.add(randomCarMake);
        // to recall indexes when the device is rotated
        displayingImageIndexes.add(randomImageIndex);

        // return chosen random image
        return getResources().getIdentifier(randomImageOfChosenCarMake, "drawable", "com.example.car_match_game_app");
    }

    public String displayRelevantImage(String randomCarMake, int randomImageIndex) {   // display a chosen image
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
        Random r = new Random();
        return r.nextInt(6);
    }

    public int getRandomImage() {
        Random r = new Random();
        return r.nextInt(10);
    }

    @SuppressLint("SetTextI18n")
    public void getResult() {
         score = 0;

        resultMessage.setText("");
        mFinalScore.setText("");
         answer1 = userAnswer1.getText().toString().toUpperCase();
         answer2 = userAnswer2.getText().toString().toUpperCase();
         answer3 = userAnswer3.getText().toString().toUpperCase();

        try {
            score = Integer.parseInt(mFinalScore.getText().toString());
        }catch (NumberFormatException hh){}
        //user is only allowed up to 3 incorrect character guesses
        if (wrongCount >= 2) {
            if (answer1.equals(displayingCarMakes.get(0).toUpperCase())) {
                userAnswer1.setTextColor(Color.parseColor("#008000"));
                score += 1;
                mFinalScore.setText(score + "");
            }

            if (answer2.equals(displayingCarMakes.get(2).toUpperCase())) {
                userAnswer2.setTextColor(Color.parseColor("#008000"));
                score += 1;
                mFinalScore.setText(score + "");
            }

            if (answer3.equals(displayingCarMakes.get(1).toUpperCase())) {
                userAnswer3.setTextColor(Color.parseColor("#008000"));
                score += 1;
                mFinalScore.setText(score + "");
            }

            if (!(answer1.equals(displayingCarMakes.get(0).toUpperCase())) && !(answer2.equals(displayingCarMakes.get(2).toUpperCase())) && !(answer3.equals(displayingCarMakes.get(1).toUpperCase()))){
                mFinalScore.setText("0");
            }

            if (!(answer1.equals(displayingCarMakes.get(0).toUpperCase()))) {
                int questionBreedIndex = 0;

                questionCarMake = displayingCarMakes.get(questionBreedIndex);

                mCorrectans1.setText(questionCarMake);
                mCorrectans1.setTextColor(Color.YELLOW);

            }

            if (!(answer2.equals(displayingCarMakes.get(2).toUpperCase()))) {
                int questionBreedIndex = 2;

                questionCarMake = displayingCarMakes.get(questionBreedIndex);

                mCorrectans2.setText(questionCarMake);
                mCorrectans2.setTextColor(Color.YELLOW);

            }

            if (!(answer3.equals(displayingCarMakes.get(1).toUpperCase()))) {
                int questionBreedIndex = 1;

                questionCarMake = displayingCarMakes.get(questionBreedIndex);

                mCorrectans3.setText(questionCarMake);
                mCorrectans3.setTextColor(Color.YELLOW);
            }

            if (answer1.equals(displayingCarMakes.get(0).toUpperCase()) && answer2.equals(displayingCarMakes.get(2).toUpperCase()) && answer3.equals(displayingCarMakes.get(1).toUpperCase())) {
                correct();
            }else {
                wrong();
            }
        } else {
            if (userAnswer1.length() == 0 && userAnswer2.length() == 0 && userAnswer3.length() == 0) {
                Toast.makeText(this, "You have to answer to validate !", Toast.LENGTH_LONG).show();
                userAnswer1.setText("");
                userAnswer2.setText("");
                userAnswer3.setText("");
            } else {
                //boolean for found the entered character in somewhere through country name
                //check whether the character is there

                if (answer1.equals(displayingCarMakes.get(0).toUpperCase())) {
                    userAnswer1.setTextColor(Color.parseColor("#008000"));
                    score += 1;
                    mFinalScore.setText(score + "");
                }

                if (!(answer1.equals(displayingCarMakes.get(0).toUpperCase()))) {
                    userAnswer1.setTextColor(Color.parseColor("#FF0000"));
                }


                if (answer2.equals(displayingCarMakes.get(2).toUpperCase())) {
                    userAnswer2.setTextColor(Color.parseColor("#008000"));
                    score += 1;
                    mFinalScore.setText(score + "");
                }

                if (!(answer2.equals(displayingCarMakes.get(2).toUpperCase()))) {
                    userAnswer2.setTextColor(Color.parseColor("#FF0000"));
                }

                if (answer3.equals(displayingCarMakes.get(1).toUpperCase())) {
                    userAnswer3.setTextColor(Color.parseColor("#008000"));
                    score += 1;
                    mFinalScore.setText(score + "");
                }

                if (!(answer3.equals(displayingCarMakes.get(1).toUpperCase()))) {
                    userAnswer3.setTextColor(Color.parseColor("#FF0000"));
                }
            }
            //if at least one character is there
            //if hyphens are not empty the word is complete and calling correct method
            if (answer1.equals(displayingCarMakes.get(0).toUpperCase()) && answer2.equals(displayingCarMakes.get(2).toUpperCase()) && answer3.equals(displayingCarMakes.get(1).toUpperCase())) {
                correct();
            } else {
                incorrectCheck();
                mFinalScore.setText(score + "");
            }
        }
    }

    public void incorrectCheck(){
        resultMessage.setText("Wrong!");
        resultMessage.setTextColor(Color.parseColor("#FF0000"));
        wrongCount +=1;

        if (toggleCountdown) {
            // if next is touched repeatedly, this will prevent the countdown timer messing up
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();
            }
            // start the count down timer
            runTimer(20000);
        }
    }

    public void submitCheck(View view) {
        resultMessage.setText("");
        getResult();            // follow steps to display result
    }

    void wrong() {
        resultMessage.setTextColor(Color.parseColor("#FF0000"));
        resultMessage.setText("Wrong!");
        mButtonSubNext.setText("Next");
        if (toggleCountdown) {
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();
            }
        }
    }

    void correct() {
        resultMessage.setTextColor(Color.parseColor("#008000"));
        resultMessage.setText("Correct!");
        mButtonSubNext.setText("Next");
        if (toggleCountdown) {
            if (roundCountDownTimer != null) {
                roundCountDownTimer.cancel();
            }
        }
    }

    public void runTimer(long setTime) {

        roundCountDownTimerText = findViewById(R.id.timer_text);
        roundCountDownTimerText.setVisibility(View.VISIBLE);

        roundCountDownTimerProgress = findViewById(R.id.circular_progress_timer);
        roundCountDownTimerProgress.setProgress(100);            // resetting progress bar

        final GradientDrawable mProgressCircle = (GradientDrawable) roundCountDownTimerProgress.getProgressDrawable();

        roundCountDownTimer = new CountDownTimer(setTime, 1000) {

            @SuppressLint("SetTextI18n")
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
//                        "Submit" will be shown only if Next was clicked
                roundCountDownTimerText.setText(Integer.toString(0));
                    getResult();            // follow steps to display result
                    //repeating should be done only when the "Next button is clicked"
            }
        }.start();
    }
}