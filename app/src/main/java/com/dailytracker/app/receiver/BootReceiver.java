package com.dailytracker.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.dailytracker.app.utils.SessionManager;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SessionManager session = new SessionManager(context);
            if (session.isLoggedIn()) {
                // Re-start foreground service after reboot
                Intent serviceIntent = new Intent(context,
                        com.dailytracker.app.service.ReminderForegroundService.class);
                ContextCompat.startForegroundService(context, serviceIntent);
            }
        }
    }
}
