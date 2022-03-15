package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CountdownActivity extends AppCompatActivity {



    public static final String COUNTDOWN_BUTTONS = "my.action.COUNTDOWN_BUTTONS";

    private long maxWindowClosedTime;
    private long maxWindowOpenTime;
    private long maxAbstandsTime;

    private boolean countdownPaused = false;
    private boolean abstandPaused = false;

    private boolean isOpen;
    private boolean lueftungIsFinished = false;
    private boolean abstandIsFinished = false;

    private boolean lueftungsSwitchStatus = false;
    private boolean abstandsSwitchStatus = false;

    private Date startDate;

    private String participantCount = "0";
    private String ort;

    // Meeting end Button
    private Button endMeetingButton;

    // Fragments
    private CountdownTimerFragment timer;
    private Fragment information;

    // Tabs Layout
    TabLayout tabLayout;
    ViewPager2 viewPager;
    TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        //getSupportActionBar().setTitle("Meeting");
        // Hide the action bar
        getSupportActionBar().hide();

        getIntents();

        // Get the Starting Time of the Meeting
        startDate = new Date();

        // Start the Countdown Service
        startCountDownService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register the Receiver
        registerReceiver(br, new IntentFilter(CountdownService.COUNTDOWN_SERVICE));
        // Make sure the Timer updates
        wakeUpTimer();

        // Init Views
        initViews();
        timer.hideUI(lueftungsSwitchStatus,abstandsSwitchStatus);
        // Set the ProgressBar Values to the initial Values
        timer.setupProgressBars(maxAbstandsTime, maxWindowClosedTime);

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

    // Finds Views
    protected void initViews(){
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        endMeetingButton = findViewById(R.id.end_meeting);

        // create timer
        timer = CountdownTimerFragment.newInstance();
        CountdownInformationFragment information = CountdownInformationFragment.newInstance();

        tabAdapter = new TabAdapter(getSupportFragmentManager(), getLifecycle());
        // Add fragments
        tabAdapter.addFragment(timer);
        tabAdapter.addFragment(information);
        // Set Adapter
        viewPager.setAdapter(tabAdapter);
        // Create Tabs
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabConfigurationStrategy());
        tabLayoutMediator.attach();

        // Set listeners for End Button
        endMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Butten gedrückt halten um Meeting zu beenden", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        endMeetingButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                displayDialog(view).show();
                return true;
            }
        });
    }


    private AlertDialog displayDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("ja", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                finishMeeting(view);
            }
        });
        builder.setNegativeButton("nein", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setMessage("Wollen sie das Meeting wirklich Beenden?");
        return builder.create();

    }

    // Get the Data from the Intents
    protected void getIntents(){

        // Get Countdown Timer
        maxWindowClosedTime = getIntent().getLongExtra("maxCountdownTime", 0) * 60000;
        maxWindowOpenTime = getIntent().getLongExtra("maxLueftungsTimer", 0) * 60000;
        maxAbstandsTime = getIntent().getLongExtra("maxAbstandsTimer", 0) * 60000;

        // Get Activation Status
        lueftungsSwitchStatus = getIntent().getBooleanExtra("lueftungsSwitchStatus", false);
        abstandsSwitchStatus = getIntent().getBooleanExtra("abstandsSwitchStatus", false);

        // Get Meeting Information
        participantCount = getIntent().getStringExtra("participantCount");
        ort = getIntent().getStringExtra("location");
    }

    // Start the Countdown Service
    private void startCountDownService(){
        Intent countdownIntent = new Intent(this, CountdownService.class);

        // Put the countdown timer as Extra
        countdownIntent.putExtra("maxCountdownTime", maxWindowClosedTime);
        countdownIntent.putExtra("maxLueftungsTimer", maxWindowOpenTime);
        countdownIntent.putExtra("maxAbstandsTimer", maxAbstandsTime);

        // Put the countdown status as Extra
        countdownIntent.putExtra("lueftungsSwitchStatus", lueftungsSwitchStatus);
        countdownIntent.putExtra("abstandsSwitchStatus", abstandsSwitchStatus);

        // start the service
        startService(countdownIntent);
    }

    // Broadcast Receiver to receive updates on the countdown
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // send Update to the UI
            updateTime(intent);
        }
    };

    // Wakes up the timer if app was in background
    private void wakeUpTimer(){
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        // set userInteraction to false to signal its not a Button Press
        intent.putExtra("userInteraction",false);
        sendBroadcast(intent);
    }

    // Update Countdown Timer and sends UI updates to the Fragment
    private void updateTime(Intent intent){
        // Check if the Intent has Extras
        if(intent.getExtras() != null){
            // get the countdowns and if window is open
            long lueftungsMilliS = intent.getLongExtra("lueftungsMilliS", 0);
            isOpen = intent.getBooleanExtra("windowOpen", false);

            long abstandsMilliS = intent.getLongExtra("abstandsMilliS", 0);

            // Get if timer is finished
            lueftungIsFinished = intent.getBooleanExtra("lueftungDone", false);
            abstandIsFinished = intent.getBooleanExtra("abstandDone", false);

            // Update the UI of the timer
            timer.UpdateUI(isOpen, lueftungIsFinished, abstandIsFinished, abstandsMilliS, lueftungsMilliS);
        }
    }

    // Starts the lueftungs countdown
    public void startLueftung(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("lueftungsUserInteraction", true);
        sendBroadcast(intent);

        // Check if Window is open and reset progressbars in the UI
       if(!isOpen)timer.resetLueftungsProgressBar(maxWindowOpenTime);
       else timer.resetLueftungsProgressBar(maxWindowClosedTime);
    }

    // Starts the abstands countdown
    public void startAbstand(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("abstandsUserInteraction", true);
        sendBroadcast(intent);
    }

    // Pauses the lueftungsCountdown
    public void pauseLueftungsCountdown(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("lueftungsPauseUserInteraction", true);
        sendBroadcast(intent);
        countdownPaused = !countdownPaused;

        timer.pauseButtonToggle(countdownPaused, CountdownTimerFragment.PAUSE_LUEFTUNGS_BUTTON);
    }

    // Paused the abstands countdown
    public void pauseAbstand(View view){
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra("abstandsPauseUserInteraction", true);
        sendBroadcast(intent);
        abstandPaused = !abstandPaused;

        timer.pauseButtonToggle(abstandPaused, CountdownTimerFragment.PAUSE_ABSTAND_BUTTON);
    }

    private void SaveToDatabase(){
        Date endDate = new Date();
        long diff = endDate.getTime() - startDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());

        MettingDatabase database = new MettingDatabase(this);
        database.addMeeting("" + dateFormat.format(startDate), dateFormat.format(endDate),ort, "" + seconds, "" + participantCount);
    }

    /* finishMeeting method code by Kimmi Dhingra:
     * https://stackoverflow.com/questions/18442328/how-to-finish-all-activities-except-the-first-activity
     * submitted 2014-08-06, edited 2017-10-10
     * "To clear top activities from stack use below code
     * It will delete all activities from stack either asynctask run or not in the application." */

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