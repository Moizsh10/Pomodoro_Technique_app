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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    StateMachine state = new StateMachine(mainContext, mainActivity);

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

    public void testMethod(View v) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        SoundPool alarm = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        int soundID = alarm.load(this, R.raw.twin_bell_alarm_short, 1);

        alarm.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                playAudio(alarm,soundID);
            }
        });

    }

    public void playAudio(SoundPool sp, int soundID) {
        sp.play(soundID, 1, 1, 1, 0, 1);
    }
}


