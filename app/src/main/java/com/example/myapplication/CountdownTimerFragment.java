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

import com.google.android.material.timepicker.TimeFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CountdownTimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountdownTimerFragment extends Fragment {
    public static final int PAUSE_ABSTAND_BUTTON = 0;
    public static final int PAUSE_LUEFTUNGS_BUTTON = 1;
    // Countdown Fragments
   private TimerFragment lueftungsTimer;
   private TimerFragment abstandsTimer;

    private View mView;

    private long maxWindowClosedTime;
    private long maxAbstandsTime;

    private boolean lueftungactive;
    private boolean abstandactive;

    private TextView windowStatus;

    public static CountdownTimerFragment newInstance(){
        return new CountdownTimerFragment();
    }

    // Implements the TimerListener for LueftungsTimer
    public static class LueftungsTimer extends TimerFragment implements TimerFragment.TimerListener{
        @Override
        public void onReplaySend(View view) {
            ((CountdownActivity)getActivity()).startLueftung(view);
        }

        @Override
        public void onPauseSend(View view) {
            ((CountdownActivity)getActivity()).pauseLueftungsCountdown(view);
        }
    }

    // Implements the TimerListener for AbstandsTimer
    public static class AbstandsTimer extends TimerFragment implements TimerFragment.TimerListener{
        @Override
        public void onReplaySend(View view) {
            ((CountdownActivity)getActivity()).startAbstand(view);
        }

        @Override
        public void onPauseSend(View view) {
            ((CountdownActivity)getActivity()).pauseAbstand(view);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create new Fragments
        lueftungsTimer = new LueftungsTimer();
        abstandsTimer = new AbstandsTimer();

        // Set the listeners
        lueftungsTimer.setListener((TimerFragment.TimerListener) lueftungsTimer);
        abstandsTimer.setListener((TimerFragment.TimerListener) abstandsTimer);
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

        // Set countdown titles
        lueftungsTimer.setTimerName("Lüftungs Timer");
        abstandsTimer.setTimerName("Abstands Timer");
    }

    // Gets the Views by Id
    private void initiateComponents(View view){
        if(mView != null) {
            getChildFragmentManager().beginTransaction().
                    replace(R.id.fragment_container_lueftung, lueftungsTimer)
                    .replace(R.id.fragmet_container_abstand, abstandsTimer)
                    .commit();

           windowStatus = mView.findViewById(R.id.window_status_text);
        }
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
               mView.findViewById(R.id.fragment_container_lueftung).setVisibility(View.GONE);
            }
            // If "Abstand" is disabled
            if (!abstandsSwitchStatus) {
                mView.findViewById(R.id.fragmet_container_abstand).setVisibility(View.GONE);
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
                windowStatus.setText("Fenster sollte geöffnet sein!");
            else
                windowStatus.setText("Fenster sollte geschlossen sein!");

            // Check if Lüftungstimer is done to Enable the Buttons
            if (lueftungIsFinished)
                lueftungsTimer.setReplayButtonEnabled(true);
            else
                lueftungsTimer.setReplayButtonEnabled(false);

            // Check if Abstandstimer is done to Enable the Buttons
            if (abstandIsFinished)
                abstandsTimer.setReplayButtonEnabled(true);
            else
                abstandsTimer.setReplayButtonEnabled(false);

            // Build the Text String
            String lueftungsTimeLeft = timeStringBuilder(lueftungsMilliS);
            String abstandsTimeLeft = timeStringBuilder(abstandsMilliS);

            // Set the countdown text
            lueftungsTimer.updateTimer(lueftungsTimeLeft);
            abstandsTimer.updateTimer(abstandsTimeLeft);

            // Update the Progress Bars
            lueftungsTimer.setSpinnerProgress((int) (lueftungsMilliS / 1000));
            abstandsTimer.setSpinnerProgress((int) (abstandsMilliS / 1000));
        }
    }

    // Resets the Window Progress Bar to the defined time
    public void resetLueftungsProgressBar(long time){
        lueftungsTimer.setProgressBar((int) time /1000);
    }

    public void resetAbstandProgressBar(long time){
        abstandsTimer.setProgressBar((int) time /1000);
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
            abstandsTimer.setPauseButton(paused);
        else
            lueftungsTimer.setPauseButton(paused);
    }


}