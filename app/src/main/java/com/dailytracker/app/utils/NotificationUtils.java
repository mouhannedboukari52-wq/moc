package com.dailytracker.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationUtils {

    public static final String CHANNEL_FOREGROUND_ID = "daily_tracker_foreground";
    public static final String CHANNEL_REMINDERS_ID = "daily_tracker_reminders";

    public static void createChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = context.getSystemService(NotificationManager.class);

            NotificationChannel foreground = new NotificationChannel(
                    CHANNEL_FOREGROUND_ID,
                    "Daily Tracker Service",
                    NotificationManager.IMPORTANCE_LOW);
            foreground.setDescription("Keeps the Daily Tracker running in the background");

            NotificationChannel reminders = new NotificationChannel(
                    CHANNEL_REMINDERS_ID,
                    "Routine Reminders",
                    NotificationManager.IMPORTANCE_HIGH);
            reminders.setDescription("Reminds you about your daily routines");

            nm.createNotificationChannel(foreground);
            nm.createNotificationChannel(reminders);
        }
    }
}
