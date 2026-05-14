package com.dailytracker.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dailytracker.app.activities.AddRoutineActivity;
import com.dailytracker.app.adapters.RoutineAdapter;
import com.dailytracker.app.data.FirebaseRepository;
import com.dailytracker.app.databinding.FragmentRoutinesBinding;
import com.dailytracker.app.models.RoutineItem;
import com.dailytracker.app.utils.DateUtils;
import com.dailytracker.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoutinesFragment extends Fragment {

    private FragmentRoutinesBinding binding;
    private FirebaseRepository repository;
    private SessionManager session;
    private RoutineAdapter foodAdapter;
    private RoutineAdapter sportAdapter;
    private List<RoutineItem> allRoutines = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRoutinesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new FirebaseRepository();
        session = new SessionManager(requireContext());

        String today = DateUtils.today();
        String userId = session.getUserId();

        foodAdapter = new RoutineAdapter(new ArrayList<>(), repository, userId, today);
        sportAdapter = new RoutineAdapter(new ArrayList<>(), repository, userId, today);

        binding.rvFood.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFood.setAdapter(foodAdapter);

        binding.rvSport.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSport.setAdapter(sportAdapter);

        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AddRoutineActivity.class)));

        loadRoutines(userId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (session != null) loadRoutines(session.getUserId());
    }

    private void loadRoutines(String userId) {
        repository.getUserRoutines(userId, new FirebaseRepository.RoutinesCallback() {
            @Override
            public void onResult(List<RoutineItem> items) {
                if (getContext() == null) return;
                allRoutines = items;
                List<RoutineItem> food = items.stream()
                        .filter(i -> "FOOD".equals(i.getCategory()))
                        .collect(Collectors.toList());
                List<RoutineItem> sport = items.stream()
                        .filter(i -> "SPORT".equals(i.getCategory()))
                        .collect(Collectors.toList());
                foodAdapter.updateList(food);
                sportAdapter.updateList(sport);
            }

            @Override
            public void onError(String message) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
