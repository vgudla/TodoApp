package com.example.vgudla.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vgudla.todoapp.R;
import com.example.vgudla.todoapp.persistence.TodoTask;

import java.util.List;

/**
 * Custom adapter for displaying tasks
 */
public class TodoAdapter extends ArrayAdapter<TodoTask> {

    private static class ViewHolder {
        TextView todoText;
        TextView todoCompletionDate;
        TextView todoPriority;
    }

    public TodoAdapter(Context context, List<TodoTask> tasks) {
        super(context, R.layout.item_task, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoTask task = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            viewHolder.todoText = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.todoPriority = (TextView) convertView.findViewById(R.id.tvTaskPriority);
            viewHolder.todoCompletionDate = (TextView) convertView.findViewById(R.id.tvTaskCompletionDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.todoText.setText(task.getTaskText());
        viewHolder.todoPriority.setText(task.getTaskPriority());
        viewHolder.todoCompletionDate.setText(task.getTaskCompletionDate());
        return convertView;
    }
}
