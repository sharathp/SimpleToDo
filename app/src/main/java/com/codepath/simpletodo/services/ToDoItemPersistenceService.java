package com.codepath.simpletodo.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.util.Log;

import com.codepath.simpletodo.SimpleToDoApplication;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.repos.ToDoItemDAO;

import javax.inject.Inject;

public class ToDoItemPersistenceService extends IntentService {
    private static final String TAG = ToDoItemPersistenceService.class.getSimpleName();

    @Inject
    ToDoItemDAO mToDoItemDAO;

    public static final String EXTRA_TODO_ITEM = ToDoItemPersistenceService.class.getName() + ".ITEM";
    public static final String EXTRA_TODO_ITEM_ID = ToDoItemPersistenceService.class.getName() + ".ITEM_ID";
    public static final String EXTRA_TODO_ACTION = ToDoItemPersistenceService.class.getName() + ".ACTION";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ToDoItemPersistenceService() {
        super(ToDoItemPersistenceService.class.getSimpleName());
    }

    public static Intent createIntentToInsert(final Context context, final ToDoItem toDoItem) {
        final Intent intent = new Intent(context, ToDoItemPersistenceService.class);
        intent.putExtra(EXTRA_TODO_ACTION, ACTION_INSERT);
        intent.putExtra(EXTRA_TODO_ITEM, toDoItem);
        return intent;
    }

    public static Intent createIntentToUpdate(final Context context, final ToDoItem toDoItem) {
        final Intent intent = new Intent(context, ToDoItemPersistenceService.class);
        intent.putExtra(EXTRA_TODO_ACTION, ACTION_UPDATE);
        intent.putExtra(EXTRA_TODO_ITEM, toDoItem);
        return intent;
    }

    public static Intent createIntentToDelete(final Context context, final long toDoItemId) {
        final Intent intent = new Intent(context, ToDoItemPersistenceService.class);
        intent.putExtra(EXTRA_TODO_ACTION, ACTION_DELETE);
        intent.putExtra(EXTRA_TODO_ITEM_ID, toDoItemId);
        return intent;
    }

    public static Intent createIntentToDeleteAll(final Context context) {
        final Intent intent = new Intent(context, ToDoItemPersistenceService.class);
        intent.putExtra(EXTRA_TODO_ACTION, ACTION_DELETE_ALL);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SimpleToDoApplication.from(this).getComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        @Action final int action = intent.getIntExtra(EXTRA_TODO_ACTION, -1);

        switch (action) {
            case ACTION_INSERT: {
                insert(intent);
                break;
            }
            case ACTION_UPDATE: {
                update(intent);
                break;
            }
            case ACTION_DELETE: {
                delete(intent);
                break;
            }
            case ACTION_DELETE_ALL: {
                deleteAll();
                break;
            }
            default: {
                Log.w(TAG, "Unknown action: " + action);
            }
        }
    }

    private void insert(final Intent intent) {
        final ToDoItem toDoItem = intent.getParcelableExtra(EXTRA_TODO_ITEM);
        mToDoItemDAO.insert(toDoItem);
    }

    private void update(final Intent intent) {
        final ToDoItem toDoItem = intent.getParcelableExtra(EXTRA_TODO_ITEM);
        mToDoItemDAO.update(toDoItem);
    }

    private void delete(final Intent intent) {
        final long toDoItemId = intent.getLongExtra(EXTRA_TODO_ITEM_ID, -1);
        mToDoItemDAO.delete(toDoItemId);
    }

    private void deleteAll() {
        mToDoItemDAO.deleteAll();
    }

    public static final int ACTION_INSERT = 0;
    public static final int ACTION_UPDATE = 1;
    public static final int ACTION_DELETE = 2;
    public static final int ACTION_DELETE_ALL = 3;

    @IntDef({ACTION_INSERT, ACTION_UPDATE, ACTION_DELETE, ACTION_DELETE_ALL})
    public @interface Action {
        // no-op
    }
}