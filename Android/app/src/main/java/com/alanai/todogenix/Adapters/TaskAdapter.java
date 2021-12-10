package com.alanai.todogenix.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alanai.todogenix.Models.Task;
import com.alanai.todogenix.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.tag.setText(task.getType());

        String dateTime = task.getTime() + " " + task.getDate();
        holder.dateTime.setText(dateTime);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView todoCard;
        public CheckBox check;
        public TextView title;
        public TextView dateTime;
        public TextView description;
        public TextView tag;
        public ImageView timerIcon;
        public CardView highlight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            todoCard = itemView.findViewById(R.id.todo_item_card);
            check = itemView.findViewById(R.id.todo_check);
            title = itemView.findViewById(R.id.todo_heading_tv);
            dateTime = itemView.findViewById(R.id.date_time_tv);
            description = itemView.findViewById(R.id.todo_description_tv);
            tag = itemView.findViewById(R.id.tag_tv_todo);
            timerIcon = itemView.findViewById(R.id.timer_icon_todo);
            highlight = itemView.findViewById(R.id.todo_highlight_bar);
        }
    }
}
