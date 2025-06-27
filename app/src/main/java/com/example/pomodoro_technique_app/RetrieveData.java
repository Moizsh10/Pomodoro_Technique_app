package com.example.pomodoro_technique_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class RetrieveData {
    //Time periods are in format MINUTES/SECONDS * MILLISECOND CONVERSION VALUE
    // 60000 for Minutes to ms, 1000 for seconds to ms
    Context context;
    int dataPoints;
    public RetrieveData(Context nContext, int dataPoints){
        this.context = nContext;
        this.dataPoints = dataPoints;
    }

    private static final String TAG = "moizTag";

    public void readFileData(int[] timePeriods) {

        int size = -1;
        int readOutput = 0;
        try {
            InputStream input = context.getResources().openRawResource(R.raw.test_data1);
            while (readOutput != -1) {
                size++;
                readOutput = input.read();
            }
            input.close();

            input = context.getResources().openRawResource(R.raw.test_data1);
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
    public void readFileDataOG(int[] timePeriods, String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream input;
        int size = -1;
        int readOutput = 0;
        try {
            input = assetManager.open(fileName);
            while (readOutput != -1) {
                size++;
                readOutput = input.read();
            }
            input.close();

            input = assetManager.open(fileName);
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
    public void writeFileData(String fileName, String[] data) {
       try {
           Writer out = new BufferedWriter(new FileWriter(fileName));
           for(int i = 0; i < dataPoints; i++)
           {
               out.write(data[i] + ",\n");
           }
           out.close();

       }
       catch (Exception e) {
           Log.d(TAG,"Write data error: " + e);
       }
    }
}