package com.dailytracker.app.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dailytracker.app.R;
import com.dailytracker.app.databinding.ActivityMainBinding;
import com.dailytracker.app.fragments.HomeFragment;
import com.dailytracker.app.fragments.ProfileFragment;
import com.dailytracker.app.fragments.RoutinesFragment;
import com.dailytracker.app.fragments.TipsFragment;
import com.dailytracker.app.service.ReminderForegroundService;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Start foreground service
        startForegroundService(new Intent(this, ReminderForegroundService.class));

        setupBottomNav();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_routines) {
                fragment = new RoutinesFragment();
            } else if (id == R.id.nav_tips) {
                fragment = new TipsFragment();
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else {
                return false;
            }
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
