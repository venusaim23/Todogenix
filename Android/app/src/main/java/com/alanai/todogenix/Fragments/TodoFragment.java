package com.alanai.todogenix.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alan.alansdk.button.AlanButton;
import com.alanai.todogenix.Adapters.TaskAdapter;
import com.alanai.todogenix.Models.Task;
import com.alanai.todogenix.R;
import com.alanai.todogenix.databinding.FragmentTodoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TodoFragmentListener listener;
    private FragmentTodoBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference dbRef;

    private List<Task> tasks;
    private TaskAdapter adapter;

    private Context context;
    private AlanButton alanButton;

    public TodoFragment(AlanButton alanButton) {
        this.alanButton = alanButton;
    }

    @Override
    public void onRefresh() {
        if (tasks != null)
            tasks.clear();
        adapter.notifyDataSetChanged();
        binding.swipeRefreshTodo.setRefreshing(true);
        setTimer();
        getTasks();
    }

    public interface TodoFragmentListener {
        void addTask();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof TodoFragmentListener)
            listener = (TodoFragmentListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement TodoFragmentListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTodoBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference(mUser.getUid()).child("Tasks");

        tasks = new ArrayList<>();
        adapter = new TaskAdapter(context, tasks);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.swipeRefreshTodo.setColorSchemeResources(R.color.alan_blue);
        binding.swipeRefreshTodo.setOnRefreshListener(this);
        binding.swipeRefreshTodo.setRefreshing(true);

        binding.recyclerViewTodo.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerViewTodo.setHasFixedSize(true);
        binding.recyclerViewTodo.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewTodo.setAdapter(adapter);

        getTasks();
        setTimer();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addTask();
            }
        });
    }

    private void setTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tasks.isEmpty()) {
                    binding.swipeRefreshTodo.setRefreshing(false);
                    binding.emptyTvTodo.setVisibility(View.VISIBLE);
                }
            }
        }, 4000);
    }

    public void readTasks() {
        if (tasks.size() == 0)
            alanButton.playText("The list is empty. Try adding a task");
        else {
            alanButton.playText("Reading task titles");
            for (Task task: tasks) {
                alanButton.playText(task.getTitle());
                //highlight the task
            }
        }
    }

    public void readTask(int position) {
        //read task
        alanButton.playText("Reading task " + position);
    }

    public void checkTask(int position) {
        //check task
        alanButton.playText("I'm very sorry. This feature is under development");
    }

    private void getTasks() {
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Task task = snapshot.getValue(Task.class);
                if (task != null) {
                    if (!task.isComplete())
                        tasks.add(0, task);
                    else
                        tasks.add(task);
                    binding.swipeRefreshTodo.setRefreshing(false);
//                    adapter.notifyItemInserted(tasks.size() + 1);
                    adapter.notifyDataSetChanged();
                }
                binding.emptyTvTodo.setVisibility(View.GONE);
//                Toast.makeText(context, "Task added: " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Task task = snapshot.getValue(Task.class);
                if (task != null) {
                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).getTaskID().equals(task.getTaskID())) {
                            tasks.set(i, task);
                            adapter.notifyItemChanged(i);
                            adapter.notifyDataSetChanged(); //multiple times displayed
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Task task = snapshot.getValue(Task.class);
                if (task != null) {
                    int i = tasks.indexOf(task);
                    tasks.remove(task);
//                    adapter.notifyItemRemoved(i);
                    adapter.notifyDataSetChanged();
                    if (tasks.size() == 0)
                        binding.emptyTvTodo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 100:
                adapter.editTask(item.getGroupId());
                return true;

            case 101:
                adapter.deleteTask(item.getGroupId());
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}