package com.alanai.todogenix;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alanai.todogenix.Adapters.TaskAdapter;
import com.alanai.todogenix.Models.Task;
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

    @Override
    public void onRefresh() {

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
//        binding.swipeRefreshTodo.setRefreshing(true);

        binding.recyclerViewTodo.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerViewTodo.setHasFixedSize(true);
        binding.recyclerViewTodo.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewTodo.setAdapter(adapter);

        getTasks();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addTask();
            }
        });
    }

    private void getTasks() {
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Task task = snapshot.getValue(Task.class);
                tasks.add(task);
                Toast.makeText(context, "Task added: " + task.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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