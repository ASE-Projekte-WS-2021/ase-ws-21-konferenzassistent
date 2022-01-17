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
                bi.putExtra("countdown", milliSUnitlFinished);
                // Broadcast Timer
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                // Timer finished
            }
        };
        // Start the timer
        countDownTimer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get max time from intent
        maxCountdownTime = intent.getLongExtra("maxCountdownTime", 0);

        startTimer(maxCountdownTime);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
