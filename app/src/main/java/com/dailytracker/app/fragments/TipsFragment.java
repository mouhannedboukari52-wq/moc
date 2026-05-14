package com.dailytracker.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dailytracker.app.adapters.TipAdapter;
import com.dailytracker.app.data.FirebaseRepository;
import com.dailytracker.app.databinding.FragmentTipsBinding;
import com.dailytracker.app.models.Tip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TipsFragment extends Fragment {

    private FragmentTipsBinding binding;
    private FirebaseRepository repository;
    private TipAdapter adapter;
    private String currentCategory = "FOOD";

    // Default local tips shown before Firebase loads
    private static final List<Tip> LOCAL_FOOD_TIPS = Arrays.asList(
            new Tip("f1", "FOOD", "Stay Hydrated", "Drink at least 8 glasses of water daily to maintain good health."),
            new Tip("f2", "FOOD", "Eat More Vegetables", "Include 5 portions of fruits and vegetables in your daily meals."),
            new Tip("f3", "FOOD", "Reduce Sugar", "Limit added sugars to less than 10% of your total daily calorie intake."),
            new Tip("f4", "FOOD", "Whole Grains", "Choose whole grains over refined grains for better nutrition.")
    );

    private static final List<Tip> LOCAL_SPORT_TIPS = Arrays.asList(
            new Tip("s1", "SPORT", "30 Minutes Daily", "Aim for at least 30 minutes of moderate activity each day."),
            new Tip("s2", "SPORT", "Warm Up First", "Always warm up for 5-10 minutes before intense exercise."),
            new Tip("s3", "SPORT", "Stay Consistent", "Consistency is more important than intensity when starting out."),
            new Tip("s4", "SPORT", "Rest Days Matter", "Allow your body to recover with proper rest between workouts.")
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTipsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new FirebaseRepository();
        adapter = new TipAdapter(new ArrayList<>());

        binding.rvTips.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTips.setAdapter(adapter);

        binding.tabLayout.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                currentCategory = tab.getPosition() == 0 ? "FOOD" : "SPORT";
                loadTips(currentCategory);
            }

            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {}
        });

        loadTips(currentCategory);
    }

    private void loadTips(String category) {
        // Show local defaults immediately
        adapter.updateList("FOOD".equals(category) ? LOCAL_FOOD_TIPS : LOCAL_SPORT_TIPS);

        // Then try to load from Firebase
        repository.getTipsByCategory(category, new FirebaseRepository.TipsCallback() {
            @Override
            public void onResult(List<Tip> tips) {
                if (getContext() == null) return;
                if (!tips.isEmpty()) {
                    adapter.updateList(tips);
                }
            }

            @Override
            public void onError(String message) {
                // Keep showing local defaults on error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
