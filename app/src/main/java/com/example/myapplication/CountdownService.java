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
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;

public class CountdownService extends Service {

    // Service Tag
    private final static String TAG = "CountdownService";
    public static final String COUNTDOWN_SERVICE = "com.example.myapplication.countdown_service";

    // Max Timer values
    private long maxlueftenTime;
    private long maxLueftungsTime;
    private long maxAbstandsTime;

    // if window got already opned
    private boolean isOpen = false;

    // Countdown Object that save the current states of the Countdowns
    private CountDownObject lueftungsObject;
    private CountDownObject abstandsObject;

    private Intent bi = new Intent(COUNTDOWN_SERVICE);

    // Countdown Timers
    private CountDownTimer lueftungsCountdown = null;
    private CountDownTimer abstandsCountdown = null;

    // Media Player for audible alerts
    private MediaPlayer mp = new MediaPlayer();

    // switches in Main Activity
    private Boolean lueftungsSwitchStatus;
    private Boolean abstandsSwitchStatus;

    private Boolean abstandsCountdownRunning = false;
    private Boolean lueftungsCountdownRunning = false;

    // Object to create a Countdown
    static class CountDownObject{
        long currentTime;
        boolean timerDone;

        public void createNotification(){

        }
    }

    @Override
    public void onCreate() {
        // Register Receiver to listen for button presses
        registerReceiver(br, new IntentFilter(CountdownActivity.COUNTDOWN_BUTTONS));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // Stop the timer
        if(lueftungsSwitchStatus)lueftungsCountdown.cancel();
        if(abstandsSwitchStatus)abstandsCountdown.cancel();

        if(mp.isPlaying()){
            mp.stop();
        }
        unregisterReceiver(br);
        super.onDestroy();
    }

    // Start a Countdown Timer and returns the CountdownTimer object
    private CountDownTimer startTimer(long maxTime, CountDownTimer timer, CountDownObject cdObject){
        // Create a new Countdown Timer
        timer = new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long milliSUnitlFinished) {

                // set Values
                cdObject.currentTime = milliSUnitlFinished;
                cdObject.timerDone = false;

                // send Notification
                cdObject.createNotification();
            }

