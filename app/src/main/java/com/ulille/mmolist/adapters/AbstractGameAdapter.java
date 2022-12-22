package com.ulille.mmolist.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ulille.mmolist.R;
import com.ulille.mmolist.activities.GameDetailsActivity;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.viewholders.AbstractGameViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameAdapter<VH extends AbstractGameViewHolder> extends RecyclerView.Adapter<VH> {
    List<Game> games;
    Context context;
    List<Game> favorite;


    public AbstractGameAdapter(Context context){
        super();
        this.games = new ArrayList<>();
        this.context = context;
        this.favorite = new ArrayList<>();
    }


    @NonNull
    @Override
    abstract public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.itemView.setOnClickListener(view -> {
            Intent startActivityIntent = new Intent(context, GameDetailsActivity.class);
            startActivityIntent.putExtra("idGame", games.get(position).getId());
            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startActivityIntent);
            games.get(position);
        });
        Game game = games.get(position);
        holder.buttonAddFavorite.setOnClickListener(view -> {
            ImageButton buttonFavorite = holder.buttonAddFavorite;
            if (favorite.contains(games.get(position))) {
                favorite.remove(games.get(position));
                buttonFavorite.setImageResource(R.drawable.pngwing_com);
            } else {
                favorite.add(games.get(position));
                buttonFavorite.setImageResource(R.drawable.pngwing_com2);
            }
        });

        holder.titleCard.setText(game.getTitle());
        Glide.with(context)
                .load(game.getThumbnail())
                .fitCenter()
                .into(holder.imageGame);
    }

    public void setGames(List<Game> games) {
        this.games.addAll(games);
        notifyDataSetChanged();
    }

    public List<Game> getGames() {
        return games;
    }

    @Override
    public int getItemCount() {
        return this.games.size();
    }


}
