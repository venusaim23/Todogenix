package com.alanai.todogenix.Fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alanai.todogenix.Models.UserDetails;
import com.alanai.todogenix.R;
import com.alanai.todogenix.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private HomeFragmentListener fragmentListener;
    private FragmentHomeBinding binding;

    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference dbRef;

    private UserDetails userDetails;

    public interface HomeFragmentListener {
        void openTodo();
        void openTimer();
        void greetUser(String name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference().child(mUser.getUid());

        getUserDetails();

        return binding.getRoot();

    }

    private void getUserDetails() {
        dbRef.child("Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails = snapshot.getValue(UserDetails.class);
                if (userDetails != null)
                    updateDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateDetails() {
        if (userDetails.getName() != null) {
            String greeting = context.getResources().getString(R.string.good_morning) + ",\n" +
                    userDetails.getName() + context.getResources().getString(R.string.sun_emote);
            binding.greetingTv.setText(greeting);
            fragmentListener.greetUser(userDetails.getName());
        }

        //todo sharedprefs
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
        this.context = context;
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