package com.example.pomodoro_technique_app;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class RetrieveData {
    Context context;
    public RetrieveData(Context nContext){this.context = nContext;}
    //Time periods are in format MINUTES * MILLISECOND CONVERSION VALUE
    // 60000 for Minutes to ms, 1000 for seconds to ms
    int dataPoints = 3;
    int[] timePeriods = new int[dataPoints];
    private static final String TAG = "moizTag";

    public void readFileData() {
        AssetManager assetManager = context.getAssets();
        InputStream input;
        int size = -1;
        int readOutput = 0;
        try {
            input = assetManager.open("test_data.csv");
            while (readOutput != -1) {
                size++;
                readOutput = input.read();
            }
            input.close();

            input = assetManager.open("test_data.csv");
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();
            String text = new String(buffer);

//            Log.d(TAG, text);

            int j = 0;
            String[] timeData = text.split("[,\\n]");
            String readLine;
//            Log.d(TAG, "Starting For Loop");
            for (int i = 0; i < timeData.length; i++) {
//                Log.d(TAG,"data: " + timeData[i]);
                readLine = timeData[i].replaceAll("[^0-9]+","").trim();
                if (!readLine.isEmpty()) {
//                    Log.d(TAG, "Starting data conversion of "+ readLine);
                    try {
                        timePeriods[j] = Integer.parseInt(readLine);
                    }catch (Exception e) {
                        Log.w(TAG,"Read Data error: " + e.getMessage());
                    }
                    j++;
//                    Log.d(TAG, "Data Converted");
                }
            }
            Log.d(TAG, "Array timePeriods[] filled with ints");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}