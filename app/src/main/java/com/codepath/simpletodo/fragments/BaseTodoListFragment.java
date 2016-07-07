package com.codepath.simpletodo.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.SimpleToDoApplication;
import com.codepath.simpletodo.activities.EditItemActivity;
import com.codepath.simpletodo.adapters.ToDoItemAdapter;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.repos.ToDoItemDAO;
import com.codepath.simpletodo.views.DividerItemDecoration;
import com.yahoo.squidb.data.SquidCursor;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseTodoListFragment extends Fragment  implements ToDoItemAdapter.ToDoItemClickListener,
        LoaderManager.LoaderCallbacks<SquidCursor<ToDoItem>> {

    private static final int REQUEST_CODE_EDIT_ACTIVITY = 1;
    private static final int LOADER_ID_TODO_ITEMS = 0;

    @Inject
    ToDoItemDAO mToDoItemDAO;

    @BindView(R.id.rv_items)
    RecyclerView mItemsRecyclerView;

    @BindView(R.id.fab_new)
    FloatingActionButton mFabNewItem;

    @BindView(R.id.pb_item_loading)
    ProgressBar mLoadingProgressBar;

    @BindView(R.id.tv_no_todo_items)
    TextView mNoItemsTextView;

    private ToDoItemAdapter mToDoItemAdapter;
    private ToDoActionListener mToDoActionListener;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (! (context instanceof ToDoActionListener)) {
            throw new RuntimeException("activity should implement: " + ToDoActionListener.class.getName());
        }

        mToDoActionListener = (ToDoActionListener) context;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToDoItemDAO = SimpleToDoApplication.from(getActivity()).getComponent().getToDoItemDao();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, parent, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initViews();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDefaultToDoItems();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE_EDIT_ACTIVITY != requestCode) {
            return;
        }

        if (getActivity().RESULT_OK == resultCode) {
            final ToDoItem toDoItem = EditItemActivity.getItem(data);
            if (toDoItem != null) {
                Toast.makeText(getActivity(), toDoItem.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(final ToDoItem toDoItem) {
        mToDoActionListener.onEditToDo(toDoItem);
    }

    @Override
    public Loader<SquidCursor<ToDoItem>> onCreateLoader(final int id, final Bundle args) {
        // show loader
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mNoItemsTextView.setVisibility(View.INVISIBLE);
        mItemsRecyclerView.setVisibility(View.INVISIBLE);

        return doCreateToDoItemsLoader(id, args);
    }

    @Override
    public void onLoadFinished(final Loader<SquidCursor<ToDoItem>> loader, final SquidCursor<ToDoItem> data) {
        mToDoItemAdapter.swapCursor(data);
        // hide loader
        mLoadingProgressBar.setVisibility(View.INVISIBLE);

        if (data.getCount() > 0) {
            // show recycler view
            mNoItemsTextView.setVisibility(View.INVISIBLE);
            mItemsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            // show empty message
            mNoItemsTextView.setVisibility(View.VISIBLE);
            mItemsRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(final Loader<SquidCursor<ToDoItem>> loader) {
        mToDoItemAdapter.swapCursor(null);
    }

    private void initViews() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mToDoItemAdapter = new ToDoItemAdapter(this);
        mItemsRecyclerView.setLayoutManager(linearLayoutManager);
        mItemsRecyclerView.setAdapter(mToDoItemAdapter);
        mItemsRecyclerView.setHasFixedSize(true);
        mItemsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        if (isItemCreationSupported()) {
            mFabNewItem.setVisibility(View.VISIBLE);
            mFabNewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mToDoActionListener.onAddNewToDo();
                }
            });
        } else {
            mFabNewItem.setVisibility(View.INVISIBLE);
        }
    }

    protected abstract Loader<SquidCursor<ToDoItem>> doCreateToDoItemsLoader(final int id, final Bundle args);

    // not supported by default
    protected boolean isItemCreationSupported() {
        return false;
    }

    protected void loadDefaultToDoItems() {
        getLoaderManager().restartLoader(LOADER_ID_TODO_ITEMS, null, this);
    }

    protected void destroyDefaultToDoItems() {
        getLoaderManager().destroyLoader(LOADER_ID_TODO_ITEMS);
    }

    public interface ToDoActionListener {

        void onAddNewToDo();

        void onEditToDo(ToDoItem toDoItem);
    }
}
