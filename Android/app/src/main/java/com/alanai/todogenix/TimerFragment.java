package com.alanai.todogenix;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alanai.todogenix.databinding.FragmentTimerBinding;

import java.util.Locale;

public class TimerFragment extends Fragment {

    private FragmentTimerBinding binding;

    private Context context;

    private CountDownTimer countDownTimer;
    private CountDownTimer cdtBreak;

    private long totalWorkTime = 60000;
    private long remainingWorkTime = totalWorkTime;

    //Todo settings
    private long totalShortTime = 300000;
    private long totalLongTime = 600000;

    private long time = 0;
    //0 - work mode, 1 - short break, 2 - long break
    private int mode = 0;

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

        updateTimer(remainingWorkTime);

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

        binding.shortBreakCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start short break
                if (mode != 1) {
                    mode = 1;
                    binding.shortBreakTagTv.setText(context.getResources().getString(R.string.stop));
                    startBreak(true);
                    binding.resumeCard.setVisibility(View.VISIBLE);
                } else {
                    stopBreak();
                    binding.shortBreakTagTv.setText(context.getResources().getString(R.string.start));
                }
            }
        });

        binding.longBreakCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start long break
                if (mode != 2) {
                    mode = 2;
                    binding.longBreakTagTv.setText(context.getResources().getString(R.string.stop));
                    startBreak(false);
                    binding.resumeCard.setVisibility(View.VISIBLE);
                } else {
                    stopBreak();
                    binding.longBreakTagTv.setText(context.getResources().getString(R.string.start));
                }
            }
        });

        binding.resumeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset breaks and resume work
                if (mode != 0) {
                    stopBreak();
                }
            }
        });
    }

    private void stopBreak() {
        //todo notify break stopped

        cdtBreak.cancel();
        if(mode == 1)
            binding.shortBreakTagTv.setText(context.getResources().getString(R.string.start));
        else if (mode == 2)
            binding.longBreakTagTv.setText(context.getResources().getString(R.string.start));
        mode = 0;

        binding.playBtnTimer.setVisibility(View.VISIBLE);
        binding.stopBtnTimer.setVisibility(View.VISIBLE);
        binding.resumeCard.setVisibility(View.GONE);
        startTimer();
    }

    private void startBreak(boolean shortBreak) {
        //todo notify break started

        pauseTimer();
        binding.playBtnTimer.setVisibility(View.GONE);
        binding.stopBtnTimer.setVisibility(View.GONE);

        time = shortBreak? totalShortTime: totalLongTime;

        binding.progressTimer.setMax((int) time);
        binding.progressTimer.setProgress((int) time);

        cdtBreak = new CountDownTimer(time, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = millisUntilFinished;
                updateTimer(time);
            }

            @Override
            public void onFinish() {
                //todo notify break finished
                stopBreak();
            }
        }.start();
    }

    private void startTimer() {
        //todo notify timer started

        binding.progressTimer.setMax((int) totalWorkTime);
        binding.progressTimer.setProgress((int) totalWorkTime);

        countDownTimer = new CountDownTimer(remainingWorkTime, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingWorkTime = millisUntilFinished;
                updateTimer(remainingWorkTime);
            }

            @Override
            public void onFinish() {
                //todo notify timer finished
                countDownTimer.cancel();
                binding.playBtnTimer.setVisibility(View.VISIBLE);
                binding.pauseBtnTimer.setVisibility(View.GONE);
            }
        }.start();

        binding.pauseBtnTimer.setVisibility(View.VISIBLE);
        binding.playBtnTimer.setVisibility(View.GONE);
    }

    private void updateTimer(long remaining) {
        int hour = (int) (((remaining / 1000) / 60) / 60);
        int min = (int) ((remaining / 1000) / 60);
        int sec = (int) ((remaining / 1000) % 60);

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
        remainingWorkTime = totalWorkTime;
        updateTimer(remainingWorkTime);
        binding.stopBtnTimer.setVisibility(View.GONE);
    }
}