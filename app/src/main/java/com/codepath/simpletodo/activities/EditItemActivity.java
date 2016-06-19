package com.codepath.simpletodo.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.Priority;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditItemActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM = "EditItemActivity.ITEM";

    @BindView(R.id.tie_item_name)
    TextInputEditText mItemNameEditText;

    @BindView(R.id.til_item_name)
    TextInputLayout mItemNameTextInputLayout;

    @BindView(R.id.tie_item_description)
    TextInputEditText mItemDescEditText;

    @BindView(R.id.acs_item_duedate)
    AppCompatSpinner mDueDateSpinner;

    @BindView(R.id.sb_item_priority)
    SeekBar mPrioritySeekBar;

    @BindView(R.id.btn_save_item)
    Button mSaveButton;

    @BindView(R.id.cb_item_complete)
    CheckBox mCompletedCheckbox;

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
        mToDoItem = getIntent().getParcelableExtra(EXTRA_ITEM);

        ButterKnife.bind(this);
        initViews();

        if (mToDoItem != null) {
            bindViews();
        } else {
            mToDoItem = new ToDoItem();
        }
    }

    private void bindViews() {
        mItemNameEditText.setText(mToDoItem.getName());
        if (! TextUtils.isEmpty(mToDoItem.getDescription())) {
            mItemDescEditText.setText(mToDoItem.getDescription());
        }
        mPrioritySeekBar.setProgress(mToDoItem.getPriority().getOrder());

        // TODO - bind date

        mCompletedCheckbox.setChecked(mToDoItem.isCompleted());
    }

    private void initViews() {
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

        // set color based on its default value
        setPrioritySeekBarColor(mPrioritySeekBar, mPrioritySeekBar.getProgress());

        mPrioritySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                setPrioritySeekBarColor(seekBar, progress);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
                // no-op
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                // no-op
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveItem();
            }
        });
    }

    private void setPrioritySeekBarColor(final SeekBar seekBar, final int progress) {
        // default low
        int colorResId = R.color.priority_low;
        switch(progress) {
            case 1: {
                colorResId = R.color.priority_medium;
                break;
            }
            case 2: {
                colorResId = R.color.priority_high;
                break;
            }
        }

        final int color = getResources().getColor(colorResId);
        seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    private void onSaveItem() {
        final String updatedName = mItemNameEditText.getText().toString();
        if (TextUtils.isEmpty(updatedName)) {
            mItemNameTextInputLayout.setError("Name is required");
            return;
        }

        mToDoItem.setName(updatedName.trim());
        mToDoItem.setPriority(Priority.getPriorityByOrder(mPrioritySeekBar.getProgress()));

        final String updatedDescription = mItemNameEditText.getText().toString();
        if (! TextUtils.isEmpty(updatedDescription)) {
            mToDoItem.setDescription(updatedDescription);
        }

        // TODO - set the date based on the selected date
        mToDoItem.setDueDate(1L);

        mToDoItem.setIsCompleted(mCompletedCheckbox.isChecked());

        final Intent updateIntent = ToDoItemPersistenceService.createIntentToUpdate(this, mToDoItem);
        startService(updateIntent);

        final Intent data = new Intent();
        data.putExtra(EXTRA_ITEM, mToDoItem);
        setResult(RESULT_OK, data);
        finish();
    }
}