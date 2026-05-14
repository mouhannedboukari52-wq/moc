package com.dailytracker.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailytracker.app.databinding.ItemTipBinding;
import com.dailytracker.app.models.Tip;

import java.util.List;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder> {

    private List<Tip> items;

    public TipAdapter(List<Tip> items) {
        this.items = items;
    }

    public void updateList(List<Tip> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTipBinding b = ItemTipBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tip tip = items.get(position);
        holder.binding.tvTitle.setText(tip.getTitle());
        holder.binding.tvDescription.setText(tip.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTipBinding binding;

        ViewHolder(ItemTipBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
