package com.example.pomodoro_technique_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "moizTag";
    Context mainContext = this;
    Activity mainActivity = (Activity) mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    StateMachine state = new StateMachine(mainContext, mainActivity);

    public void startTimer(View v){
        try {
            state.start(v);
            Log.d(TAG,"clicked start");
        } catch (Exception e) {
        Log.d(TAG,"Main Activity error: "+ e.getMessage());
        }
    }

    public void pauseTimer(View v){
        try {
            state.pause(v);
            Log.d(TAG,"clicked pause");
        } catch (Exception e) {
        Log.d(TAG,"Main Activity error: "+ e.getMessage());
        }
    }

    public void resetTimer(View v){
        try {
            state.reset(v);
            Log.d(TAG,"clicked reset");
        } catch (Exception e) {
            Log.d(TAG,"Main Activity error: "+ e.getMessage());
        }
    }


}


