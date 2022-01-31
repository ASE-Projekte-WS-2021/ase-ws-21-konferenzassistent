package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountdownActivity extends AppCompatActivity {

    // Countdown TextView
    private TextView countdownText;
    private Button startCountdownButton;

    private Button abstandsButton;
    private TextView abstandsView;

    public static final String COUNTDOWN_BUTTONS = "my.action.COUNTDOWN_BUTTONS";

    private long maxCountdownTime;
    private long maxLueftungsTime;
    private long maxAbstandsTime;

    private boolean lueftungIsFinished = false;
    private boolean abstandIsFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        countdownText = findViewById(R.id.countdownView);
        startCountdownButton = findViewById((R.id.StartButton));

        abstandsButton = findViewById(R.id.abstandsButton);
        abstandsView = findViewById(R.id.abstandsView);

        // get max Countdown from intent
        maxCountdownTime = getIntent().getLongExtra("maxCountdownTime", 0);
        maxLueftungsTime = getIntent().getLongExtra("maxLueftungsTimer", 0);

        maxAbstandsTime = getIntent().getLongExtra("maxAbstandsTimer", 0);

        // start the Countdown service
        Intent countdownIntent = new Intent(this, CountdownService.class);

        // add the countdown Time to the intent
        countdownIntent.putExtra("maxCountdownTime", maxCountdownTime);
        countdownIntent.putExtra("maxLueftungsTimer", maxLueftungsTime);

        countdownIntent.putExtra("maxAbstandsTimer", maxAbstandsTime);

        // start the service
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
        wakeUpTimer();
    }

    // Wakes up the timer if app was in background
    private void wakeUpTimer(){
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        // set userInteraction to false to signal its not a Button Press
        intent.putExtra("userInteraction",false);
        sendBroadcast(intent);
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

            // get the countdowns and if window is open
            long lueftungsMilliS = intent.getLongExtra("lueftungsMilliS", 0);
            boolean isOpen = intent.getBooleanExtra("windowOpen", false);

            long abstandsMilliS = intent.getLongExtra("abstandsMilliS", 0);

            // Get if timer is finished
            lueftungIsFinished = intent.getBooleanExtra("lueftungDone", false);
            abstandIsFinished = intent.getBooleanExtra("abstandDone", false);


            // Build a string
            String lueftungsTimeLeft = timeStringBuilder(lueftungsMilliS);
            String abstandsTimeLeft = timeStringBuilder(abstandsMilliS);


            // Add the description
            if(isOpen) {
                lueftungsTimeLeft += " schließen!";
                startCountdownButton.setText("Fenster Offen.");
            }
            else{
                lueftungsTimeLeft += " öffnen!";
                startCountdownButton.setText("Fenster Geschlossen.");
            }

            // Check if Lüftungstimer is done
            if(lueftungIsFinished){
                startCountdownButton.setEnabled(true);
            }
            else{
                startCountdownButton.setEnabled(false);
            }

            // Check if Abstandstimer is done
            if(abstandIsFinished){
                abstandsButton.setEnabled(true);
            }
            else{
                abstandsButton.setEnabled(false);
            }

            // Set the countdown text
            countdownText.setText(lueftungsTimeLeft);
            abstandsView.setText(abstandsTimeLeft);
        }

    }

    public void startCountdown(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("lueftungsUserInteraction", true);
        sendBroadcast(intent);
    }

    public void startAbstand(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("abstandsUserInteraction", true);
        sendBroadcast(intent);
    }

    // Builds a String to show the Timer
    private String timeStringBuilder(long timer){
        // Convert to minutes and seconds
        int minutes = (int) timer/60000;
        int seconds = (int) timer%60000/1000;

        // Build a string
        String timeLeft;

        timeLeft = "in " + minutes;
        timeLeft += ":";
        // Add a leading 0 to seconds
        if(seconds < 10) timeLeft += "0";
        timeLeft += seconds;

        return timeLeft;
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