package com.example.myapplication;

import static com.example.myapplication.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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
        // Register Receiver to listen for button presses
        registerReceiver(br, new IntentFilter(CountdownActivity.COUNTDOWN_BUTTONS));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // Stop the timer
        countDownTimer.cancel();
        unregisterReceiver(br);
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

        Intent notificationIntent = new Intent(this, CountdownActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, 0);

        // build the Notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Konferenzassistent")
                .setContentText("timeText")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        // Start as Foreground Service
        startForeground(1, notification);

        return START_NOT_STICKY;
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
    };

}
