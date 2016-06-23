package com.codepath.simpletodo.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.codepath.simpletodo.services.ToDoItemNotificationService;

/**
 * Broadcast Receiver that wakes up to kick off {@link ToDoItemNotificationService}.
 */
public class TodayToDoNotificationReceiver extends WakefulBroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    public static Intent createIntent(final Context context) {
        return new Intent(context, TodayToDoNotificationReceiver.class);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final Intent startServiceIntent = ToDoItemNotificationService.createIntent(context);
        startWakefulService(context, startServiceIntent);
    }
}
