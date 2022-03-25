package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.data.MeetingParticipantPair;
import com.example.myapplication.data.RoomDB;

import java.time.format.DateTimeFormatter;
import java.util.List;


@SuppressLint("NewApi")
public class PastMeetingInfoActivity extends AppCompatActivity {

    private String title;
    private String duration;
    private String startTime;
    private String endTime;
    private String participants;
    private String ort;
    private String date;

    TextView meeting;
    TextView meetingDate;
    TextView meetingDuration;
    TextView meetingStartTime;
    TextView meetingEndTime;
    TextView meetingParticipantCount;
    TextView meetingOrt;
    ImageButton cancelButton;
    ImageButton moreButton;
    private int dataBaseID;
    private RoomDB database;

    private List<MeetingParticipantPair> meetingList;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_bottom_sheet);

        database = RoomDB.getInstance(getBaseContext());
        meetingList = database.meetingWithParticipantDao().getMeetings();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        findViewById(R.id.transparent_background).setBackgroundColor(getColor(R.color.corona_blue));
        findViewById(R.id.meeting_bottom_sheet_participants_dropdown_indicator).setVisibility(View.GONE);
        getView();
        setView();

        setupListeners();
        dataBaseID = getIntent().getIntExtra("Database_ID", 0);
        database = RoomDB.getInstance(getBaseContext());

        //database.meetingDao().getAll();
    }

    private void getView() {
        meeting = findViewById(R.id.nameToolbar);
        meetingDate = findViewById(R.id.meetingDate);
        meetingDuration = findViewById(R.id.duration);
        meetingStartTime = findViewById(R.id.startTime);
        meetingEndTime = findViewById(R.id.endTime);
        meetingParticipantCount = findViewById(R.id.participantCount);
        meetingOrt = findViewById(R.id.ort);

        cancelButton = findViewById(R.id.cancelBtn);
        moreButton = findViewById(R.id.moreBtn);
    }

    private void setView() {
        MeetingParticipantPair meetingData = meetingList.get(meetingList.size()-1);

        duration = meetingData.getMeeting().getDuration()/60 + "";
        startTime = meetingData.getMeeting().getStartDate().substring(11);
        date = meetingData.getMeeting().getStartDate().substring(0,10);
        endTime = meetingData.getMeeting().getEndDate().substring(11);

        meeting.setText(meetingData.getMeeting().getTitle());
        meetingDuration.setText(String.format(getBaseContext().getString(R.string.meeting_history_minutes_long), ""+
                duration));
        meetingDate.setText(date);
        meetingStartTime.setText(startTime);
        meetingEndTime.setText(endTime);
        meetingParticipantCount.setText("" + meetingData.getParticipants().size());
        meetingOrt.setText(meetingData.getMeeting().getLocation());
    }


    private void setupListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeScreen();
            }
        });
    }

    @Override
    public void onBackPressed() {
        openHomeScreen();
    }

    private void openHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}