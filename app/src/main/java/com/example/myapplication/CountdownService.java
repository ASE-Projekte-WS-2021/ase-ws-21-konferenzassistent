package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class CountdownService extends Service {

    // Service Tag
    private final static String TAG = "CountdownService";

    public static final String COUNTDOWN_SERVICE = "com.example.myapplication.countdown_service";
    Intent bi = new Intent(COUNTDOWN_SERVICE);

    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a new Countdown Timer
        countDownTimer = new CountDownTimer(30000, 1000) {
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
    public void onDestroy() {
        // Stop the timer
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
