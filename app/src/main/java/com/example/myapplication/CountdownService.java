package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class CountdownService extends Service {

    // Service Tag
    private final static String TAG = "CountdownService";
    public static final String COUNTDOWN_SERVICE = "com.example.myapplication.countdown_service";

    private long maxCountdownTime;
    private long lueftungsCountdown;
    private boolean isOpen = false;

    Intent bi = new Intent(COUNTDOWN_SERVICE);
    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // Stop the timer
        countDownTimer.cancel();
        super.onDestroy();
    }

    private void startTimer(long maxTime){
        // Create a new Countdown Timer
        countDownTimer = new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long milliSUnitlFinished) {

                // Broadcast the countdown and status of the window
                bi.putExtra("countdown", milliSUnitlFinished);
                bi.putExtra("windowOpen", isOpen);

                // Broadcast Timer
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                // Timer finished
                // Restart timer with Window Open/Closed
                if(isOpen){
                    startTimer(maxCountdownTime);
                    isOpen = false;
                }
                else{
                    startTimer(lueftungsCountdown);
                    isOpen = true;
                }
            }
        };
        // Start the timer
        countDownTimer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get max time from intent
        maxCountdownTime = intent.getLongExtra("maxCountdownTime", 0);
        lueftungsCountdown = intent.getLongExtra("maxLueftungsTimer", 0);

        // start timer with countdown timer
        startTimer(maxCountdownTime);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
