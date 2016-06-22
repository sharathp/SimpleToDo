package com.codepath.simpletodo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.codepath.simpletodo.receivers.TodayToDoNotificationReceiver;

import java.util.Calendar;

public class AlarmUtils {

    public static void scheduleToDoNotificationAlarm(final Context context) {
        final AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        final Intent intent = TodayToDoNotificationReceiver.createIntent(context);
        // FLAG_UPDATE_CURRENT will prevent duplicates
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, TodayToDoNotificationReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to start at approximately 4:00 am everyday
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 4);

        // we don't need precise wake-up, hence inexact
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
    }
}