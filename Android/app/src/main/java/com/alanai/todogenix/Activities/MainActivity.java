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
    private FragmentManager manager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private static final String SDK_KEY = "f5e57554206244ac1dc9a0d5cd74e85d2e956eca572e1d8b807a3e2338fdd0dc/stage";

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

        homeFragment = new HomeFragment();
        todoFragment = new TodoFragment();
        timerFragment = new TimerFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout_main, homeFragment).commit();

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
//                    Log.d("AlanButton", "onCommand: commandName: " + commandName);
                } catch (JSONException e) {
                    Log.e("AlanButton", e.getMessage());
                }
            }
        };

        binding.alanButton.registerCallback(alanCallback);
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
    public void addTask() {
        AddTask bottomSheet = new AddTask(this);
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
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        if (id == R.id.action_settings) {
            //open settings dialog
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void editTask(Task task) {
        AddTask bottomSheet = new AddTask(this, task);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }
}