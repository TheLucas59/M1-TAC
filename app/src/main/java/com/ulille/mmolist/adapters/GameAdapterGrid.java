package com.ulille.mmolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ulille.mmolist.R;
import com.ulille.mmolist.viewholders.GameViewHolderGrid;

public class GameAdapterGrid extends AbstractGameAdapter<GameViewHolderGrid>{
    public GameAdapterGrid(Context context, String activityName) {
        super(context, activityName);
    }

    @NonNull
    @Override
    public GameViewHolderGrid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.gamecard_grid;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new GameViewHolderGrid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolderGrid holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
