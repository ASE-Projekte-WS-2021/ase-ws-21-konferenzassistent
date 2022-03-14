package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountdownTimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountdownTimerFragment extends Fragment {
    public static final int PAUSE_ABSTAND_BUTTON = 0;
    public static final int PAUSE_LUEFTUNGS_BUTTON = 1;
    // Countdown TextView
    private TextView countdownText;
    private Button startCountdownButton;

    private Button abstandsButton;
    private TextView abstandsView;

    private Button abstandsPauseButton;
    private Button countdownPauseButton;

    private TextView lueftungsInfoText;

    private ProgressBar abstandsProgressBar;
    private ProgressBar lueftungsProgressBar;

    private TextView teilnehmerTextView;
    private TextView ortTextView;

    private View mView;

    private long maxWindowClosedTime;
    private long maxAbstandsTime;

    private boolean lueftungactive;
    private boolean abstandactive;

    public static CountdownTimerFragment newInstance(){
        return new CountdownTimerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_countdown_timer, container, false);
        this.mView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get Views
        initiateComponents(view);

        // Hide UI
        hideUI(lueftungactive, abstandactive);

        // Set Progress bar
        setupProgressBars(maxAbstandsTime,maxWindowClosedTime);
        /*
        if (ort.equals("")) {
            ort = "<leer>";
        }

                // Setting the initial and Max values of the Progress bar
        setProgressBarValues(maxAbstandsTime, abstandsProgressBar);
        setProgressBarValues(maxCountdownTime, lueftungsProgressBar);

        */
                 /*
        FillInformationField("" + participantCount, ort);

        */

    }

    // Gets the Views by Id
    private void initiateComponents(View view){
        if(mView != null) {
            countdownText = mView.findViewById(R.id.countdownView);
            startCountdownButton = mView.findViewById((R.id.StartButton));

            abstandsPauseButton = mView.findViewById(R.id.abstandsPauseButton);
            countdownPauseButton = mView.findViewById(R.id.PauseButton);

            abstandsButton = mView.findViewById(R.id.abstandsButton);
            abstandsView = mView.findViewById(R.id.abstandsView);

            lueftungsInfoText = mView.findViewById(R.id.lueftungsInfoText);

            abstandsProgressBar = mView.findViewById(R.id.abstandsProgressBar);
            lueftungsProgressBar = mView.findViewById(R.id.lueftungsProgressBar);

        }
    }

    // TODO: delete
    private void FillInformationField(String teilnehmer, String ort){
        teilnehmerTextView.setText(teilnehmer);
        ortTextView.setText(ort);
    }

    public void setupProgressBars(long maxAbstandsTime, long maxWindowClosedTime){
        // Check if the View is null
        if(mView != null) {
            resetLueftungsProgressBar(maxWindowClosedTime);
            resetAbstandProgressBar(maxAbstandsTime);
        }
        else
        {
            // if view is null save the values for later
            this.maxWindowClosedTime = maxWindowClosedTime;
            this.maxAbstandsTime = maxAbstandsTime;
        }
    }

    // Sets the Ui to Invisible if not used
    public void hideUI(boolean lueftungsSwitchStatus, boolean abstandsSwitchStatus){
        // Makes sure the view is not null
        if(mView != null) {

            // If "Lüftung" is disabled
            if (!lueftungsSwitchStatus) {
                countdownText.setVisibility(View.GONE);
                startCountdownButton.setVisibility(View.GONE);
                lueftungsProgressBar.setVisibility(View.GONE);
                lueftungsInfoText.setVisibility(View.GONE);
                mView.findViewById(R.id.materialCardView3).setVisibility(View.GONE);
                mView.findViewById(R.id.materialCardView5).setVisibility(View.GONE);
            }
            // If "Abstand" is disabled
            if (!abstandsSwitchStatus) {
                abstandsView.setVisibility(View.GONE);
                abstandsButton.setVisibility(View.GONE);
                abstandsProgressBar.setVisibility(View.GONE);
                mView.findViewById(R.id.materialCardView4).setVisibility(View.GONE);
            }
        }
        else
        {
            // If view is null save the status for later
            abstandactive = abstandsSwitchStatus;
            lueftungactive = lueftungsSwitchStatus;
        }
    }

    public void UpdateUI(boolean isOpen, boolean lueftungIsFinished, boolean abstandIsFinished, long abstandsMilliS, long lueftungsMilliS){
        // Check if View is null
        if(mView != null) {
            // Change the Information Text depending on the window status
            if (isOpen)
                lueftungsInfoText.setText("Fenster sollte geöffnet sein!");
            else
                lueftungsInfoText.setText("Fenster sollte geschlossen sein!");

            // Check if Lüftungstimer is done to Enable the Buttons
            if (lueftungIsFinished)
                startCountdownButton.setEnabled(true);
            else
                startCountdownButton.setEnabled(false);

            // Check if Abstandstimer is done to Enable the Buttons
            if (abstandIsFinished)
                abstandsButton.setEnabled(true);
            else
                abstandsButton.setEnabled(false);

            // Build the Text String
            String lueftungsTimeLeft = timeStringBuilder(lueftungsMilliS);
            String abstandsTimeLeft = timeStringBuilder(abstandsMilliS);

            // Set the countdown text
            countdownText.setText(lueftungsTimeLeft);
            abstandsView.setText(abstandsTimeLeft);

            // Update the Progress Bars
            abstandsProgressBar.setProgress((int) (abstandsMilliS / 1000));
            lueftungsProgressBar.setProgress((int) (lueftungsMilliS / 1000));
        }
    }

    // Resets the Window Progress Bar to the defined time
    public void resetLueftungsProgressBar(long time){
        lueftungsProgressBar.setMax((int) time /1000);
    }

    public void resetAbstandProgressBar(long time){
        abstandsProgressBar.setMax((int) time /1000);
    }

    // Sets the initial values of the Progress Bar
    private void setProgressBarValues(long startTime, ProgressBar progressBar) {
        progressBar.setMax((int) startTime / 1000);
        progressBar.setProgress((int) startTime / 1000);
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

    // Toggles the pause Buttons
    public void pauseButtonToggle(boolean paused, int id){
        if(id == PAUSE_ABSTAND_BUTTON)
            toggleIcon(abstandsPauseButton, paused);
        else
            toggleIcon(countdownPauseButton, paused);
    }

    // Toggles the pause icon
    private void toggleIcon(Button button, Boolean isPaused){
        if(isPaused)
        {
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_play_arrow_24, null));
        }
        else{
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_pause_24, null));
        }
    }
}