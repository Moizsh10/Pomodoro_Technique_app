package com.example.pomodoro_technique_app;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import java.io.IOException;

public class PlayAudio {
    Context context;
    public PlayAudio(Context nContext){this.context = nContext;}
    private static final String TAG = "moizTag";
    AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build();

    SoundPool alarm = new SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build();
    int soundID;
    public void loadSound(){
        AssetManager am = context.getAssets();
        AssetFileDescriptor soundName;

        try {
            soundName = am.openFd("twin_bell_alarm_short.mp3");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        soundID = alarm.load(soundName, 1);
    }

    public void playSound() {
//        alarm.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                alarm.play(soundID, 1, 1, 1, 0, 1);
//            }
//        });
        alarm.play(soundID, 1, 1, 1, 0, 1);
    }

}

