package com.ulille.mmolist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    List<Game> games;
    Context context;
    List<Game> favorite;

    public GameAdapter(Context context){
        super();
        this.games = new ArrayList<>();
        this.context = context;
        this.favorite = new ArrayList<>();
    }


    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.v_gamecard_list;
        if(viewType == Configuration.ORIENTATION_LANDSCAPE) {
            layout = R.layout.v_gamecard_grid;
            Log.d("DEBUG", "onCreateViewHolder: horizontal ");
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new GameViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.GameViewHolder holder, int position) {

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

        holder.descriptionCard.setText(game.getShortDescription());
        holder.titleCard.setText(game.getTitle());
        Glide.with(context)
                .load(game.getThumbnail())
                .into(holder.imageGame);
    }

    public void setGames(List<Game> games) {
        this.games.addAll(games);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.games.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder{
        TextView titleCard;
        TextView descriptionCard;
        ImageButton buttonAddFavorite;
        ImageView imageGame;

        public GameViewHolder(@NonNull View item) {
            super(item);
            this.titleCard = item.findViewById(R.id.titleCard);
            this.descriptionCard = item.findViewById(R.id.descriptionCard);
            this.buttonAddFavorite = item.findViewById(R.id.buttonAddFavorite);
            this.imageGame = item.findViewById(R.id.imageGame);
        }
    }
}
