package com.dailytracker.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dailytracker.app.data.FirebaseRepository;
import com.dailytracker.app.databinding.ActivityLoginBinding;
import com.dailytracker.app.utils.HashUtils;
import com.dailytracker.app.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseRepository repository;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new FirebaseRepository();
        session = new SessionManager(this);

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String name = binding.etName.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnLogin.setEnabled(false);
        String hash = HashUtils.sha256(password);

        repository.getUserByName(name, new FirebaseRepository.UserCallback() {
            @Override
            public void onResult(com.dailytracker.app.models.User user) {
                binding.btnLogin.setEnabled(true);
                if (user == null) {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                } else if (!hash.equals(user.getPasswordHash())) {
                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                } else {
                    session.saveSession(user.getUid(), user.getName());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(String message) {
                binding.btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
