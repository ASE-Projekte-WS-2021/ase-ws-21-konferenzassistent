package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CountdownActivity extends AppCompatActivity {

    // Countdown TextView
    private TextView countdownText;
    private long maxCountdownTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        countdownText = findViewById(R.id.countdownView);

        // get max Countdown from intent
        maxCountdownTime = getIntent().getLongExtra("maxCountdownTime", 0);

        // start the Countdown service
        Intent countdownIntent = new Intent(this, CountdownService.class);
        // add the countdown Time to the intent
        countdownIntent.putExtra("maxCountdownTime", maxCountdownTime);
        startService(countdownIntent);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update the Gui
            updateTime(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // register the Receiver
        registerReceiver(br, new IntentFilter(CountdownService.COUNTDOWN_SERVICE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister the Receiver
        unregisterReceiver(br);
    }

    @Override
    public void onStop() {
        try {
            // unregister the Receiver
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // stop the Service
        stopService(new Intent(this, CountdownService.class));
        super.onDestroy();
    }

    // Update Countdown Timer
    private void updateTime(Intent intent){
        if(intent.getExtras() != null){
            long milliSUntilFinish = intent.getLongExtra("countdown", 0);

            // Convert to minutes and seconds
            int minutes = (int) milliSUntilFinish/60000;
            int seconds = (int) milliSUntilFinish%60000/1000;

            // Build a string
            String timeLeft;

            timeLeft = "" + minutes;
            timeLeft += ":";
            // Add a leading 0 to seconds
            if(seconds < 10) timeLeft += "0";
            timeLeft += seconds;

            // Set the countdown text
            countdownText.setText(timeLeft);
        }


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