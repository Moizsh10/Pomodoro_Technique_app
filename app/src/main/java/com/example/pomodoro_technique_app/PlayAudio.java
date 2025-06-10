package com.example.pomodoro_technique_app;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PlayAudio {

    private static final String TAG = "moizTag";
    Context context;
    public PlayAudio(Context nContext){this.context = nContext;}

    AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build();

    SoundPool alarm = new SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build();
    int soundID = alarm.load(context, R.raw.twin_bell_alarm_short, 1);

    public void playSound() {
        alarm.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                alarm.play(soundID, 1, 1, 1, 0, 1);
            }
        });
    }

}

