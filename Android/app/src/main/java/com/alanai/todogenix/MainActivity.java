package com.alanai.todogenix;

import android.Manifest;
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

import com.alanai.todogenix.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private HomeFragment homeFragment;
    private TodoFragment todoFragment;
    private FragmentManager manager;

    private static final String SDK_KEY = "f5e57554206244ac1dc9a0d5cd74e85d2e956eca572e1d8b807a3e2338fdd0dc/stage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        homeFragment = new HomeFragment();
        todoFragment = new TodoFragment();
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

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void requestPermission() {

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    //initialise alan
                } else {
                    //prompt dialog for permission
                }
            });

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}