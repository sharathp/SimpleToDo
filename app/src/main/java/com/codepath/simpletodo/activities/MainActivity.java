package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.SimpleToDoApplication;
import com.codepath.simpletodo.adapters.ToDoItemAdapter;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.repos.ToDoItemDAO;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;
import com.codepath.simpletodo.views.DividerItemDecoration;
import com.yahoo.squidb.data.SquidCursor;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ToDoItemAdapter.ToDoItemClickListener,
        LoaderManager.LoaderCallbacks<SquidCursor<ToDoItem>> {

    private static final int REQUEST_CODE_EDIT_ACTIVITY = 1;
    private static final int LOADER_ID_TODO_ITEMS = 0;

    @Inject
    ToDoItemDAO mToDoItemDAO;

    @BindView(R.id.recycler_items)
    RecyclerView mItemsRecyclerView;

    @BindView(R.id.edit_new_item)
    EditText mNewItemEditText;

    private ToDoItemAdapter mToDoItemAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        SimpleToDoApplication.from(this).getComponent().inject(this);

        initViews();
        getSupportLoaderManager().initLoader(LOADER_ID_TODO_ITEMS, null, this);
    }

    @OnClick(R.id.btn_add_item)
    void onAddItem(final View view) {
        final String name = mNewItemEditText.getText().toString();
        final ToDoItem newItem = new ToDoItem();
        newItem.setName(name);

        final Intent insertIntent = ToDoItemPersistenceService.createIntentToInsert(this, newItem);
        startService(insertIntent);

        // reset
        mNewItemEditText.setText("");
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE_EDIT_ACTIVITY != requestCode) {
            return;
        }

        if (RESULT_OK == resultCode) {
            final ToDoItem toDoItem = EditItemActivity.getItem(data);
            if (toDoItem != null) {
                Toast.makeText(MainActivity.this, toDoItem.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(final ToDoItem toDoItem) {
        final Intent intent = EditItemActivity.createIntent(MainActivity.this, toDoItem);
        startActivityForResult(intent, REQUEST_CODE_EDIT_ACTIVITY);
    }

    @Override
    public void onLongClick(final ToDoItem toDoItem) {
        final Intent deleteIntent = ToDoItemPersistenceService.createIntentToDelete(this, toDoItem.getId());
        startService(deleteIntent);
    }

    @Override
    public Loader<SquidCursor<ToDoItem>> onCreateLoader(final int id, final Bundle args) {
        return mToDoItemDAO.getAllToDoItems();
    }

    @Override
    public void onLoadFinished(final Loader<SquidCursor<ToDoItem>> loader, final SquidCursor<ToDoItem> data) {
        mToDoItemAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<SquidCursor<ToDoItem>> loader) {
        mToDoItemAdapter.swapCursor(null);
    }

    private void initViews() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mToDoItemAdapter = new ToDoItemAdapter(this);
        mItemsRecyclerView.setLayoutManager(linearLayoutManager);
        mItemsRecyclerView.setAdapter(mToDoItemAdapter);
        mItemsRecyclerView.setHasFixedSize(true);
        mItemsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }
}