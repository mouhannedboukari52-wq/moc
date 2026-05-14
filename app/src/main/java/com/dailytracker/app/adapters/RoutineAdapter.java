package com.dailytracker.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailytracker.app.data.FirebaseRepository;
import com.dailytracker.app.databinding.ItemRoutineBinding;
import com.dailytracker.app.models.RoutineItem;
import com.dailytracker.app.utils.DateUtils;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    private List<RoutineItem> items;
    private final FirebaseRepository repository;
    private final String userId;
    private final String today;

    public RoutineAdapter(List<RoutineItem> items, FirebaseRepository repository,
                          String userId, String today) {
        this.items = items;
        this.repository = repository;
        this.userId = userId;
        this.today = today;
    }

    public void updateList(List<RoutineItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRoutineBinding b = ItemRoutineBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoutineItem item = items.get(position);
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.tvTime.setText(DateUtils.formatTime(item.getHour(), item.getMinute()));
        holder.binding.tvCategory.setText(item.getCategory());

        // Load done state
        repository.isRoutineDoneToday(userId, item.getId(), today, isDone -> {
            holder.binding.cbDone.setOnCheckedChangeListener(null);
            holder.binding.cbDone.setChecked(isDone);
            holder.binding.cbDone.setOnCheckedChangeListener((btn, checked) -> {
                if (checked) {
                    repository.markRoutineDone(userId, item.getId(), today,
                            () -> Toast.makeText(holder.binding.getRoot().getContext(),
                                    "Marked as done!", Toast.LENGTH_SHORT).show(),
                            err -> holder.binding.cbDone.setChecked(false));
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemRoutineBinding binding;

        ViewHolder(ItemRoutineBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
