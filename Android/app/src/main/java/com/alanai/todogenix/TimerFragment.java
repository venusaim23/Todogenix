package com.alanai.todogenix;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alanai.todogenix.databinding.FragmentTimerBinding;

import java.util.Locale;

public class TimerFragment extends Fragment {

    private FragmentTimerBinding binding;

    private Context context;

    private CountDownTimer countDownTimer;

    private long totalTime = 60000;
    private long remaining = totalTime;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTimerBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateTimer();
        binding.progressTimer.setMax((int) totalTime);
        binding.progressTimer.setProgress((int) totalTime);

        binding.playBtnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        binding.pauseBtnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        binding.stopBtnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(remaining, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                remaining = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                binding.playBtnTimer.setVisibility(View.VISIBLE);
                binding.pauseBtnTimer.setVisibility(View.GONE);
            }
        }.start();

        binding.pauseBtnTimer.setVisibility(View.VISIBLE);
        binding.playBtnTimer.setVisibility(View.GONE);
    }

    private void updateTimer() {
        int hour = (int) (((remaining / 1000) / 60) / 60);
        int min = (int) ((remaining / 1000) / 60);
        int sec = (int) ((remaining / 1000) % 60);

        double progress = (double) (remaining * 100) / totalTime;
        binding.progressTimer.setProgress((int) remaining);

        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, min, sec);
        binding.timeTv.setText(time);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        binding.pauseBtnTimer.setVisibility(View.GONE);
        binding.playBtnTimer.setVisibility(View.VISIBLE);
        binding.stopBtnTimer.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        remaining = totalTime;
        updateTimer();
        binding.stopBtnTimer.setVisibility(View.GONE);
    }
}