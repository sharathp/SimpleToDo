package com.codepath.simpletodo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codepath.simpletodo.receivers.TodayToDoNotificationReceiver;

import java.util.Calendar;

public class AlarmUtils {
    private static final String TAG = AlarmUtils.class.getSimpleName();

    public static void scheduleToDoNotificationAlarm(final Context context) {
        final AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        final Intent intent = TodayToDoNotificationReceiver.createIntent(context);
        // FLAG_UPDATE_CURRENT will prevent duplicates
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, TodayToDoNotificationReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        // Set the alarm to start at approximately 4:00 am everyday
        final Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(now.getTimeInMillis());
        alarmTime.set(Calendar.HOUR_OF_DAY, 4);
        alarmTime.set(Calendar.MINUTE, 0);

        // if it is already past 4 AM, schedule for tomorrow
        if (now.getTimeInMillis() >= alarmTime.getTimeInMillis()) {
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        // we don't need precise wake-up, hence inexact
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
    }
}