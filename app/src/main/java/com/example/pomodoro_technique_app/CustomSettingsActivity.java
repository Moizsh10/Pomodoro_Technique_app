package com.example.pomodoro_technique_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CustomSettingsActivity extends AppCompatActivity {
    Context settingsContext = CustomSettingsActivity.this;
    Activity settingsActivity = CustomSettingsActivity.this;
//    Intent i = getIntent();
//    int dataPoints = i.getIntExtra("DATA", 3);
    RetrieveData retriever = new RetrieveData(settingsContext,3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_settings);
    }
    public void setCustomTimes(View v) {
        EditText workText = findViewById(R.id.editTextWork);
        EditText shortText = findViewById(R.id.editTextShort);
        EditText longText = findViewById(R.id.editTextLong);

        String workTime = workText.getText().toString();
        String shortTime = shortText.getText().toString();
        String longTime = longText.getText().toString();

        String[] data = {workTime, shortTime, longTime};
        retriever.writeFileData("custom_time_data.csv", data);


    }
    public void activityChangeMain(View v) {
        Intent i = new Intent(settingsContext, MainActivity.class);
        startActivity(i);

    }
}