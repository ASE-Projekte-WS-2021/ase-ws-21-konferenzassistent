package com.example.myapplication;

import static com.example.myapplication.App.CHANNEL_ID;
import static com.example.myapplication.CountdownActivity.COUNTDOWN_OBJECTS;
import static com.example.myapplication.CountdownActivity.PAUSE_BUTTON_PRESSED_ID;
import static com.example.myapplication.CountdownActivity.REPLAY_BUTTON_PRESSED_ID;

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

import com.example.myapplication.meetingwizard.cdServiceObject;

import java.util.ArrayList;
import java.util.Timer;

public class CountdownService extends Service {

    // Service Tag
    private final static String TAG = "CountdownService";
    public static final String COUNTDOWN_SERVICE = "com.example.myapplication.countdown_service";

    // Countdown Object that save the current states of the Countdowns
    ArrayList<cdServiceObject> countdownServiceObjects;

    private final Intent bi = new Intent(COUNTDOWN_SERVICE);

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
        // Stop all timers
        countdownServiceObjects.forEach(object ->{
            object.getCountDownTimer().cancel();
        });

        // Stop the Media Player if it still is playing
        if(mp.isPlaying()){
            mp.stop();
        }

        unregisterReceiver(br);
        super.onDestroy();
    }

    // Start a Countdown Timer and returns the CountdownTimer object
    private void startTimer(cdServiceObject cdObject, long maxTime){
        // Create a new Countdown Timer
        cdObject.setCountDownTimer(new CountDownTimer(maxTime, 1000) {
            @Override
            public void onTick(long milliSUnitlFinished) {

                // set Values
                cdObject.setCurrentTime(milliSUnitlFinished);
                cdObject.setTimerDone(false);

                // send Notification
                bi.putExtra(COUNTDOWN_OBJECTS, countdownServiceObjects);
                sendBroadcast(bi);
                notifyNotification(NotificationTextBuilder());
            }

            @Override
            public void onFinish() {
                // Timer finished
                // Restart timer with Window Open/Closed
                cdObject.setTimerDone(true);

                sendBroadcast(bi);
                // activate the alert
                Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if(!mp.isPlaying()){
                    mp = MediaPlayer.create(getApplicationContext(), alert);
                    mp.start();
                }

            }

        });

        // Start the timer
        cdObject.getCountDownTimer().start();
    }

    // Pauses the Timer
    private void pauseTimer(CountDownTimer timer){
        timer.cancel();
    }

    // Resumes the Timer

    private void resumeTimer(cdServiceObject cdObject){
        startTimer(cdObject, cdObject.getCurrentTime());
    }

    // Sends a Notification to the user
    // TODO: FIX BUGS
    private void notifyNotification(String text) {
        // Create a new spannable string
        SpannableString htmlText = new SpannableString(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));

        Intent notificationIntent = new Intent(this, CountdownActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

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
        // Get extra
        countdownServiceObjects = (ArrayList<cdServiceObject>)
                intent.getSerializableExtra(COUNTDOWN_OBJECTS);

        // Start the timers
        startTimers();

        Intent notificationIntent = new Intent(this, CountdownActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

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
            Integer id = intent.getIntExtra(PAUSE_BUTTON_PRESSED_ID, -1);

            if(id != -1){
                cdServiceObject timer = countdownServiceObjects.get(id);
                timer.setTimerRunning(!timer.getTimerRunning());

                if(!timer.getTimerRunning()){
                    pauseTimer(timer.getCountDownTimer());
                }else{
                    resumeTimer(timer);
                }
                sendBroadcast(bi);
            }

            Integer restart_id = intent.getIntExtra(REPLAY_BUTTON_PRESSED_ID, -1);
            Log.i(TAG, "onReceive: " + restart_id);

            if(restart_id != -1){
                cdServiceObject cdObject = countdownServiceObjects.get(restart_id);
                if(cdObject.getCountdownPosition() < cdObject.getTimer().getmItems().size() -1){
                    cdObject.setCountdownPosition(cdObject.getCountdownPosition()+1);
                }
                else{
                    cdObject.setCountdownPosition(0);
                }

                startSpecificTimer(restart_id);
                }
            }
            /*
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

             */

    };

    /*
    private void toggleLueftungsCountdown(){
        if(lueftungsCountdownRunning){
            lueftungsCountdownRunning = false;
            pauseTimer(lueftungsCountdown);
            return;
        }
        lueftungsCountdown = resumeTimer(lueftungsCountdown, lueftungsObject);
        lueftungsCountdownRunning = true;
    }


*/

    // Builds the Notification Text
    private String NotificationTextBuilder(){
        String notificationText = "Timer sind aktiv";
        /*
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
        */


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

    private void startSpecificTimer(Integer id){
        cdServiceObject object = countdownServiceObjects.get(id);
        startTimer(object, object.getTimer().getmItems().get(object.getCountdownPosition()).getSubCountdown() * 60000);
        object.setTimerRunning(true);
    }

    private void startTimers(){
        // For every Countdow do the same
        countdownServiceObjects.forEach(object ->{
            startTimer(object, object.getTimer().getmItems().get(object.getCountdownPosition()).getSubCountdown() * 60000);
            object.setTimerRunning(true);

        });
        Log.i(TAG, "startTimers: ");

        // create a CountDownObject for lueftung
        /*

        lueftungsObject.timerDone = false;
        lueftungsObject.currentTime = maxLueftungsTime;

        // start timer with countdown timer
        lueftungsCountdown = startTimer(startingTimer, lueftungsCountdown, lueftungsObject);
         */
    }


}


