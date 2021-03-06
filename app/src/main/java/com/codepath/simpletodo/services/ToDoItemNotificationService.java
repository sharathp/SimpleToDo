package com.codepath.simpletodo.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.SimpleToDoApplication;
import com.codepath.simpletodo.activities.MainActivity;
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

            while (cursor.moveToNext()) {
                final int priority = cursor.get(ToDoItem.PRIORITY);
                if (Priority.HIGH.getOrder() == priority) {
                    high++;
                } else if (Priority.MEDIUM.getOrder() == priority) {
                    medium++;
                } else {
                    low++;
                }
            }
        } finally {
            cursor.close();
        }

        sendNotification(high, medium, low);
    }

    private void sendNotification(final int high, final int medium, final int low) {
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final int requestID = (int) System.currentTimeMillis();
        final Intent intent = MainActivity.createIntent(this);
        final PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, 0);

        final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(String.format(getString(R.string.notif_big_content_title), (high + medium + low)));
        addNotificationEntry(inboxStyle, R.string.notif_content_high, high);
        addNotificationEntry(inboxStyle, R.string.notif_content_medium, medium);
        addNotificationEntry(inboxStyle, R.string.notif_content_low, low);

        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_alarm_black)
                        .setContentTitle(getString(R.string.notif_title))
                        .setStyle(inboxStyle)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);

        mNotificationManager.notify(0, builder.build());
    }

    private void addNotificationEntry(final NotificationCompat.InboxStyle inboxStyle, @StringRes int priorityStringId, int count) {
        if (count > 0) {
            inboxStyle.addLine(String.format(getString(priorityStringId), count));
        }
    }
}