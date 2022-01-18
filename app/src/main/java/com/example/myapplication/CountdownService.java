package com.example.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
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

    // Media Player for audible alerts
    private MediaPlayer mp;

    @Override
    public void onCreate() {
        // Register Receiver to listen for button presses
        registerReceiver(br, new IntentFilter(CountdownActivity.COUNTDOWN_BUTTONS));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // Stop the timer
        countDownTimer.cancel();
        unregisterReceiver(br);
        mp.stop();
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
                bi.putExtra("timerDone", false);
                // Broadcast Timer
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                // Timer finished
                // Restart timer with Window Open/Closed
                bi.putExtra("timerDone", true);

                // Broadcast timer done
                sendBroadcast(bi);
                countDownTimer.cancel();

                // activate the alert
                Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                mp = MediaPlayer.create(getApplicationContext(), alert);
                mp.start();
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

    // Receives the button press on the Countdown activity
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
            // get if its a button Press
            boolean userInteraction = intent.getBooleanExtra("userInteraction", true);

            // Check if its a user Interaction or if the app got resumed
            if(userInteraction){
                // Stop the alert
                mp.stop();

                // If open set to closed and restart timer
                if(isOpen){
                    startTimer(maxCountdownTime);
                    isOpen = false;
                }
                else{
                    startTimer(lueftungsCountdown);
                    isOpen = true;
                }
            }
        }
    };

}
