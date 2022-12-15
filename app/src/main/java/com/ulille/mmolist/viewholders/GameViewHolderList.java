package com.ulille.mmolist.viewholders;

import android.view.View;

import androidx.annotation.NonNull;

import com.ulille.mmolist.R;

public class GameViewHolderList extends AbstractGameViewHolder{
    public GameViewHolderList(@NonNull View item) {
        super(item);
        this.descriptionCard = item.findViewById(R.id.descriptionCard);
    }
}
