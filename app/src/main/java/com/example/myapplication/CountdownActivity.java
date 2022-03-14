package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CountdownActivity extends AppCompatActivity {

    // Countdown TextView
    private TextView countdownText;
    private Button startCountdownButton;

    private Button abstandsButton;
    private TextView abstandsView;

    private Button abstandsPauseButton;
    private Button countdownPauseButton;

    private TextView lueftungsInfoText;

    public static final String COUNTDOWN_BUTTONS = "my.action.COUNTDOWN_BUTTONS";

    private long maxCountdownTime;
    private long maxLueftungsTime;
    private long maxAbstandsTime;

    private boolean countdownPaused = false;
    private boolean abstandPaused = false;

    private boolean isOpen;
    private boolean lueftungIsFinished = false;
    private boolean abstandIsFinished = false;

    private boolean lueftungsSwitchStatus = false;
    private boolean abstandsSwitchStatus = false;

    private ProgressBar abstandsProgressBar;
    private ProgressBar lueftungsProgressBar;

    private TextView teilnehmerTextView;
    private TextView ortTextView;

    private Date startDate;
    private Date endDate;

    private String participantCount = "0";
    private String ort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        getSupportActionBar().setTitle("Meeting");

        initiateComponents();

        startDate = new Date();

        // get max Countdown from intent
        maxCountdownTime = getIntent().getLongExtra("maxCountdownTime", 0) * 60000;
        maxLueftungsTime = getIntent().getLongExtra("maxLueftungsTimer", 0) * 60000;

        maxAbstandsTime = getIntent().getLongExtra("maxAbstandsTimer", 0) * 60000;

        lueftungsSwitchStatus = getIntent().getBooleanExtra("lueftungsSwitchStatus", false);
        abstandsSwitchStatus = getIntent().getBooleanExtra("abstandsSwitchStatus", false);
        participantCount = getIntent().getStringExtra("participantCount");
        ort = getIntent().getStringExtra("location");

        if (ort.equals("")) {
            ort = "<leer>";
        }

        FillInformationField("" + participantCount, ort);

        // start the Countdown service
        Intent countdownIntent = new Intent(this, CountdownService.class);

        // add the countdown Time to the intent
        countdownIntent.putExtra("maxCountdownTime", maxCountdownTime);
        countdownIntent.putExtra("maxLueftungsTimer", maxLueftungsTime);

        countdownIntent.putExtra("maxAbstandsTimer", maxAbstandsTime);

        countdownIntent.putExtra("lueftungsSwitchStatus", lueftungsSwitchStatus);
        countdownIntent.putExtra("abstandsSwitchStatus", abstandsSwitchStatus);
        // start the service
        startService(countdownIntent);

        // Setting the initial and Max values of the Progress bar
        setProgressBarValues(maxAbstandsTime, abstandsProgressBar);
        setProgressBarValues(maxCountdownTime, lueftungsProgressBar);

        hideUI();
    }

    private void FillInformationField(String teilnehmer, String ort){
        teilnehmerTextView.setText(teilnehmer);
        ortTextView.setText(ort);
    }

    // Finds the Components in the View
    private void initiateComponents(){
        countdownText = findViewById(R.id.countdownView);
        startCountdownButton = findViewById((R.id.StartButton));

        abstandsPauseButton = findViewById(R.id.abstandsPauseButton);
        countdownPauseButton = findViewById(R.id.PauseButton);

        abstandsButton = findViewById(R.id.abstandsButton);
        abstandsView = findViewById(R.id.abstandsView);

        lueftungsInfoText = findViewById(R.id.lueftungsInfoText);

        abstandsProgressBar = findViewById(R.id.abstandsProgressBar);
        lueftungsProgressBar = findViewById(R.id.lueftungsProgressBar);

        teilnehmerTextView = findViewById(R.id.teilnehmerTextView);
        ortTextView = findViewById(R.id.ortTextView);
    }


    // Sets the Ui to Invisible if not used
    private void hideUI(){
        // If "Lüftung" is disabled
        if(!lueftungsSwitchStatus){
            countdownText.setVisibility(View.GONE);
            startCountdownButton.setVisibility(View.GONE);
            lueftungsProgressBar.setVisibility(View.GONE);
            lueftungsInfoText.setVisibility(View.GONE);
            findViewById(R.id.materialCardView3).setVisibility(View.GONE);
            findViewById(R.id.materialCardView5).setVisibility(View.GONE);
        }
        // If "Abstand" is disabled
        if(!abstandsSwitchStatus){
            abstandsView.setVisibility(View.GONE);
            abstandsButton.setVisibility(View.GONE);
            abstandsProgressBar.setVisibility(View.GONE);
            findViewById(R.id.materialCardView4).setVisibility(View.GONE);
        }

    }


    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update the Gui
            updateTime(intent);
        }
    };

    // Sets the initial values of the Progress Bar
    private void setProgressBarValues(long startTime, ProgressBar progressBar) {

        progressBar.setMax((int) startTime / 1000);
        progressBar.setProgress((int) startTime / 1000);

    }

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
            isOpen = intent.getBooleanExtra("windowOpen", false);

            long abstandsMilliS = intent.getLongExtra("abstandsMilliS", 0);

            // Get if timer is finished
            lueftungIsFinished = intent.getBooleanExtra("lueftungDone", false);
            abstandIsFinished = intent.getBooleanExtra("abstandDone", false);


            // Build a string
            String lueftungsTimeLeft = timeStringBuilder(lueftungsMilliS);
            String abstandsTimeLeft = timeStringBuilder(abstandsMilliS);


            // Add the description
            if(isOpen) {
               // lueftungsTimeLeft += " schließen!";
                lueftungsInfoText.setText("Fenster sollte geöffnet sein!");
            }
            else{
                //lueftungsTimeLeft += " öffnen!";
                lueftungsInfoText.setText("Fenster sollte geschlossen sein!");
            }

            // Check if Lüftungstimer is done
            if(lueftungIsFinished){
                // Recalibrate the Progress Bar cause timer changes every iteration

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

            abstandsProgressBar.setProgress((int) (abstandsMilliS / 1000));
            lueftungsProgressBar.setProgress((int) (lueftungsMilliS / 1000));

        }

    }

    public void startCountdown(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("lueftungsUserInteraction", true);
        sendBroadcast(intent);

        // Check if Window is open
       if(!isOpen)lueftungsProgressBar.setMax((int) maxLueftungsTime/1000);
       else lueftungsProgressBar.setMax((int) maxCountdownTime/1000);
    }

    public void startAbstand(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("abstandsUserInteraction", true);
        sendBroadcast(intent);
    }

    public void pauseCountdown(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("lueftungsPauseUserInteraction", true);
        sendBroadcast(intent);
        countdownPaused = !countdownPaused;

        toggleIcon(countdownPauseButton, countdownPaused);
    }

    public void pauseAbstand(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("abstandsPauseUserInteraction", true);
        sendBroadcast(intent);
        abstandPaused = !abstandPaused;

        toggleIcon(abstandsPauseButton, abstandPaused);
    }

    private void toggleIcon(Button button, Boolean isPaused){
        if(isPaused)
        {
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_play_arrow_24, null));
        }
        else{
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_pause_24, null));
        }
    }

    // Builds a String to show the Timer
    private String timeStringBuilder(long timer){
        // Convert to minutes and seconds
        int minutes = (int) timer/60000;
        int seconds = (int) timer%60000/1000;

        // Build a string
        String timeLeft;

        timeLeft = "" + minutes;
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

    private void SaveToDatabase(){
        endDate = new Date();
        long diff = endDate.getTime() - startDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());

        MettingDatabase database = new MettingDatabase(this);
        database.addMeeting("" + dateFormat.format(startDate), dateFormat.format(endDate),ort, "" + seconds, "" + participantCount);
    }

    public void finishMeeting(View view) {
        Intent intent = new Intent(this, PastMeetingInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        | Intent.FLAG_ACTIVITY_CLEAR_TOP
        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        SaveToDatabase();

        // Send -1 to signal that its the latest entry in the database
        intent.putExtra("Database_ID", -1);
        startActivity(intent);
        finish();
    }
}