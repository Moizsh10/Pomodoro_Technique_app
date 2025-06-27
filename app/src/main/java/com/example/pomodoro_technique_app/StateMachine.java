package com.example.pomodoro_technique_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StateMachine{
    Context context;
    Activity activity;
    final int dataPoints = 3;

    public StateMachine(Context nContext, Activity nActivity) {
        this.context = nContext;
        this.activity = nActivity;
    }

    //[0] will be for work period, [1] will be for short break period, [2] will be for long break period for the following 3 arrays
    private int[] timerInvervals = new int[dataPoints];
    final int[] trackSessions = {4, 4, 1};
    private int[] numSessions = new int[3];
    //***************************************************
    //Time periods are in format MINUTES * MILLISECOND CONVERSION VALUE;
//    private int workPeriod = 25 * 60000;
//    private int breakPeriodShort = 10 * 60000;
//    private int breakPeriodLong = 30 * 60000;

    private int whichIndex;
    private boolean shortBreak = false;
    private boolean workSession = true;
    private int postPauseTime = 0;
    private String[] pausedTimeVal;
    private boolean timerPaused = false;
    private boolean timerReset = false;
    private Timer currentTimer;
    PlayAudio alarm;
    private static final String TAG = "moizTag";
    public void machine(int type) {
        Timer timer = new Timer();
        if(!timerPaused && !timerReset){
            whichIndex = sessionTracker();
        }

        Log.d(TAG,"Index: "+ whichIndex +" chosen");

        switch(type) {
            case 1:
                if(timerPaused && !timerReset){
                    //get minutes & seconds then convert to milliseconds
                    int minutes = Integer.parseInt(pausedTimeVal[0]) * 60000;
                    int seconds = Integer.parseInt(pausedTimeVal[1]) * 1000; // Multiply by 1000 to convert seconds to milliseconds
                    postPauseTime = minutes+seconds;
                    startTimer(timer,postPauseTime);
                    Log.d(TAG, "Starting from a pause");
                } else {
                    try {
                    startTimer(timer, timerInvervals[whichIndex]);
                    Log.d(TAG, "Starting from time data array");
                    } catch (Exception e) {
                        Log.d(TAG,"Start timer method: "+e.getMessage());
                    }
                }
                break;

            case 2:
                pausedTimeVal = pauseTimer(currentTimer);
                timer.cancel(); // get rid of unnecessary newly created timer
                Log.d(TAG, "Paused at Minutes: " + pausedTimeVal[0] + " Seconds: " + pausedTimeVal[1]);
                break;

            case 3:
                resetTimer(currentTimer);
                timer.cancel(); // get rid of unnecessary newly created timer
                break;

            default:
        }
    }

    public void start(View start){
        try {
            machine(1);
        } catch (Exception e) {
            Log.d(TAG,"State Machine start: "+e.getMessage());
            e.printStackTrace();
        }
}

    public void pause(View pause){
        try {
            timerPaused = true;
            machine(2);
        } catch (Exception e) {
            Log.d(TAG,"State Machine pause: "+e.getMessage());
        }
    }

    public void reset(View reset){
        try {
            timerReset = true;
            machine(3);
        } catch (Exception e) {
            Log.d(TAG,"State Machine reset: "+ e.getMessage());
        }
    }

    public void startTimer(Timer timer, int timeLen) {
        currentTimer = timer;
        timerPaused = false;
        timerReset = false;

        Log.d(TAG,"Start Button clicked");
        // Disable start button
        Button startButton = activity.findViewById(R.id.startButton);
        startButton.setEnabled(false);

        TextView displayTime = activity.findViewById(R.id.timerView); // Grab the TextView being used to display time in the app


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
                        alarm.playSound();
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                startButton.setEnabled(true);

                            }
                        });

                        Log.d(TAG, "Timer Finished");
                    } catch (Exception e) {
                        Log.d(TAG,"Timer Finish error: "+ e.getMessage());
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

        Button startButton = activity.findViewById(R.id.startButton);
        startButton.setEnabled(true);

        TextView displayTime = activity.findViewById(R.id.timerView);
        String currentTime = displayTime.getText().toString();
        displayTime.setText(currentTime);

        timeVals = currentTime.split(":");

        return timeVals;
    }
    public void resetTimer(Timer timer) {
        Log.d(TAG,"Reset Button clicked");
        timer.cancel();

        TextView displayTime = activity.findViewById(R.id.timerView);
        displayTime.setText("00:00");

        Button startButton = activity.findViewById(R.id.startButton);
        startButton.setEnabled(true);
    }

    //Initialize data like file info and set up audio files
    public void initData(Context aContext){
        RetrieveData file = new RetrieveData(aContext, dataPoints);
        file.readFileData(timerInvervals);
        alarm = new PlayAudio(aContext);
        alarm.loadSound();
    }

    public int sessionTracker() {
        int index = 0;
        TextView sessionIndicator = activity.findViewById(R.id.sessionView);
        if (numSessions[0] == trackSessions[0] && numSessions[1] == trackSessions[1] && numSessions[2] == trackSessions[2]) {
            workSession = false;
            shortBreak = true;
            for (int i = 0; i < 3; i++) {
                numSessions[i] = 0;
            }
            // immediately start a work session again, set index equal to 0, and make shortBreak true so the next run is a short break
            numSessions[0] = 1;
            sessionIndicator.setText("Work Session " + numSessions[0]);
            Log.d(TAG,"Resetting the loop, starting Work Session");

            index = 0;

        } else if(numSessions[0] == trackSessions[0] && numSessions[1] == trackSessions[1]){
            workSession = false;
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
