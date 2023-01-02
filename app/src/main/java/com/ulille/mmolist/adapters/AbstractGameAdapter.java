package com.ulille.mmolist.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ulille.mmolist.R;
import com.ulille.mmolist.activities.AllGameActivity;
import com.ulille.mmolist.activities.GameDetailsActivity;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.viewholders.AbstractGameViewHolder;
import com.ulille.mmolist.viewmodel.GameViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameAdapter<VH extends AbstractGameViewHolder> extends RecyclerView.Adapter<VH> {
    List<Game> games;
    Context context;

    public AbstractGameAdapter(Context context){
        super();
        this.games = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    abstract public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.itemView.setOnClickListener(view -> {
            ((AllGameActivity) context).openDetailsActivity(games.get(position), position);
        });
        Game game = games.get(position);
        holder.buttonAddFavorite.setOnClickListener(view -> {
            ImageButton buttonFavorite = holder.buttonAddFavorite;
            if (game.isFavorite()) {
                ((AllGameActivity) context).deleteFavorite(game);
                game.setFavorite(false);
                buttonFavorite.setImageResource(R.drawable.pngwing_com);
            } else {
                ((AllGameActivity) context).insertFavorite(game);
                game.setFavorite(true);
                buttonFavorite.setImageResource(R.drawable.pngwing_com2);
            }
            notifyDataSetChanged();
        });

        if(game.isFavorite()) {
            holder.buttonAddFavorite.setImageResource(R.drawable.pngwing_com2);
        }
        else {
            holder.buttonAddFavorite.setImageResource(R.drawable.pngwing_com);
        }
        holder.titleCard.setText(game.getTitle());
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Glide.with(context)
                    .load(game.getThumbnail())
                    .fitCenter()
                    .into(holder.imageGame);
        }else{
            Glide.with(context)
                    .load(game.getThumbnail())
                    .into(holder.imageGame);
        }
    }

    public void setGames(List<Game> games) {
        this.games.addAll(games);
    }

    public List<Game> getGames() {
        return games;
    }

    @Override
    public int getItemCount() {
        return this.games.size();
    }

}
