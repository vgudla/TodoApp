package com.example.vgudla.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.vgudla.todoapp.persistence.TodoTask;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    TodoTask editableTask;
    TodoTask editedTask;
    EditText dateEditableText;
    int editablePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editableTask = (TodoTask)getIntent().getExtras().getSerializable(MainActivity.TO_EDIT);
        editedTask = new TodoTask(editableTask);
        editablePosition = getIntent().getExtras().getInt(MainActivity.EDITABLE_POSITION);
        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText(editableTask.getTaskText());
        dateEditableText = (EditText)findViewById(R.id.dateText);
        dateEditableText.setText(editableTask.getTaskCompletionDate());
        dateEditableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        showPrioritySpinner();
    }

    private void showPrioritySpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<TodoTask.Priority> adapter = new ArrayAdapter<>(this,
                                                        R.layout.spinner_layout,
                                                        Arrays.asList(TodoTask.Priority.values()));
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(adapter);
        spinner.setSelection(TodoTask.Priority.valueOf(editableTask.getTaskPriority()).ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TodoTask.Priority taskPriority = (TodoTask.Priority) parent.getItemAtPosition(position);
                editedTask.setTaskPriority(taskPriority);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onEditedItemSave(View view) {
        Intent editedIntent = new Intent();
        EditText editText = (EditText)findViewById(R.id.editText);
        editedTask.setTaskText(editText.getText().toString());
        editedIntent.putExtra(MainActivity.EDITED_TEXT, editedTask);
        editedIntent.putExtra(MainActivity.TO_EDIT, editableTask);
        editedIntent.putExtra(MainActivity.EDITABLE_POSITION, editablePosition);
        setResult(RESULT_OK, editedIntent);
        this.finish();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //month is indexed from 0 in Calendar TODO: switch to jodatime
            TodoTask editedTask = ((EditItemActivity) getActivity()).editedTask;
            editedTask.setTaskCompletionDate(new DateTime(year, month + 1, day, 0, 0));
            EditText dateEditableText = ((EditItemActivity) getActivity()).dateEditableText;
            dateEditableText.setText(editedTask.getTaskCompletionDate());
        }
    }
}
