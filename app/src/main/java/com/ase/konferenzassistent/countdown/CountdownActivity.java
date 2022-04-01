package com.ase.konferenzassistent.countdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.mainscreen.history.PastMeetingInfoActivity;
import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.shared.TabAdapter;
import com.ase.konferenzassistent.data.MeetingData;
import com.ase.konferenzassistent.data.MeetingWithParticipantData;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.meetingwizard.MeetingWizardActivity;
import com.ase.konferenzassistent.meetingwizard.Participant;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.ase.konferenzassistent.meetingwizard.cdServiceObject;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class CountdownActivity extends AppCompatActivity implements Serializable, CustomAlertBottomSheetAdapter.onLeaveListener {


    public static final String COUNTDOWN_BUTTONS = "my.action.COUNTDOWN_BUTTONS";
    public static final String COUNTDOWN_OBJECTS = "COUNTDOWN_OBJECTS";
    public static final String PAUSE_BUTTON_PRESSED_ID = "PAUSE_BUTTON_PRESSED_ID";
    public static final String REPLAY_BUTTON_PRESSED_ID = "REPLAY_BUTTON_PRESSED_ID";
    TextView meetingTitle;
    // Tabs Layout
    TabLayout tabLayout;
    ViewPager2 viewPager;
    TabAdapter tabAdapter;
    ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> countdownObjects;
    ArrayList<cdServiceObject> serviceObjectArrayList = new ArrayList<>();
    ArrayList<Participant> participants = new ArrayList<>();
    private Date startDate;
    private String title = "Test Meeting";
    private String participantCount = "0";
    private String ort;
    // Fragments
    private CountdownTimerFragment timer;
    private CountdownInformationFragment information;


    // Object used to Create Timer in the Service
    // Broadcast Receiver to receive updates on the countdown
    private final BroadcastReceiver br = new BroadcastReceiver() {
        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            // send Update to the UI
            updateTime(intent);
            serviceObjectArrayList.clear();
            serviceObjectArrayList = (ArrayList<cdServiceObject>) intent.getSerializableExtra(COUNTDOWN_OBJECTS);
            timer.updateView(serviceObjectArrayList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        // Hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
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

        // Init Views
        initViews();

        // Set the meeting title
        meetingTitle.setText(title);

        // Date Formatter
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());

        information.getValuesForTextViews(ort, "" + dateFormat.format(startDate), participantCount);
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
    protected void initViews() {
        meetingTitle = findViewById(R.id.meeting_title);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        // Meeting end Button
        Button endMeetingButton = findViewById(R.id.end_meeting);

        // Create Views
        timer = CountdownTimerFragment.newInstance(serviceObjectArrayList);
        information = CountdownInformationFragment.newInstance(serviceObjectArrayList, participants);

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
        endMeetingButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(),
                "Butten gedrückt halten um Meeting zu beenden", Toast.LENGTH_SHORT).show());

        endMeetingButton.setOnLongClickListener(view -> {
            // Warn user about leaving
            onLeaveWarning();
            return true;
        });
    }

    // Warning when user clicks on the leave button
    private void onLeaveWarning() {
        CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
        customAlertBottomSheetAdapter.setWarningText("Solle das Meeting wirklich Beendet werden?");
        customAlertBottomSheetAdapter.setAcceptText("Meeting Beenden");
        customAlertBottomSheetAdapter.setDeclineText("Meeting Fortfahren");
        customAlertBottomSheetAdapter.show(getSupportFragmentManager(), customAlertBottomSheetAdapter.getTag());
    }

    // Get the Data from the Intents
    @SuppressWarnings("unchecked")
    protected void getIntents() {
        countdownObjects =
                (ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject>)
                        getIntent().getSerializableExtra(MeetingWizardActivity.COUNTDOWN_ARRAY);

        participants =
                (ArrayList<Participant>)
                        getIntent().getSerializableExtra(MeetingWizardActivity.PARTICIPANT_ARRAY);

        ort = getIntent().getStringExtra(MeetingWizardActivity.MEETING_LOCATION);
        title = getIntent().getStringExtra(MeetingWizardActivity.MEETING_TITLE);

        participantCount = "" + participants.size();

        // TODO: Rework that part :YIKERS:
        // Create an Array List with all the active Countdowns
        countdownObjects.forEach(object -> {
            // Check if the Countdown is enabled
            if (object.getmEnabled()) {
                // create an new countdown object
                cdServiceObject countdown = new cdServiceObject(
                        object,
                        object.getmItems().get(0).getSubCountdown() * 60000,
                        false,
                        0,
                        countdownObjects.indexOf(object));
                // add the countdown to the new list
                serviceObjectArrayList.add(countdown);
            }
        });

    }

    // Start the Countdown Service
    private void startCountDownService() {
        Intent countdownIntent = new Intent(this, CountdownService.class);

        // Put the countdown timers as Extra
        countdownIntent.putExtra(COUNTDOWN_OBJECTS, serviceObjectArrayList);

        // start the service
        startService(countdownIntent);
    }



    // Pauses the lueftungsCountdown
    public void pauseCountdown(Integer id) {
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra(PAUSE_BUTTON_PRESSED_ID, id);

        sendBroadcast(intent);
    }

    public void startCountdown(Integer id) {
        // Send a Broadcast to the Service if button is pressed
        Intent intent = new Intent(COUNTDOWN_BUTTONS);
        intent.putExtra(REPLAY_BUTTON_PRESSED_ID, id);
        sendBroadcast(intent);

    }


    // Update Countdown Timer and sends UI updates to the Fragment
    private void updateTime(Intent intent) {
        // Check if the Intent has Extras
        intent.getExtras();// TODO: implement new logic
    }

    private void saveToDatabase() {
        Date endDate = new Date();
        long diff = endDate.getTime() - startDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());

        RoomDB database = RoomDB.getInstance(getBaseContext());
        MeetingData meetingData = new MeetingData();
        meetingData.setStartDate(dateFormat.format(startDate));
        meetingData.setEndDate(dateFormat.format(endDate));
        if (ort != null)
            meetingData.setLocation(ort);
        else
            meetingData.setLocation(getString(R.string.meeting_data_no_location));
        meetingData.setTitle(title);
        meetingData.setDuration(seconds);

        long ID = database.meetingDao().insert(meetingData);

        participants.forEach(participant -> {
            MeetingWithParticipantData meetingWithParticipantData = new
                    MeetingWithParticipantData();
            meetingWithParticipantData.setMeetingID((int) ID);
            meetingWithParticipantData.setParticipantID(participant.getId());
            database.meetingWithParticipantDao().insert(meetingWithParticipantData);
        });
    }

    /* finishMeeting method code by Kimmi Dhingra:
     * https://stackoverflow.com/questions/18442328/how-to-finish-all-activities-except-the-first-activity
     * submitted 2014-08-06, edited 2017-10-10
     * "To clear top activities from stack use below code
     * It will delete all activities from stack either asynctask run or not in the application." */

    public void finishMeeting() {
        Intent intent = new Intent(this, PastMeetingInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        saveToDatabase();

        // Send -1 to signal that its the latest entry in the database
        intent.putExtra("Database_ID", -1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLeaving() {
        finishMeeting();
    }

    @Override
    public void clearWarnings() {

    }
}