package com.example.pomodoro_technique_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;
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
    Context mainContext = MainActivity.this;
    Activity mainActivity = MainActivity.this;
    StateMachine state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state = new StateMachine(mainContext, mainActivity);
        state.initData(mainContext);
    }

    public void startTimer(View v) {
        try {
            findViewById(R.id.pauseButton).setEnabled(true);
            findViewById(R.id.resetButton).setEnabled(true);
            state.start(v);
        } catch (Exception e) {
            Log.d(TAG, "Main Activity start error: " + e.getMessage());
        }
    }

    public void pauseTimer(View v) {
        try {
            state.pause(v);
        } catch (Exception e) {
            Log.d(TAG, "Main Activity pause error: " + e.getMessage());
        }
    }

    public void resetTimer(View v) {
        try {
            state.reset(v);
        } catch (Exception e) {
            Log.d(TAG, "Main Activity reset error: " + e.getMessage());
        }
    }
}


