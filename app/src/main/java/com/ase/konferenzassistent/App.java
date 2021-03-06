package com.ase.konferenzassistent;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "konferenzServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    // creates a Notification Channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Konferenz Service Channel",
                    NotificationManager.IMPORTANCE_LOW // Set to high for Notification messages
            );

            NotificationManager manger = getSystemService(NotificationManager.class);
            manger.createNotificationChannel(serviceChannel);
        }
    }
}
