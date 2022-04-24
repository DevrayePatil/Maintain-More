package com.example.maintainmore;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String  CATEGORY_ID = "Invoice PDF";

    @Override
    public void onCreate() {
        super.onCreate();
        
        createNotificationCategory();
    }

    private void createNotificationCategory() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(
                    CATEGORY_ID,
                    "Invoice",
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationChannel.setDescription("This notification is used to display Downloaded Invoice");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
