package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {
    private Button replayButton;
    private Button pauseButton;

    private TextView countdownName;
    private TextView countdownTimer;

    private ProgressBar countdownSpinner;
    private TimerListener listener;

    private int maxValue;
    private String cdName;

    public TimerFragment() {
        // Required empty public constructor
    }

interface TimerListener {
    void onReplaySend(View view);
    void onPauseSend(View view);
}


    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    public void setListener(TimerListener listener){
        this.listener = listener ;

    }

    private void initViews(View view){
        replayButton = view.findViewById(R.id.replay_button);
        pauseButton = view.findViewById(R.id.pause_button);

        countdownName = view.findViewById(R.id.countdown_name);
        countdownTimer = view.findViewById(R.id.countdown_timer);

        countdownSpinner = view.findViewById(R.id.countdown_spinner);

        // Setup View
        setProgressBar(maxValue);
        setTimerName(cdName);

        // Setup onclick listeners
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReplaySend(view);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPauseSend(view);
            }
        });
    }

    // Update Timer
    public void updateTimer(String time){
        countdownTimer.setText(time);
    }

    // Update the ProgressBar to 100%
    public void setProgressBar(int maxValue){
        if(countdownSpinner != null)
            countdownSpinner.setMax(maxValue);
        else
            this.maxValue = maxValue;

    }

    // Sets the Timer name
    public void setTimerName(String name){
        if(countdownName != null)
            countdownName.setText(name);
        else
            cdName = name;

    }

    // Switch replay button status
    public void setReplayButtonEnabled(boolean enabled){
            replayButton.setEnabled(enabled);
    }

    public void setSpinnerProgress(int value){
        countdownSpinner.setProgress(value);
    }

    // Switch pause button status
    public void setPauseButton(boolean enabled){
        if(enabled)
            pauseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_play_arrow_24, null));
        else
            pauseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_pause_24, null));

    }

}