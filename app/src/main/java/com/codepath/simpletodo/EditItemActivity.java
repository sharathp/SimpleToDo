package com.codepath.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_TEXT = "EditItemActivity.ITEM_TEXT";
    public static final String EXTRA_ITEM_POSITION = "EditItemActivity.ITEM_POSITION";

    public static final int INVALID_ITEM_POSITION = -1;

    private EditText etItem;
    private Button btnSaveItem;
    private int itemPosition;

    public static Intent createIntent(Context context, String text, int position) {
        Intent intent = new Intent(context, EditItemActivity.class);
        intent.putExtra(EXTRA_ITEM_TEXT, text);
        intent.putExtra(EXTRA_ITEM_POSITION, position);
        return intent;
    }

    public static String getText(Intent data) {
        return data.getStringExtra(EXTRA_ITEM_TEXT);
    }

    public static int getPosition(Intent data) {
        return data.getIntExtra(EXTRA_ITEM_POSITION, INVALID_ITEM_POSITION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItem = (EditText) findViewById(R.id.etExistingItem);
        btnSaveItem = (Button) findViewById(R.id.btnSaveItem);

        itemPosition = getIntent().getIntExtra(EXTRA_ITEM_POSITION, INVALID_ITEM_POSITION);
        String text = getIntent().getStringExtra(EXTRA_ITEM_TEXT);
        etItem.setText(text);
        etItem.setSelection(etItem.length());
        etItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // no-op
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    btnSaveItem.setEnabled(false);
                } else {
                    btnSaveItem.setEnabled(true);
                }
            }
        });

        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveItem();
            }
        });
    }

    private void onSaveItem() {
        String text = etItem.getText().toString();

        Intent data = new Intent();
        data.putExtra(EXTRA_ITEM_TEXT, text);
        data.putExtra(EXTRA_ITEM_POSITION, itemPosition);

        setResult(RESULT_OK, data);
        finish();
    }
}
