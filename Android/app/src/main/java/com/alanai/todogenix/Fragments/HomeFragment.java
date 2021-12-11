package com.alanai.todogenix.Fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alanai.todogenix.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeFragmentListener fragmentListener;
    private FragmentHomeBinding binding;

    public interface HomeFragmentListener {
        void openTodo();
        void openTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.cardHeading.setPaintFlags(binding.cardHeading.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        binding.todoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.openTodo();
            }
        });

        binding.timerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.openTimer();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener)
            fragmentListener = (HomeFragmentListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement HomeFragmentListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
}