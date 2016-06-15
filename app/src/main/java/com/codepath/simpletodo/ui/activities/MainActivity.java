package com.codepath.simpletodo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.db.SimpleToDoOpenHelper;
import com.codepath.simpletodo.db.models.TodoItem;
import com.codepath.simpletodo.ui.views.ToDoItemView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_EDIT_ACTIVITY = 1;

    @Inject
    SimpleToDoOpenHelper mSimpleToDoOpenHelper;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lv_items);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = EditItemActivity.createIntent(MainActivity.this, items.get(position), position);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE_EDIT_ACTIVITY != requestCode) {
            return;
        }

        if (RESULT_OK == resultCode) {
            String text = EditItemActivity.getText(data);
            int position = EditItemActivity.getPosition(data);

            if (text != null && EditItemActivity.INVALID_ITEM_POSITION != position) {
                items.set(position, text);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch(IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    static final class ToDoItemAdapter extends CursorAdapter {

        public ToDoItemAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return new ToDoItemView(context);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((ToDoItemView) view).setItem(TodoItem.MAPPER.map(cursor));
        }
    }
}