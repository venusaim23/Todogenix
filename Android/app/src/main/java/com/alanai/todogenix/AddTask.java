package com.alanai.todogenix;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alanai.todogenix.Models.Task;
import com.alanai.todogenix.databinding.FragmentAddTaskBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;

public class AddTask extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FragmentAddTaskBinding binding;

    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference dbRef;

    private String title, description, taskType, date, time;
    private String[] taskTypes;

    private Task task;

    public AddTask(Context context) {
        this.context = context;
    }

    public AddTask(Context context, Task task) {
        this.context = context;
        this.task = task;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference(mUser.getUid()).child("Tasks");

        taskTypes = getResources().getStringArray(R.array.task_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_layout, taskTypes);
        binding.spinner.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        binding.setDateTimeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                binding.dateTilTask.setError(null);
                binding.timeTilTask.setError(null);

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(context, AddTask.this,
                        year, month, day);
                dialog.show();
            }
        });

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
//                Toast.makeText(context, "Add button clicked", Toast.LENGTH_SHORT).show();
                binding.progressBarAddTask.setVisibility(View.VISIBLE);
            }
        });

        binding.cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (task != null) {
            setValues();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "/" + month + "/" + year;
        binding.dateEtTask.setText(date);

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, AddTask.this,
                hour, min, DateFormat.is24HourFormat(context));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time = hourOfDay + ":" + minute;
        binding.timeEtTask.setText(time);
    }

    private void setValues() {
        binding.taskHeadingEt.setText(task.getTitle());
        binding.taskDescEt.setText(task.getDescription());
        binding.spinner.setSelection(Arrays.asList(taskTypes).indexOf(task.getType()));
        binding.dateEtTask.setText(task.getDate());
        binding.timeEtTask.setText(task.getTime());
    }

    private void getData() {
        if (binding.taskHeadingEt.getText() != null) {
            title = binding.taskHeadingEt.getText().toString().trim();
            if (title.isEmpty()) {
                layoutErrorManager(binding.taskHeadingTil, binding.taskHeadingEt, "Required");
                return;
            }
        } else {
            layoutErrorManager(binding.taskHeadingTil, binding.taskHeadingEt, "Required");
            return;
        }

        if (binding.taskDescEt.getText() != null) {
            description = binding.taskDescEt.getText().toString().trim();
            if (description.isEmpty()) {
                layoutErrorManager(binding.taskDescTil, binding.taskDescEt, "Required");
                return;
            }
        } else {
            layoutErrorManager(binding.taskDescTil, binding.taskDescEt, "Required");
            return;
        }

        taskType = binding.spinner.getSelectedItem().toString();
        Toast.makeText(context, taskType + " selected", Toast.LENGTH_SHORT).show();
        if (taskType.equals(taskTypes[1]) || taskType.equals(taskTypes[2])) {
            if (binding.dateEtTask.getText() != null) {
                date = binding.dateEtTask.getText().toString().trim();
                if (date.isEmpty()) {
//                    layoutErrorManager(binding.dateTilTask, binding.dateEtTask, "Required");
                    binding.dateTilTask.setError("Required");
                    binding.dateEtTask.requestFocus();
                    binding.progressBarAddTask.setVisibility(View.GONE);
                    return;
                }
            } else {
//                layoutErrorManager(binding.dateTilTask, binding.dateEtTask, "Required");
                binding.dateTilTask.setError("Required");
                binding.dateEtTask.requestFocus();
                binding.progressBarAddTask.setVisibility(View.GONE);
                return;
            }

            if (binding.timeEtTask.getText() != null) {
                time = binding.timeEtTask.getText().toString().trim();
                if (time.isEmpty()) {
//                    layoutErrorManager(binding.timeTilTask, binding.timeEtTask, "Required");
                    binding.timeTilTask.setError("Required");
                    binding.timeEtTask.requestFocus();
                    binding.progressBarAddTask.setVisibility(View.GONE);
                    return;
                }
            } else {
//                layoutErrorManager(binding.timeTilTask, binding.timeEtTask, "Required");
                binding.timeTilTask.setError("Required");
                binding.timeEtTask.requestFocus();
                binding.progressBarAddTask.setVisibility(View.GONE);
                return;
            }
        }

        updateDateAndTime();
    }

    private void updateDateAndTime() {
        DatabaseReference ref;
        if (task != null) {
            ref = dbRef.child(task.getTaskID());
            task = new Task(task.getTaskID(), title, description, taskType, date, time, task.getTimeStamp());
        } else {
            ref = dbRef.push();
            String key = ref.getKey();
            task = new Task(key, title, description, taskType, date, time, System.currentTimeMillis());
        }
        ref.setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show();
                    binding.progressBarAddTask.setVisibility(View.GONE);
                    dismiss();
                } else {
                    if (task.getException() != null) {
                        Toast.makeText(context, "Task upload failed: " + task.getException().toString(),
                                Toast.LENGTH_SHORT).show();
                        binding.progressBarAddTask.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(context, "Task could not be added", Toast.LENGTH_SHORT).show();
                        binding.progressBarAddTask.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void layoutErrorManager(TextInputLayout layout, EditText editText, String error) {
        layout.setError(error);
        binding.progressBarAddTask.setVisibility(View.GONE); //not disappearing
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.requestFocus();
    }
}