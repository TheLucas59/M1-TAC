package com.ulille.mmolist.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ulille.mmolist.R;

public abstract class AbstractGameViewHolder extends RecyclerView.ViewHolder{
    public final TextView titleCard;
    public TextView descriptionCard;
    public final ImageButton buttonAddFavorite;
    public final ImageView imageGame;

        public AbstractGameViewHolder(@NonNull View item) {
            super(item);
            this.titleCard = item.findViewById(R.id.titleCard);
            this.buttonAddFavorite = item.findViewById(R.id.buttonAddFavorite);
            this.imageGame = item.findViewById(R.id.imageGame);
        }
}
