package com.codepath.simpletodo.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.codepath.simpletodo.SimpleToDoApplication;
import com.codepath.simpletodo.models.Priority;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.repos.ToDoItemDAO;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Intent service that checks ToDo items scheduled for today and sends a notification.
 */
public class ToDoItemNotificationService extends IntentService {
    private static final String TAG = ToDoItemNotificationService.class.getSimpleName();

    private ToDoItemDAO mToDoItemDAO;

    public ToDoItemNotificationService() {
        super(ToDoItemNotificationService.class.getSimpleName());
    }

    public static Intent createIntent(final Context context) {
        return new Intent(context, ToDoItemNotificationService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mToDoItemDAO = SimpleToDoApplication.from(this).getComponent().getToDoItemDao();
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.i(TAG, "Service running");

        try {
            checkAndSendNotifications();
        } catch (final Exception e) {
            Log.e(TAG, "Unexpected error encountered", e);
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void checkAndSendNotifications() {
        final SquidCursor<ToDoItem> cursor = mToDoItemDAO.getAllToDoItemsForToday();
        int high = 0, medium = 0, low = 0;
        try {
            if (cursor.getCount() == 0) {
                // no ToDo items for today, do nothing
                return;
            }

            ToDoItem item = new ToDoItem();
            while (cursor.moveToNext()) {
                item.readPropertiesFromCursor(cursor);
                if (Priority.HIGH.getOrder() == item.getPriority()) {
                    high++;
                } else if (Priority.MEDIUM.getOrder() == item.getPriority()) {
                    medium++;
                } else {
                    low++;
                }
            }
            sendNotification(high, medium, low);
        } finally {
            cursor.close();
        }

        sendNotification(high, medium, low);
    }

    private void sendNotification(final int high, final int medium, final int low) {
        // TODO - send notification
    }
}