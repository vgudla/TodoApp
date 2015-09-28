package com.example.vgudla.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vgudla.todoapp.R;
import com.example.vgudla.todoapp.persistence.TodoTask;

import java.util.List;

/**
 * Custom adapter for displaying tasks
 */
public class CustomTodoAdapter extends ArrayAdapter<TodoTask> {
    public CustomTodoAdapter(Context context, List<TodoTask> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoTask task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.etNewTask);
        textView.setText(task.getTaskText());
        return convertView;
    }
}
