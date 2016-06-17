package com.codepath.simpletodo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.services.ToDoItemPersistenceService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditItemActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM = "EditItemActivity.ITEM";

    @BindView(R.id.edit_item_name)
    EditText mItemNameEditText;

    @BindView(R.id.btn_save_item)
    Button mSaveButton;

    private ToDoItem mToDoItem;

    public static Intent createIntent(final Context context, final ToDoItem toDoItem) {
        final Intent intent = new Intent(context, EditItemActivity.class);
        intent.putExtra(EXTRA_ITEM, toDoItem);
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
    }

    private void initViews() {
        mItemNameEditText.setText(mToDoItem.getName());
        mItemNameEditText.setSelection(mToDoItem.getName().length());
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
                if (s.toString().trim().isEmpty()) {
                    mSaveButton.setEnabled(false);
                } else {
                    mSaveButton.setEnabled(true);
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

    private void onSaveItem() {
        final String updatedName = mItemNameEditText.getText().toString();
        mToDoItem.setName(updatedName);

        final Intent updateIntent = ToDoItemPersistenceService.createIntentToUpdate(this, mToDoItem);
        startService(updateIntent);

        final Intent data = new Intent();
        data.putExtra(EXTRA_ITEM, mToDoItem);
        setResult(RESULT_OK, data);
        finish();
    }
}