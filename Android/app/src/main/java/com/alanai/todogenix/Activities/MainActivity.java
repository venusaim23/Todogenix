package com.alanai.todogenix.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.alan.alansdk.AlanCallback;
import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.events.EventCommand;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.alanai.todogenix.Adapters.TaskAdapter;
import com.alanai.todogenix.AddTask;
import com.alanai.todogenix.Fragments.HomeFragment;
import com.alanai.todogenix.Models.Task;
import com.alanai.todogenix.R;
import com.alanai.todogenix.Fragments.TimerFragment;
import com.alanai.todogenix.Fragments.TodoFragment;
import com.alanai.todogenix.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener, TodoFragment.TodoFragmentListener, TaskAdapter.AdapterInterface {

    private ActivityMainBinding binding;

    private HomeFragment homeFragment;
    private TodoFragment todoFragment;
    private TimerFragment timerFragment;
    private AddTask bottomSheet;
    private FragmentManager manager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private static final String SDK_KEY = "f5e57554206244ac1dc9a0d5cd74e85d2e956eca572e1d8b807a3e2338fdd0dc/stage";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_tg_no_text);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);

        AlanConfig config = AlanConfig.builder().setProjectId(SDK_KEY).build();
        binding.alanButton.initWithConfig(config);

        AlanCallback alanCallback = new AlanCallback() {
            /// Handle commands from Alan Studio
            @Override
            public void onCommand(final EventCommand eventCommand) {
                try {
                    JSONObject command = eventCommand.getData();
//                    Log.d("JSONObject", "Basic Object: " + command);
                    JSONObject data = command.getJSONObject("data");
//                    Log.d("JSONObject", "Data Object: " + data);
                    String commandName = data.getString("command");
                    executeCommand(commandName, data);
                } catch (JSONException e) {
                    Log.e("AlanButton", e.getMessage());
                }
            }
        };

        binding.alanButton.registerCallback(alanCallback);

        homeFragment = new HomeFragment();
        todoFragment = new TodoFragment(binding.alanButton);
        timerFragment = new TimerFragment(binding.alanButton);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout_main, homeFragment).commit();

        bottomSheet = new AddTask(this, binding.alanButton);
    }

    private void executeCommand(String commandName, JSONObject data) {
        if (commandName.equals("go_back")) {
            onBackPressed();
        }

        if (commandName.equals("exit")) {
            binding.alanButton.deactivate();
            finish();
        }

        if (commandName.equals("log_out")) {
            logOut();
        }

        if (commandName.equals("open_todo")) {
            openTodo();
        }

        if (commandName.equals("add_task")) {
            addTask();
        }

        if (commandName.equals("set_title")) {
            try {
                String title = data.getString("title");
                bottomSheet.addTitle(title);
            } catch (JSONException e) {
                Log.e("AlanButton", e.getMessage());
                binding.alanButton.playText("I'm sorry I'm unable to do this at the moment");
            }
        }

        if (commandName.equals("set_description")) {
            try {
                String desc = data.getString("description");
                bottomSheet.addDescription(desc);
            } catch (JSONException e) {
                Log.e("AlanButton", e.getMessage());
                binding.alanButton.playText("I'm sorry I'm unable to do this at the moment");
            }
        }

        if (commandName.equals("set_type")) {
            try {
                String type = data.getString("type");
                bottomSheet.setSelection(type);
            } catch (JSONException e) {
                Log.e("AlanButton", e.getMessage());
                binding.alanButton.playText("I'm sorry I'm unable to do this at the moment");
            }
        }

        if (commandName.equals("refresh_tasks")) {
            todoFragment.onRefresh();
        }

        if (commandName.equals("confirm_add_task")) {
            bottomSheet.addTask();
        }

        if (commandName.equals("read_tasks")) {
            todoFragment.readTasks();
        }

        if (commandName.equals("highlight_task")) {
            try {
                int position = data.getInt("taskNo");
                todoFragment.readTask(position);
            } catch (JSONException e) {
                Log.e("AlanButton", e.getMessage());
                binding.alanButton.playText("I'm sorry I'm unable to do this at the moment");
            }
        }

        if (commandName.equals("check_task")) {
            try {
                int position = data.getInt("taskNo");
                todoFragment.checkTask(position);
            } catch (JSONException e) {
                Log.e("AlanButton", e.getMessage());
                binding.alanButton.playText("I'm sorry I'm unable to do this at the moment");
            }
        }

        if (commandName.equals("open_timer")) {
            openTimer();
        }

        if (commandName.equals("start_timer")) {
            timerFragment.startTimer();
        }

        if (commandName.equals("pause_timer")) {
            timerFragment.pauseTimer();
        }

        if (commandName.equals("reset_timer")) {
            timerFragment.resetTimer();
        }

        if (commandName.equals("short_break")) {
            timerFragment.startBreak(true);
        }

        if (commandName.equals("long_break")) {
            timerFragment.startBreak(false);
        }

        if (commandName.equals("stop_break")) {
            timerFragment.stopBreak();
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    //initialise alan
                } else {
                    //prompt dialog for permission
                }
            });

    @Override
    public void openTodo() {
        manager.beginTransaction().replace(R.id.frame_layout_main, todoFragment).addToBackStack(null).commit();
    }

    @Override
    public void openTimer() {
        manager.beginTransaction().replace(R.id.frame_layout_main, timerFragment).addToBackStack(null).commit();
    }

    @Override
    public void greetUser(String name) {
        if (count++ == 0) {
            binding.alanButton.activate();
            JSONObject object = new JSONObject();
            try {
                object.put("userName", name);
                binding.alanButton.callProjectApi("greetUser", object.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addTask() {
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            logOut();
            return true;
        }

        if (id == R.id.action_settings) {
            //open settings dialog
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        binding.alanButton.deactivate();
        finish();
    }

    @Override
    public void editTask(Task task) {
        AddTask bottomSheet = new AddTask(this, task);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }
}