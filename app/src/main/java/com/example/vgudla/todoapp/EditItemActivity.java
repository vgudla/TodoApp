package com.example.vgudla.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    int editablePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String editableText = getIntent().getExtras().getString(MainActivity.EDITABLE);
        editablePosition = getIntent().getExtras().getInt(MainActivity.EDITABLE_POSITION);
        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText(editableText);
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
        editedIntent.putExtra(MainActivity.EDITABLE, editText.getText().toString());
        editedIntent.putExtra(MainActivity.EDITABLE_POSITION, editablePosition);
        setResult(RESULT_OK, editedIntent);
        this.finish();
    }
}
