package com.example.vgudla.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.vgudla.todoapp.adapters.CustomTodoAdapter;
import com.example.vgudla.todoapp.persistence.DatabaseHelper;
import com.example.vgudla.todoapp.persistence.TodoTask;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TO_EDIT = "toEdit";
    public static final String EDITED_TEXT = "edited";
    public static final String EDITABLE_POSITION = "editablePos";
    public static final int REQUEST_CODE = 1;
    private List<TodoTask> items;
    private ListView lvItems;
    private ArrayAdapter<TodoTask> itemsAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lookup view and update
        databaseHelper = DatabaseHelper.getInstance(this);
        items = databaseHelper.getAllTasks();
        lvItems = (ListView)findViewById(R.id.lvItems);
        itemsAdapter = new CustomTodoAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupItemEditListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoTask removedTask = items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                databaseHelper.deleteTask(removedTask);
                return true;
            }
        });
    }

    private void setupItemEditListener() {
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
                TodoTask editableValue = items.get(position);
                editIntent.putExtra(TO_EDIT, editableValue.getTaskText());
                editIntent.putExtra(EDITABLE_POSITION, position);
                startActivityForResult(editIntent, REQUEST_CODE);
            }
        });
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
            String editedValue = data.getExtras().getString(EDITED_TEXT);
            String oldValue = data.getExtras().getString(TO_EDIT);
            int editedPosition = data.getExtras().getInt(EDITABLE_POSITION);
            TodoTask updatedTask = new TodoTask(editedValue);
            items.set(editedPosition, updatedTask);
            itemsAdapter.notifyDataSetChanged();
            databaseHelper.updateTask(oldValue, updatedTask);
        }
    }

    public void onAddItem(View view) {
        EditText editText = (EditText)findViewById(R.id.etNewItem);
        String enteredText = editText.getText().toString();
        TodoTask addedTask = new TodoTask(enteredText);
        itemsAdapter.add(addedTask);
        editText.setText("");
        databaseHelper.addTask(addedTask);
    }
}
