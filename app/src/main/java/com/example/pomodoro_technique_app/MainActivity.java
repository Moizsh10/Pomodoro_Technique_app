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

    //[0] will be for work period, [1] will be for short break period, [2] will be for long break period for the following 3 arrays
    private int[] timerInvervals = new int[dataPoints];
    final int[] trackSessions = {4, 4, 1};
    private int[] numSessions = new int[3];
    private int whichIndex;
    private boolean workSession = true;
    private boolean shortBreak = false;

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
        whichIndex = sessionTracker();
        Log.d(TAG,"Index: "+ whichIndex +" chosen");

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausedTimeVal = pauseTimer(timer);
                timerPaused = true;

                Log.d(TAG, "Paused at Minutes: " + pausedTimeVal[0] + " Seconds: " + pausedTimeVal[1]);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer(timer);
                timerReset = true;
            }
        });
        // check if data from files has been read in
        if(timerInvervals[0] == 0){
            timerInvervals = readFile();
            Log.d(TAG, "Data imported for first time");
        }

        if(timerPaused && !timerReset){
            //get minutes & seconds then convert to milliseconds
            int minutes = Integer.parseInt(pausedTimeVal[0]) * 60000;
            int seconds = Integer.parseInt(pausedTimeVal[1]) * 1000; // Multiply by 1000 to convert seconds to milliseconds
            postPauseTime = minutes+seconds;
            startTimer(timer,postPauseTime);
            timerPaused = false;
            Log.d(TAG, "Starting from a pause");
        }
        else {
            startTimer(timer, timerInvervals[whichIndex]);
            Log.d(TAG, "Starting from time data array");
        }
    }

    public void startTimer(Timer timer, int timeLen) {

        Log.d(TAG,"Start Button clicked");
        // Disable start button
        Button startButton = findViewById(R.id.startButton);
        startButton.setEnabled(false);

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
                   try {
                       displayTime.setText("Timer Finished!");
                       timer.cancel();
                       runOnUiThread(new Runnable() {

                           @Override
                           public void run() {

                               startButton.setEnabled(true);

                           }
                       });

                       Log.d(TAG, "Timer Finished");
                   } catch (Exception e) {
                       Log.d(TAG,e.getMessage());
                   }
               }
           }
       };
       timer.schedule(task, 0, 1000); // task starts immediately, with pause of 1 second between run cycles
        timerReset = false;

    }

    public String[] pauseTimer(Timer timer) {
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
    public void resetTimer(Timer timer) {
        Log.d(TAG,"Reset Button clicked");
        timer.cancel();

        TextView displayTime = findViewById(R.id.timerView);
        displayTime.setText("00:00");

        Button startButton = findViewById(R.id.startButton);
        startButton.setEnabled(true);
    }

    public int[] readFile(){
        RetrieveData file = new RetrieveData(MainActivity.this);
        file.readFileData();
        int[] fileData = file.timePeriods;
        return fileData;
    }

    public int sessionTracker() {
        int index = 0;
        TextView sessionIndicator = findViewById(R.id.sessionView);
        if (numSessions[0] == trackSessions[0] && numSessions[1] == trackSessions[1] && numSessions[2] == trackSessions[2]) {
            workSession = true;
            shortBreak = false;
            for (int i = 0; i < 3; i++) {
                numSessions[i] = 0;
            }
            // immediately start a work session again, set index equal to one so the next run is a short break
            numSessions[0] = 1;
            sessionIndicator.setText("Work Session" + numSessions[0]);
            Log.d(TAG,"Reset Session");

            index = 1;

        } else if(numSessions[0] == trackSessions[0] && numSessions[1] == trackSessions[1]){
            workSession = true;
            shortBreak = false;
            numSessions[2]++;
            index = 2;
            sessionIndicator.setText("Long Break");
            Log.d(TAG,"Begin long break");

        } else if (workSession && numSessions[0] < trackSessions[0]) {
            workSession = false;
            shortBreak = true;
            numSessions[0]++;
            index = 0;
            sessionIndicator.setText("Work Session " + numSessions[0]);
            Log.d(TAG,"Begin work session");

        } else if (shortBreak && numSessions[1] < trackSessions[1]){
            workSession = true;
            shortBreak = false;
            numSessions[1]++;
            index = 1;
            sessionIndicator.setText("Short Break " + numSessions[1]);
            Log.d(TAG,"Begin short break");
        }

        return index;
    }
}


