package com.alanai.todogenix;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alanai.todogenix.databinding.FragmentAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddTask extends BottomSheetDialogFragment {

    private FragmentAddTaskBinding binding;

    private Context context;

    private String[] taskTypes;

    public AddTask(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);

        taskTypes = getResources().getStringArray(R.array.task_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_layout, taskTypes);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinner) {
                    String type = binding.spinner.getSelectedItem().toString();
                    if (type.equals(taskTypes[0]))
                        binding.dateTimeCard.setVisibility(View.GONE);
                    if (type.equals(taskTypes[1]) || type.equals(taskTypes[2])) {
                        binding.dateTimeCard.setVisibility(View.VISIBLE);
                        //set date and time pickers
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.layoutAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.layoutAddTask.getWindowToken(), 0);
            }
        });

        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        //validate and upload data
    }
}