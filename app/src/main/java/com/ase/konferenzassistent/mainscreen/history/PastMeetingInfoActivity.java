package com.ase.konferenzassistent.mainscreen.history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.data.MeetingParticipantPair;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.mainscreen.MainScreenActivity;

import java.util.List;


@SuppressLint("NewApi")
public class PastMeetingInfoActivity extends AppCompatActivity {

    TextView meeting;
    TextView meetingDate;
    TextView meetingDuration;
    TextView meetingStartTime;
    TextView meetingEndTime;
    TextView meetingParticipantCount;
    TextView meetingOrt;
    ImageButton cancelButton;
    ImageButton moreButton;

    private List<MeetingParticipantPair> meetingList;

    public PastMeetingInfoActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_bottom_sheet);

        RoomDB database = RoomDB.getInstance(getBaseContext());
        meetingList = database.meetingWithParticipantDao().getMeetings();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        findViewById(R.id.transparent_background).setBackgroundColor(getColor(R.color.corona_blue));
        findViewById(R.id.meeting_bottom_sheet_participants_dropdown_indicator).setVisibility(View.GONE);
        getView();
        setView();

        setupListeners();

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
        MeetingParticipantPair meetingData = meetingList.get(meetingList.size() - 1);

        String duration = meetingData.getMeeting().getDuration() / 60 + "";
        String startTime = meetingData.getMeeting().getStartDate().substring(11);
        String date = meetingData.getMeeting().getStartDate().substring(0, 10);
        String endTime = meetingData.getMeeting().getEndDate().substring(11);

        meeting.setText(meetingData.getMeeting().getTitle());
        meetingDuration.setText(String.format(getBaseContext().getString(R.string.meeting_history_minutes_long), "" +
                duration));
        meetingDate.setText(date);
        meetingStartTime.setText(startTime);
        meetingEndTime.setText(endTime);
        meetingParticipantCount.setText("" + meetingData.getParticipants().size());
        meetingOrt.setText(meetingData.getMeeting().getLocation());
    }


    private void setupListeners() {
        cancelButton.setOnClickListener(view -> openHomeScreen());
    }

    @Override
    public void onBackPressed() {
        openHomeScreen();
    }

    private void openHomeScreen() {
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}