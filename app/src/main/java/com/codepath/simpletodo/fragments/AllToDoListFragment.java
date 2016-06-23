package com.codepath.simpletodo.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Fragment that displays all the ToDo items.
 */
public class AllToDoListFragment extends BaseTodoListFragment {

    public static AllToDoListFragment createInstance() {
        return new AllToDoListFragment();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_all, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all: {
                showDeleteAllAlert();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader() {
        return mToDoItemDAO.getAllToDoItems();
    }

    @Override
    protected boolean isItemCreationSupported() {
        return true;
    }

    private void showDeleteAllAlert() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.alert_delete_all_title);
        alertDialogBuilder.setMessage(R.string.alert_delete_all_message);
        alertDialogBuilder.setPositiveButton(R.string.alert_positive,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAll();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.alert_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    private void deleteAll() {
        final Intent deleteAllIntent = ToDoItemPersistenceService.createIntentToDeleteAll(getActivity());
        getActivity().startService(deleteAllIntent);
    }
}