package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMeetings;
    private TextView introText;

    private List<Meeting> meetingsList;
    MeetingHistoryAdapter meetingHistoryAdapter;
    LinearLayoutManager linearLayoutManager;
    private int counter = 0;

    MettingDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dbHelper = new MettingDatabase(this);

        /*
        String randomDate =
                2021 + "-" +
                        (new Random().nextInt(24) + 12) + "-" +
                        (new Random().nextInt(24) + 12) + " " +
                        (new Random().nextInt(50) + 25) + ":" +
                        (new Random().nextInt(50) + 25);
        String randomDateEnd =
                2021 + "-" +
                        (new Random().nextInt(24) + 12) + "-" +
                        (new Random().nextInt(24) + 12) + " " +
                        (new Random().nextInt(50) + 25) + ":" +
                        (new Random().nextInt(50) + 25);
        String randomLocation = Integer.toString(new Random().nextInt(1000) + 10);
        String randomDuration = Integer.toString(new Random().nextInt(5701) + 300);
        String randomParticipants = Integer.toString(new Random().nextInt(13) + 3);
        dbHelper.addMeeting(randomDate,randomDateEnd,randomLocation,randomDuration,randomParticipants);
        */

        rvMeetings = findViewById(R.id.rv_history);
        introText = findViewById(R.id.introText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        meetingsList = new ArrayList<>();
        Cursor cursor = dbHelper.readAllData();

        while (cursor.moveToNext()) {
            String mId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String mDate = cursor.getString(cursor.getColumnIndexOrThrow("meeting_date"));
            String mDateEnd = cursor.getString(cursor.getColumnIndexOrThrow("meeting_date_end"));
            String mDuration = cursor.getString(cursor.getColumnIndexOrThrow("meeting_duration"));
            String mLocation = cursor.getString(cursor.getColumnIndexOrThrow("meeting_position"));
            String mNumberParticipants = cursor.getString(cursor.getColumnIndexOrThrow("meeting_number_participants"));
            meetingsList.add(new Meeting(mId,mDate,mDateEnd,mLocation,mDuration,mNumberParticipants));
        }
        Log.d("database", Arrays.toString(cursor.getColumnNames()));
        for (Meeting m:  meetingsList) {
            Log.d("database", m.toString());
        }

        meetingHistoryAdapter = new MeetingHistoryAdapter(this,meetingsList);
        rvMeetings.setAdapter(meetingHistoryAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvMeetings.setLayoutManager(linearLayoutManager);

        // meetingHistoryAdapter.notifyDataSetChanged();
        setIntroTextVisibility();
    }

    private void onDataChanged() {
        meetingHistoryAdapter.notifyDataSetChanged();
        setIntroTextVisibility();
        linearLayoutManager.scrollToPosition(meetingsList.size() - 1);
    }

    private void setIntroTextVisibility() {
        if (meetingsList.isEmpty()) {
            introText.setVisibility(View.VISIBLE);
        } else {
            introText.setVisibility(View.INVISIBLE);
        }
    }

    public void openSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openPastMeeting(View view){
        Intent intent = new Intent(this, PastMeetingInfoActivity.class);
        startActivity(intent);
    }
}