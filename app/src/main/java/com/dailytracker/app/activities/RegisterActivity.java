package com.dailytracker.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dailytracker.app.data.FirebaseRepository;
import com.dailytracker.app.databinding.ActivityRegisterBinding;
import com.dailytracker.app.models.User;
import com.dailytracker.app.utils.HashUtils;
import com.dailytracker.app.utils.SessionManager;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseRepository repository;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new FirebaseRepository();
        session = new SessionManager(this);

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
        binding.tvGoLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        String name = binding.etName.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirm = binding.etConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnRegister.setEnabled(false);

        // Check if name already taken
        repository.getUserByName(name, new FirebaseRepository.UserCallback() {
            @Override
            public void onResult(User user) {
                if (user != null) {
                    binding.btnRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid = UUID.randomUUID().toString();
                String hash = HashUtils.sha256(password);
                User newUser = new User(uid, name, hash);

                repository.saveUser(newUser, () -> {
                    binding.btnRegister.setEnabled(true);
                    session.saveSession(uid, name);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finishAffinity();
                }, err -> {
                    binding.btnRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Error: " + err, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String message) {
                binding.btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
