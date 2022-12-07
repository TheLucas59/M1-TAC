package com.ulille.mmolist;

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

import com.ulille.mmolist.api.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    List<Game> games;

    public GameAdapter(){
        super();
        this.games = new ArrayList<>();
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
        Log.d("EGG", "onBindViewHolder: " + game.getTitle());
        holder.buttonAddFavorite.setOnClickListener(view -> Log.d("EGG", game.getTitle()));
        holder.descriptionCard.setText(game.getShortDescription());
        holder.titleCard.setText(game.getTitle());
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

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleCard = itemView.findViewById(R.id.titleCard);
            this.descriptionCard = itemView.findViewById(R.id.descriptionCard);
            this.buttonAddFavorite = itemView.findViewById(R.id.buttonAddFavorite);
            this.imageGame = itemView.findViewById(R.id.imageGame);
        }
    }
}
