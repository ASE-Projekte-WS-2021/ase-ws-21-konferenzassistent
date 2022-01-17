package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CountdownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
    }

    /* finishMeeting method code by Kimmi Dhingra:
    * https://stackoverflow.com/questions/18442328/how-to-finish-all-activities-except-the-first-activity
    * submitted 2014-08-06, edited 2017-10-10
    * "To clear top activities from stack use below code
    * It will delete all activities from stack either asynctask run or not in the application." */

    public void finishMeeting(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        | Intent.FLAG_ACTIVITY_CLEAR_TOP
        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}