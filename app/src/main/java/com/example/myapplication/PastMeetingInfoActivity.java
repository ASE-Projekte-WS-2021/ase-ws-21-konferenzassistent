package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@SuppressLint("NewApi")
public class PastMeetingInfoActivity extends AppCompatActivity {

    private TextView tvMinutes, tvStartTime, tvEndTime,
            tvNumOfParticipants,
            tvLocation,
            tvWindowInterval, tvWindowTime, tvDistanceInterval;

    private int minutes;
    private LocalDateTime startTime, endTime;
    private int numOfParticipants;
    private String location;
    private int windowInterval, windowTime, distanceInterval;

    private ArrayList<String> participantList;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_meeting_info);

        findTextViews();
        updateData();
        updateView();
        setParticipantListAndAlert();
    }

    private void setParticipantListAndAlert() {
        participantList = new ArrayList<>();
        participantList.add("test1");
        participantList.add("test2");
        participantList.add("test3");
        participantList.add("test4");
        participantList.add("test5");
        String[] participantArray = new String[participantList.size()];
        participantArray = participantList.toArray(participantArray);

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(PastMeetingInfoActivity.this)
                .setItems(participantArray, (dialogInterface, i) -> {
                    // do nothing
                })
                .setPositiveButton("OK", ((dialogInterface, i) -> {
                    // do nothing
                }));
        final AlertDialog a = materialAlertDialogBuilder.create();
        findViewById(R.id.cardViewParticipants).setOnClickListener(view -> a.show());
    }

    private void updateView() {
        tvMinutes.setText(minutes + " Minuten");
        tvStartTime.setText(dateTimeFormatter.format(startTime));
        tvEndTime.setText(dateTimeFormatter.format(endTime));

        tvNumOfParticipants.setText(numOfParticipants + " Teilnehmer");

        tvLocation.setText(location);

        tvWindowInterval.setText(windowInterval + " Minuten");
        tvWindowTime.setText(windowTime + " Minuten");
        tvDistanceInterval.setText(distanceInterval + " Minuten");
    }

    private void updateData() {
        minutes = 23;
        startTime = LocalDateTime.now();
        endTime = LocalDateTime.now();
        numOfParticipants = 5;
        location = "Testgelände";
        windowInterval = 15;
        windowTime = 5;
        distanceInterval = 10;
    }

    private void findTextViews() {
        tvMinutes = findViewById(R.id.tvMinutes);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);

        tvNumOfParticipants = findViewById(R.id.tvNumOfParticipants);

        tvLocation = findViewById(R.id.tvLocation);

        tvWindowInterval = findViewById(R.id.tvWindowInterval);
        tvWindowTime = findViewById(R.id.tvWindowTime);
        tvDistanceInterval = findViewById(R.id.tvDistanceInterval);
    }
}