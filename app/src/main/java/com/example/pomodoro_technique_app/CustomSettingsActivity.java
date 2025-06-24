package com.example.pomodoro_technique_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CustomSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_settings);
    }

    public void activityChangeMain(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }
}