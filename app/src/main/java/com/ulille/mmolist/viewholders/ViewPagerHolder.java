package com.ulille.mmolist.viewholders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ulille.mmolist.R;

/**
 * ViewPagerHolder represent an item in the recyclerview for screenshots
 */
public class ViewPagerHolder extends RecyclerView.ViewHolder {
    public final ImageView fullScreenImage;
    public ViewPagerHolder(@NonNull View itemView) {
        super(itemView);
        fullScreenImage = itemView.findViewById(R.id.fullScreenImage);
    }
}
