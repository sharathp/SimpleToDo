package com.codepath.simpletodo.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;
import com.yahoo.squidb.data.SquidCursor;

/**
 * Fragment that displays all the ToDo items.
 */
public class AllToDoListFragment extends BaseTodoListFragment {
    private static final int LOADER_ID_SEARCH_TODO_ITEMS = 2;
    private static final String ARG_SEARCH_TEXT = "AllToDoListFragment.SEARCH_TEXT";

    private SearchView mSearchView;

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
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                // destroy currently running default loader
                destroyDefaultToDoItems();

                final Bundle args = new Bundle();
                args.putString(ARG_SEARCH_TEXT, query);
                getLoaderManager().restartLoader(LOADER_ID_SEARCH_TODO_ITEMS, args, AllToDoListFragment.this);
                Toast.makeText(getActivity(), "Search invoked: " + query, Toast.LENGTH_SHORT).show();
                mSearchView.clearFocus();
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getLoaderManager().destroyLoader(LOADER_ID_SEARCH_TODO_ITEMS);

                loadDefaultToDoItems();
                return false;
            }
        });

        mSearchView.clearFocus();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all: {
                showDeleteAllAlert();
                return true;
            }
            case R.id.action_delete_completed: {
                deleteCompleted();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSearchView != null && mSearchView.hasFocus()) {
            mSearchView.clearFocus();
        }
    }

    @Override
    protected Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader(final int id, final Bundle args) {
        switch (id) {
            case LOADER_ID_SEARCH_TODO_ITEMS: {
                return mToDoItemDAO.getMatchingToDoItems(args.getString(ARG_SEARCH_TEXT));
            }
            default: {
                return mToDoItemDAO.getAllToDoItems();
            }
        }
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

    private void deleteCompleted() {
        final Intent deleteAllIntent = ToDoItemPersistenceService.createIntentToDeleteCompleted(getActivity());
        getActivity().startService(deleteAllIntent);
    }
}