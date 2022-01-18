package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChecklistActivity extends AppCompatActivity {
    private long maxCountdownTime;
    private long maxLueftungsTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_checklist);

        // Get extras
        maxCountdownTime = getIntent().getLongExtra("maxCountdownTime", 0);
        maxLueftungsTime = getIntent().getLongExtra("maxLueftungsTimer", 0);

        super.onCreate(savedInstanceState);
    }

    // open the new activity
    public void openCountdownActivity(View view) {
        Intent intent = new Intent(this, CountdownActivity.class);

        // send to next activity
        intent.putExtra("maxCountdownTime", maxCountdownTime);
        intent.putExtra("maxLueftungsTimer", maxLueftungsTime);

        // start next activity
        startActivity(intent);
    }
}