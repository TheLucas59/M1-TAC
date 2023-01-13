package com.ulille.mmolist.viewholders;

import android.view.View;

import androidx.annotation.NonNull;

import com.ulille.mmolist.R;

/**
 * GameViewHolderList is the same as the abstractClass, but it contains a description
 */
public class GameViewHolderList extends AbstractGameViewHolder{
    public GameViewHolderList(@NonNull View item) {
        super(item);
        this.descriptionCard = item.findViewById(R.id.descriptionCard);
    }
}
