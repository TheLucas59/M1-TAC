package com.ulille.mmolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ulille.mmolist.R;
import com.ulille.mmolist.viewholders.ViewPagerHolder;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerHolder> {
    final Context context;
    final String[] uris;
    public ViewPagerAdapter(Context context, String[] uris){
        this.context = context;
        this.uris = uris;
    }
    @NonNull
    @Override
    public ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.view_pager_adapter;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent,false);
        return new ViewPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerHolder holder, int position) {

        Glide.with(context)
                .load(uris[position])
                .fitCenter()
                .into(holder.fullScreenImage);
    }

    @Override
    public int getItemCount() {
        return uris.length;
    }
}
