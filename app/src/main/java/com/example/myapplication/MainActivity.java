package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMeetings;
    private TextView introText;

    private ArrayList<String> meetingsList;
    MeetingHistoryAdapter meetingHistoryAdapter;
    LinearLayoutManager linearLayoutManager;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        rvMeetings = findViewById(R.id.rv_history);
        introText = findViewById(R.id.introText);

        meetingsList = new ArrayList<>();
        meetingsList.add("test");

        meetingHistoryAdapter = new MeetingHistoryAdapter(this,meetingsList);
        rvMeetings.setAdapter(meetingHistoryAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvMeetings.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
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