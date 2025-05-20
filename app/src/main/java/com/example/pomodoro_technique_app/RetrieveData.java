package com.example.pomodoro_technique_app;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;

public class RetrieveData {
    private int workPeriod;
    private int shortBreakPeriod;
    private int longBreakPeriod;
    private static final String TAG = "moizTag";
    Context context;
    public RetrieveData(Context nContext){
        this.context = nContext;
    }
    public void readFileData() {
        AssetManager assetManager = context.getAssets();
        InputStream input;
        int size = -1;
        int readOutput = 0;
        try {
            input = assetManager.open("time_data.csv");
            while (readOutput != -1) {
                size++;
                readOutput = input.read();
            }
            input.close();

            input = assetManager.open("time_data.csv");
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            String text = new String(buffer);

            Log.d(TAG, "total number of chars is " + size);
            Log.d(TAG, text);

            String[] timeData = text.split(",");
            for (int i = 0; i < timeData.length; i++) {
                Log.d(TAG, timeData[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
