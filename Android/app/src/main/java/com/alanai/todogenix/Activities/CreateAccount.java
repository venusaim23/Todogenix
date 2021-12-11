package com.alanai.todogenix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alanai.todogenix.Models.UserDetails;
import com.alanai.todogenix.R;
import com.alanai.todogenix.databinding.ActivityCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference dbRef;

    private String name, email, password, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null)
            finish();

        dbRef = FirebaseDatabase.getInstance().getReference();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails();
            }
        });

        binding.cityEtSign.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                    getDetails();
                return false;
            }
        });
    }

    private void getDetails() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.layoutSignUp.getWindowToken(), 0);

        binding.progressBarSignUp.setVisibility(View.VISIBLE);

        name = binding.nameEtSign.getText().toString().trim();
        if (name.isEmpty()) {
            layoutErrorManager(binding.cityTilSign, binding.cityEtSign, "Required");
            return;
        }

        email = binding.emailEtSign.getText().toString().trim();
        if (email.isEmpty()) {
            //todo email regex validation
            layoutErrorManager(binding.emailTilSign, binding.emailEtSign, "Required");
            return;
        }

        password = binding.passwordEtSign.getText().toString();
        String repPassword = binding.passwordRepEtSign.getText().toString();

        if (password.isEmpty()) {
            layoutErrorManager(binding.passwordTilSign, binding.passwordEtSign, "Required");
            return;
        }

        if (repPassword.isEmpty()) {
            layoutErrorManager(binding.passwordRepTilSign, binding.passwordRepEtSign, "Required");
            return;
        }

        if (!password.equals(repPassword)) {
            layoutErrorManager(binding.passwordRepTilSign, binding.passwordRepEtSign, "Passwords do not match");
            return;
        }

        city = binding.cityEtSign.getText().toString().trim();
        if (city.isEmpty()) {
            layoutErrorManager(binding.cityTilSign, binding.cityEtSign, "Required");
            return;
        }

        createAccount();
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccount.this, "Account created", Toast.LENGTH_SHORT).show();
                    mUser = mAuth.getCurrentUser();
                    updateDetails();
                } else {
                    Toast.makeText(CreateAccount.this, "Account could not be created: "
                            + task.getException(), Toast.LENGTH_LONG).show();
                    binding.progressBarSignUp.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateDetails() {
        UserDetails details = new UserDetails(name, email, city);
        details.setUid(mUser.getUid());
        dbRef.child(mUser.getUid()).child("Details").setValue(details)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, "Details saved", Toast.LENGTH_SHORT).show();
                        } else {
                            //handle error - prompt user to re-upload details after logging
                            Toast.makeText(CreateAccount.this, "Details could not be saved: "
                                    + task.getException(), Toast.LENGTH_LONG).show();
                        }
                        binding.progressBarSignUp.setVisibility(View.GONE);
                        startActivity(new Intent(CreateAccount.this, MainActivity.class));
                        finish();
                    }
                });
    }

    private void layoutErrorManager(TextInputLayout layout, EditText editText, String error) {
        layout.setError(error);
        binding.progressBarSignUp.setVisibility(View.GONE);
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