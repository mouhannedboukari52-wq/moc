package com.dailytracker.app.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.dailytracker.app.R;
import com.dailytracker.app.utils.NotificationUtils;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String EXTRA_ROUTINE_TITLE = "routine_title";
    public static final String EXTRA_ROUTINE_ID = "routine_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils.createChannels(context);

        String title = intent.getStringExtra(EXTRA_ROUTINE_TITLE);
        String routineId = intent.getStringExtra(EXTRA_ROUTINE_ID);

        int notificationId = (routineId != null) ? routineId.hashCode() : (int) System.currentTimeMillis();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationUtils.CHANNEL_REMINDERS_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Routine Reminder")
                .setContentText("Time for: " + title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notificationId, builder.build());
    }
}
