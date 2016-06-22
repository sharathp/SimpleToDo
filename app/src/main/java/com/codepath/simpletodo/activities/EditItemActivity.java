package com.codepath.simpletodo.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.fragments.DatePickerFragment;
import com.codepath.simpletodo.fragments.PriorityPickerFragment;
import com.codepath.simpletodo.models.Priority;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;
import com.codepath.simpletodo.utils.DateUtils;
import com.codepath.simpletodo.views.HideKeyboardEditTextFocusChangeListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, PriorityPickerFragment.OnPrioritySetListener {
    public static final String EXTRA_ITEM = "EditItemActivity.ITEM";
    private static final String PATTERN_DATE = "dd-MMM-yyyy";

    @BindView(R.id.tie_item_name)
    TextInputEditText mItemNameEditText;

    @BindView(R.id.til_item_name)
    TextInputLayout mItemNameTextInputLayout;

    @BindView(R.id.tie_item_description)
    TextInputEditText mItemDescEditText;

    @BindView(R.id.tie_item_duedate)
    TextInputEditText mItemDueDateEditText;

    @BindView(R.id.tie_item_priority)
    TextInputEditText mItemPriorityEditText;

    @BindView(R.id.fab_save)
    FloatingActionButton mSaveButton;

    @BindView(R.id.cb_item_complete)
    CheckBox mCompletedCheckbox;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private HideKeyboardEditTextFocusChangeListener mHideKeyboardEditTextFocusChangeListener;

    private ToDoItem mToDoItem;

    public static Intent createIntent(final Context context, final ToDoItem toDoItem) {
        final Intent intent = new Intent(context, EditItemActivity.class);
        if (toDoItem != null) {
            intent.putExtra(EXTRA_ITEM, toDoItem);
        }
        return intent;
    }

    public static ToDoItem getItem(final Intent data) {
        return data.getParcelableExtra(EXTRA_ITEM);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToDoItem = getIntent().getParcelableExtra(EXTRA_ITEM);
        mHideKeyboardEditTextFocusChangeListener = new HideKeyboardEditTextFocusChangeListener();
        initViews();

        if (mToDoItem != null) {
            bindViews();
        } else {
            // new item
            mToDoItem = new ToDoItem();
            // default to today
            setDate(DateUtils.getToday());
            setPriority(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        // not a new item, so, hide delete
        if (! mToDoItem.isSaved()) {
            menu.findItem(R.id.action_delete).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            case R.id.action_delete: {
                deleteItem();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(final DatePicker view, final int year, final int month, final int day) {
        final GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        setDate(new Date(calendar.getTimeInMillis()));
    }

    @Override
    public void onPrioritySet(final int priority) {
        setPriority(priority);
    }

    private void bindViews() {
        mItemNameEditText.setText(mToDoItem.getName());

        if (! TextUtils.isEmpty(mToDoItem.getDescription())) {
            mItemDescEditText.setText(mToDoItem.getDescription());
        }

        setPriority(mToDoItem.getPriority());

        setDate(new Date(mToDoItem.getDueDate()));

        mCompletedCheckbox.setChecked(mToDoItem.isCompleted());
    }

    private void initViews() {
        mItemNameEditText.setOnFocusChangeListener(mHideKeyboardEditTextFocusChangeListener);
        mItemNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // no-op
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                // no-op
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (mItemNameTextInputLayout.isErrorEnabled() && !TextUtils.isEmpty(s.toString())) {
                    mItemNameTextInputLayout.setErrorEnabled(false);
                }
            }
        });

        mItemDescEditText.setOnFocusChangeListener(mHideKeyboardEditTextFocusChangeListener);

        mItemDueDateEditText.setInputType(InputType.TYPE_NULL);
        // this is required once the focus is obtained
        mItemDueDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        // this is required the first time focus is obtained
        mItemDueDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    showCalendar();
                }
            }
        });

        mItemPriorityEditText.setInputType(InputType.TYPE_NULL);
        // this is required once the focus is obtained
        mItemPriorityEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriorityPicker();
            }
        });

        // this is required the first time focus is obtained
        mItemPriorityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    showPriorityPicker();
                }
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveItem();
            }
        });
    }

    private void showCalendar() {
        final DatePickerFragment datePickerFragment = DatePickerFragment.createInstance(mToDoItem.getDueDate());
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showPriorityPicker() {
        final PriorityPickerFragment priorityPickerFragment = PriorityPickerFragment.createInstance(mToDoItem.getPriority());
        priorityPickerFragment.show(getSupportFragmentManager(), "priorityPicker");
    }

    private void onSaveItem() {
        final String updatedName = mItemNameEditText.getText().toString();
        if (TextUtils.isEmpty(updatedName)) {
            mItemNameTextInputLayout.setError("Name is required");
            return;
        }

        mToDoItem.setName(updatedName.trim());

        final String updatedDescription = mItemDescEditText.getText().toString();
        if (! TextUtils.isEmpty(updatedDescription)) {
            mToDoItem.setDescription(updatedDescription);
        }

        mToDoItem.setIsCompleted(mCompletedCheckbox.isChecked());

        final Intent updateIntent = ToDoItemPersistenceService.createIntentToUpdate(this, mToDoItem);
        startService(updateIntent);

        final Intent data = new Intent();
        data.putExtra(EXTRA_ITEM, mToDoItem);
        setResult(RESULT_OK, data);
        finish();
    }

    private void setDate(final Date date) {
        mItemDueDateEditText.setText(formatDate(date));
        mToDoItem.setDueDate(date.getTime());
    }

    private void setPriority(final int priority) {
        mItemPriorityEditText.setText(Priority.getPriorityByOrder(priority).name());
        mToDoItem.setPriority(priority);
    }

    private String formatDate(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE);
        return sdf.format(date);
    }

    private void deleteItem() {
        final Intent deleteIntent = ToDoItemPersistenceService.createIntentToDelete(this, mToDoItem.getId());
        startService(deleteIntent);
        this.finish();
    }
}