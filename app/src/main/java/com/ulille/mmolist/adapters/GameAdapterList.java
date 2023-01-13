package com.ulille.mmolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ulille.mmolist.R;
import com.ulille.mmolist.viewholders.GameViewHolderList;

public class GameAdapterList extends AbstractGameAdapter<GameViewHolderList> {
    public GameAdapterList(Context context, String activityName) {
        super(context, activityName);
    }

    /**
     * Set layout for gameAdapterList
     * @param parent
     * @param viewType
     * @return the viewHolderList
     */
    @NonNull
    @Override
    public GameViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.gamecard_list;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new GameViewHolderList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolderList holder, int position) {
        holder.descriptionCard.setText(super.games.get(position).getShortDescription());
        super.onBindViewHolder(holder, position);
    }
}
