package com.example.vgudla.todoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EDITABLE = "editable";
    public static final String EDITABLE_POSITION = "editablePos";
    public static final int REQUEST_CODE = 1;
    List<String> items;
    ListView lvItems;
    ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lookup view and update
        readItems();
        lvItems = (ListView)findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupItemEditListener();
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
    }

    private void setupItemEditListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
                String editableValue = items.get(position);
                editIntent.putExtra(EDITABLE, editableValue);
                editIntent.putExtra(EDITABLE_POSITION, position);
                startActivityForResult(editIntent, REQUEST_CODE);
            }
        });
    }

    private void readItems() {
        File todoFile = getFileHandle("todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (Exception ex) {
            items = new ArrayList<>();
        }
    }

    @NonNull
    private File getFileHandle(String fileName) {
        File filesDir = getFilesDir();
        return new File(filesDir, fileName);
    }

    private void writeItems() {
        File todoFile = getFileHandle("todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedValue = data.getExtras().getString(EDITABLE);
            int editedPosition = data.getExtras().getInt(EDITABLE_POSITION);
            items.set(editedPosition, editedValue);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    public void onAddItem(View view) {
        EditText editText = (EditText)findViewById(R.id.etNewItem);
        String enteredText = editText.getText().toString();
        itemsAdapter.add(enteredText);
        editText.setText("");
        writeItems();
    }
}
