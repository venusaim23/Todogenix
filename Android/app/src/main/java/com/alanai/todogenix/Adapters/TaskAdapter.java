package com.alanai.todogenix.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alanai.todogenix.Models.Task;
import com.alanai.todogenix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference dbRef;

    private AdapterInterface adapterInterface;

    public interface AdapterInterface {
        void editTask(Task task);
    }

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference(mUser.getUid()).child("Tasks");

        if (context instanceof AdapterInterface)
            adapterInterface = (AdapterInterface) context;
        else
            throw new RuntimeException(context.toString() + " must implement AdapterInterface");
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

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(task.getTimeStamp());
        String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        String time = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
        String dateTime = date + " " + time;
        holder.dateTime.setText(dateTime);

        //todo set duration in hours
        String duration = "(" +  task.getDuration() + " min)";

        if (task.getDate() != null) {
            if (!task.getDate().isEmpty() && !task.getDate().equals("")) {
                holder.dueTV.setVisibility(View.VISIBLE);
                String taskDue = "Due: " + task.getTime() + " " + task.getDate() + " " + duration;
                holder.dueTV.setText(taskDue);
            }
        }

        holder.check.setChecked(task.isComplete());
        if (holder.check.isChecked()) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.todoCard.setBackgroundColor(context.getResources().getColor(R.color.gray_completed));
        }

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.check.isChecked()) {
                    holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    holder.todoCard.setBackgroundColor(context.getResources().getColor(R.color.gray_completed));
                } else {
                    holder.title.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//                    holder.todoCard.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                task.setComplete(holder.check.isChecked());
                DatabaseReference ref = dbRef.child(task.getTaskID());
                ref.setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else
                            Toast.makeText(context, "Couldn't update task", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public CardView todoCard;
        public CheckBox check;
        public TextView title;
        public TextView dateTime;
        public TextView description;
        public TextView tag;
        public ImageView timerIcon;
        public CardView highlight;
        public TextView dueTV;

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
            dueTV = itemView.findViewById(R.id.due_tv_todo);

            todoCard.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 100, 0, "Edit");
            menu.add(this.getAdapterPosition(), 101, 1, "Delete");
        }
    }

    public void editTask(int position) {
        //edit item
        adapterInterface.editTask(tasks.get(position));
        notifyItemChanged(position);
    }

    public void deleteTask(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete task?")
                .setMessage("Are you sure? Do you want to delete the task?")
                .setCancelable(true)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task t = tasks.get(position);
                        DatabaseReference ref = dbRef.child(t.getTaskID());
                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                if (task.isSuccessful()) {
                                    tasks.remove(t);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(context, "Could not delete: " +
                                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog.cancel();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
