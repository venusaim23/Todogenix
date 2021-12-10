package com.alanai.todogenix;

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

import com.alanai.todogenix.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.passwordEtLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                    login();
                return false;
            }
        });
    }

    private void login() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.layoutLogin.getWindowToken(), 0);

        binding.progressBarLogin.setVisibility(View.VISIBLE);

        String email = binding.emailEtLogin.getText().toString().trim();
        String password = binding.passwordEtLogin.getText().toString();

        if (email.isEmpty()) {
            layoutErrorManager(binding.emailTilLogin, binding.emailEtLogin, "Required");
            binding.progressBarLogin.setVisibility(View.GONE);
            return;
        }

        if (password.isEmpty()) {
            layoutErrorManager(binding.passwordTilLogin, binding.passwordEtLogin, "Required");
            binding.progressBarLogin.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mUser = mAuth.getCurrentUser();
                    displayHome();
                } else {
                    Toast.makeText(LoginActivity.this, "Sign in failed: " +
                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    binding.progressBarLogin.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progressBarLogin.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Sign in failed: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mUser = mAuth.getCurrentUser();
        if (mUser != null)
            displayHome();
    }

    private void displayHome() {
        binding.progressBarLogin.setVisibility(View.GONE);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void layoutErrorManager(TextInputLayout layout, EditText editText, String error) {
        layout.setError(error);
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