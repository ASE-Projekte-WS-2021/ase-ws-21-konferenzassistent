package com.example.myapplication;

import static com.example.myapplication.App.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;

public class CountdownService extends Service {

    // Service Tag
    private final static String TAG = "CountdownService";
    public static final String COUNTDOWN_SERVICE = "com.example.myapplication.countdown_service";

    private long maxCountdownTime;
    private long lueftungsCountdown;
    private boolean isOpen = false;
    private long currentTime;
    private boolean timerDone = false;

    private Intent bi = new Intent(COUNTDOWN_SERVICE);
    private CountDownTimer countDownTimer = null;

    // Media Player for audible alerts
    private MediaPlayer mp = new MediaPlayer();

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

        if(mp.isPlaying()){
            mp.stop();
        }
        unregisterReceiver(br);
        super.onDestroy();
    }

    private void startTimer(long maxTime){
        // Create a new Countdown Timer
        countDownTimer = new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long milliSUnitlFinished) {

                currentTime = milliSUnitlFinished;
                timerDone = false;

                // Broadcast the countdown and status of the window
                bi.putExtra("countdown", milliSUnitlFinished);
                bi.putExtra("windowOpen", isOpen);
                bi.putExtra("timerDone", false);
                // Broadcast Timer
                sendBroadcast(bi);
                notifyNotification(NotificationTextBuilder());
            }

            @Override
            public void onFinish() {
                // Timer finished
                // Restart timer with Window Open/Closed
                bi.putExtra("timerDone", true);

                timerDone = true;
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

    private void notifyNotification(String text) {

        Intent notificationIntent = new Intent(this, CountdownActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Konferenzassistent")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, notification);

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
                0, notificationIntent, 0);

        // build the Notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Konferenzassistent")
                .setContentText(NotificationTextBuilder())
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
            // if app got resumed send timer data
            else{
                bi.putExtra("countdown", currentTime);
                bi.putExtra("windowOpen", isOpen);
                bi.putExtra("timerDone", timerDone);

                sendBroadcast(bi);
            }

        }
    };

    private String NotificationTextBuilder(){
        String notificationText = "";

        // Convert to minutes and seconds
        int minutes = (int) currentTime/60000;
        int seconds = (int) currentTime%60000/1000;

        notificationText = "Noch " + minutes;
        notificationText += ":";
        // Add a leading 0 to seconds
        if(seconds < 10) notificationText += "0";
        notificationText += seconds;

        // Add the description
        if(isOpen) {
            notificationText += " bis zum schließen des Fensters!";
        }
        else{
            notificationText += " bis zum öffnen des Fensters!";
        }

        return notificationText;
    }

}
