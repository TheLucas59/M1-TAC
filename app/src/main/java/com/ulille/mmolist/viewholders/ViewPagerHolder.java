package com.ulille.mmolist.viewholders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ulille.mmolist.R;

public class ViewPagerHolder extends RecyclerView.ViewHolder {
    public ImageView fullScreenImage;
    public ViewPagerHolder(@NonNull View itemView) {
        super(itemView);
        fullScreenImage = itemView.findViewById(R.id.fullScreenImage);
    }
}