            @Override
            public void onFinish() {
                // Timer finished
                // Restart timer with Window Open/Closed
                cdObject.timerDone = true;

                // Broadcast timer done
                cdObject.createNotification();

                // activate the alert
                Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if(!mp.isPlaying()){
                    mp = MediaPlayer.create(getApplicationContext(), alert);
                    mp.start();
                }

            }

        };
        // Start the timer
        timer.start();
        // return the timer so it isn´t null anymore
        return timer;
    }

    private void pauseTimer(CountDownTimer timer){
        timer.cancel();
    }

    private CountDownTimer resumeTimer(CountDownTimer timer, CountDownObject cdObject){
        return startTimer(cdObject.currentTime, timer, cdObject);
    }


    // Sends a Notification to the user
    private void notifyNotification(String text) {
        // Create a new spannable string
        SpannableString htmlText = new SpannableString(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));

        Intent notificationIntent = new Intent(this, CountdownActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Konferenzassistent")
                .setContentText(htmlText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(htmlText).setBigContentTitle("Konferenzassistent"))
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get max time from intent
        maxLueftungsTime = intent.getLongExtra("maxCountdownTime", 0);
        maxlueftenTime = intent.getLongExtra("maxLueftungsTimer", 0);
        maxAbstandsTime = intent.getLongExtra("maxAbstandsTimer", 0);


        lueftungsSwitchStatus = intent.getBooleanExtra("lueftungsSwitchStatus", false);
        abstandsSwitchStatus = intent.getBooleanExtra("abstandsSwitchStatus", false);

        // Start the timers
        if(lueftungsSwitchStatus){
            StartLueftungsTimer(maxLueftungsTime);
            lueftungsCountdownRunning = true;
        }

        if(abstandsSwitchStatus){
            StartAbstandsTimer(maxAbstandsTime);
            abstandsCountdownRunning = true;
        }

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
            boolean lueftungsUserInteraction = intent.getBooleanExtra("lueftungsUserInteraction", false);
            boolean abstandsUserInteraction = intent.getBooleanExtra("abstandsUserInteraction", false);

            boolean lueftungsPauseUserInteraction = intent.getBooleanExtra("lueftungsPauseUserInteraction", false);
            boolean abstandsPauseUserInteraction = intent.getBooleanExtra("abstandsPauseUserInteraction", false);

            //TODO: switch to a switch

            // Check if its a user Interaction and what button got pressed or if the app got resumed
            if(lueftungsUserInteraction){
                // Stop the alert
                if(mp.isPlaying())mp.stop();

                  // If open set to closed and restart timer
                  if(isOpen){
                      StartLueftungsTimer(maxLueftungsTime);
                      isOpen = false;
                  }
                  else{
                      StartLueftungsTimer(maxlueftenTime);
                      isOpen = true;
                  }
            }
            else if(abstandsUserInteraction) {
                // Stop the alert
                if (mp.isPlaying()) mp.stop();

                StartAbstandsTimer(maxAbstandsTime);
            }
            else if(lueftungsPauseUserInteraction){
                toggleLueftungsCountdown();

            }
            else if(abstandsPauseUserInteraction){
                toggleAbstandsCountdown();
            }
            // if app got resumed send timer data
            else{
                // Data for Lueftung
                bi.putExtra("lueftungsMilliS", lueftungsObject.currentTime);
                bi.putExtra("windowOpen", isOpen);
                bi.putExtra("lueftungDone", lueftungsObject.timerDone);

                // Data for Abstand
                bi.putExtra("abstandsMilliS", abstandsObject.currentTime);
                bi.putExtra("abstandDone", abstandsObject.timerDone);

                sendBroadcast(bi);
            }
        }
    };

    private void toggleLueftungsCountdown(){
        if(lueftungsCountdownRunning){
            lueftungsCountdownRunning = false;
            pauseTimer(lueftungsCountdown);
            return;
        }
        lueftungsCountdown = resumeTimer(lueftungsCountdown, lueftungsObject);
        lueftungsCountdownRunning = true;
    }

    private void toggleAbstandsCountdown(){
        if(abstandsCountdownRunning){
            abstandsCountdownRunning = false;
            pauseTimer(abstandsCountdown);
            return;
        }
        abstandsCountdown = resumeTimer(abstandsCountdown, abstandsObject);
        abstandsCountdownRunning = true;
    }

    // Builds the Notification Text
    private String NotificationTextBuilder(){
        String notificationText = "";

        // If "Lueftung" is activated
        if(lueftungsSwitchStatus){
            // add the lueftungstimer as text
            notificationText += LongToStringForTime(lueftungsObject.currentTime);

            // Add the description
            if(isOpen) {
                notificationText += " bis zum schließen des Fensters!";
            }
            else{
                notificationText += " bis zum öffnen des Fensters!";
            }
        }
        // IF "Abstand" is activated
        if(abstandsSwitchStatus){
            // add a line break
            notificationText += "<br>";

            // add the abstands timer as text
            notificationText += LongToStringForTime(abstandsObject.currentTime);
            notificationText += " bis zum nächsten Abstands check!";
        }

        if(!abstandsSwitchStatus && !lueftungsSwitchStatus){
            notificationText += "Keine Timer ausgewählt!";
        }

        return notificationText;
    }

    // returns the Long time as a String in minutes and seconds
    private String LongToStringForTime(long time){
        String text = "";

        // Convert to minutes and seconds Lueftung
        int minutes = (int) time/60000;
        int seconds = (int) time%60000/1000;

        text += "Noch " + minutes;
        text += ":";
        // Add a leading 0 to seconds
        if(seconds < 10) text += "0";
        text += seconds;

        return text;
    }

    // Starts the lueftungstimer
    private void StartLueftungsTimer(long startingTimer){
        // create a CountDownObject for lueftung
        lueftungsObject  = new CountDownObject(){
            @Override
            public void createNotification(){
                bi.putExtra("lueftungsMilliS", this.currentTime);
                bi.putExtra("windowOpen", isOpen);
                bi.putExtra("lueftungDone", this.timerDone);
                // Broadcast Timer
                sendBroadcast(bi);
                notifyNotification(NotificationTextBuilder());
            }
        };

        lueftungsObject.timerDone = false;
        lueftungsObject.currentTime = maxLueftungsTime;

        // start timer with countdown timer
        lueftungsCountdown = startTimer(startingTimer, lueftungsCountdown, lueftungsObject);
    }

    // Starts the abstandstimer
    private void StartAbstandsTimer(long startingTimer){
        // create a CountDownObject for lueftung
        abstandsObject  = new CountDownObject(){
            @Override
            public void createNotification(){
                bi.putExtra("abstandsMilliS", this.currentTime);
                bi.putExtra("abstandDone", this.timerDone);
                // Broadcast Timer
                sendBroadcast(bi);
                notifyNotification(NotificationTextBuilder());
            }
        };

        abstandsObject.timerDone = false;
        abstandsObject.currentTime = maxLueftungsTime;

        // start timer with countdown timer
        abstandsCountdown = startTimer(startingTimer, abstandsCountdown, abstandsObject);
    }

}


