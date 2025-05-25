package com.example.pomodoro_technique_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Time periods are in format MINUTES * MILLISECOND CONVERSION VALUE;
//    private int workPeriod = 25 * 60000;
//    private int breakPeriodShort = 10 * 60000;
//    private int breakPeriodLong = 30 * 60000;
    final int dataPoints = 3;
    private int[] timerInvervals = new int[dataPoints];
    private int postPauseTime = 0;
    private String[] pausedTimeVal;
    private boolean timerPaused = false;
    private boolean timerReset = false;
    private static final String TAG = "moizTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void stateMachine(View v) {
        Timer timer = new Timer();
        Button pauseButton = findViewById(R.id.pauseButton);
        Button resetButton = findViewById(R.id.resetButton);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedTimeVal = pauseTimer(v, timer);
                timerPaused = true;
                Log.d(TAG, "Minutes: " + pausedTimeVal[0] + " Seconds: " + pausedTimeVal[1]);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(v, timer);
                timerReset = true;
            }
        });
        // check if data from files has been read in
        if(timerInvervals[0] == 0){
            timerInvervals = readFile(v);
            Log.d(TAG, "Data imported for first time");
        }

        if(timerPaused && !timerReset){
            //get minutes & seconds then convert to milliseconds
            int minutes = Integer.parseInt(pausedTimeVal[0]) * 60000;
            int seconds = Integer.parseInt(pausedTimeVal[1]) * 1000; // Multiply by 1000 to convert seconds to milliseconds
            postPauseTime = minutes+seconds;
            startTimer(v,timer,postPauseTime);
            timerPaused = false;
        }
        else {
            startTimer(v, timer, timerInvervals[0]);
        }
    }

    public void startTimer(View start, Timer timer, int timeLen) {

        Log.d(TAG,"Start Button clicked");
        // Disable start button
        Button startButton = findViewById(R.id.startButton);
        startButton.setEnabled(false);
//        startButton.setVisibility(View.INVISIBLE);

        TextView displayTime = findViewById(R.id.timerView); // Grab the TextView being used to display time in the app


       TimerTask task = new TimerTask() {
           int count = timeLen; //timeLen is already in milliseconds according to stateMachine method
           int minutes = 0;
           int seconds = 0;

           @Override
           public void run() {
               minutes = count / 60000; // convert milliseconds back into minutes
               seconds = (count % 60000) / 1000; // convert milliseconds into seconds

               if (minutes < 10 && seconds < 10) { // appropriately display time based on if value is in Ones or in Tens
                   displayTime.setText(String.format(Locale.US, "0%d:0%d", minutes, seconds));
               } else if (minutes < 10 && seconds >= 10) {
                   displayTime.setText(String.format(Locale.US, "0%d:%d", minutes, seconds));
               } else if (minutes >= 10 && seconds < 10) {
                   displayTime.setText(String.format(Locale.US, "%d:0%d", minutes, seconds));
               } else {
                   displayTime.setText(String.format(Locale.US, "%d:%d", minutes, seconds));
               }

               count = count - 1000; // reduce time by one second
               if (count < 0) {
                   displayTime.setText("Timer Finished!");
                   timer.cancel();
               }
           }
       };
       timer.schedule(task, 0, 1000); // task starts immediately, with pause of 1 second between run cycles
        Log.d(TAG,"After timer log sent");
        timerReset = false;

    }

    public String[] pauseTimer(View pause, Timer timer) {
        String[] timeVals;

        Log.d(TAG,"Pause Button clicked");
        timer.cancel();

        Button startButton = findViewById(R.id.startButton);
        startButton.setEnabled(true);

        TextView displayTime = findViewById(R.id.timerView);
        String currentTime = displayTime.getText().toString();
        displayTime.setText(currentTime);

        timeVals = currentTime.split(":");

        return timeVals;
    }
    public void resetTimer(View reset, Timer timer) {
        Log.d(TAG,"Reset Button clicked");
        timer.cancel();

        TextView displayTime = findViewById(R.id.timerView);
        displayTime.setText("00:00");

        Button startButton = findViewById(R.id.startButton);
        startButton.setEnabled(true);
    }

    public int[] readFile(View view){
        RetrieveData file = new RetrieveData(MainActivity.this);
        file.readFileData();
        int[] fileData = file.timePeriods;
        return fileData;
    }
}


