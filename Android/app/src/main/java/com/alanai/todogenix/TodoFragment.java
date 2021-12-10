package com.alanai.todogenix;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alanai.todogenix.databinding.FragmentTodoBinding;

public class TodoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TodoFragmentListener listener;
    private FragmentTodoBinding binding;

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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addTask();
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