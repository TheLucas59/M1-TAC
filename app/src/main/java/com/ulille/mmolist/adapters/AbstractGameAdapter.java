package com.ulille.mmolist.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ulille.mmolist.R;
import com.ulille.mmolist.activities.AllGameActivity;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.viewholders.AbstractGameViewHolder;

import java.util.ArrayList;
import java.util.List;

import util.Constant;

/**
 * Abstract Game Adapter that allow to concentrate common behaviour between Grid and List adapters
 * @param <VH>
 */
public abstract class AbstractGameAdapter<VH extends AbstractGameViewHolder> extends RecyclerView.Adapter<VH> {
    final List<Game> originalList;
    List<Game> games;
    final Context context;
    final String activityName;

    public AbstractGameAdapter(Context context, String activity){
        super();
        this.games = new ArrayList<>();
        this.originalList = new ArrayList<>();
        this.context = context;
        this.activityName = activity;
    }

    @NonNull
    @Override
    abstract public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * Bind the Game from the data list with his position in the recycler view
     * Set the listener for the favorite button
     * Update favorite button to set it at favorite or not
     * Set data in views
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.itemView.setOnClickListener(view -> ((AllGameActivity) context).openDetailsActivity(games.get(position), position));
        Game game = games.get(position);
        holder.buttonAddFavorite.setOnClickListener(view -> {
            ImageButton buttonFavorite = holder.buttonAddFavorite;
            if (game.isFavorite()) {
                ((AllGameActivity) context).deleteFavorite(game);
                game.setFavorite(false);
                buttonFavorite.setImageResource(R.drawable.pngwing_com);
                if(this.activityName.equals(Constant.EXTRAS_FAVORITE)){
                    games.remove(position);
                    notifyItemRemoved(position);
                }
            } else {
                ((AllGameActivity) context).insertFavorite(game);
                game.setFavorite(true);
                buttonFavorite.setImageResource(R.drawable.pngwing_com2);
            }
            notifyDataSetChanged();
        });

        if(game.isFavorite()) {
            holder.buttonAddFavorite.setImageResource(R.drawable.pngwing_com2);
        }else{
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
        this.games = games;
        if(this.originalList.size() == 0) {
            this.originalList.addAll(games);
        }
    }

    public List<Game> getOriginalList() {
        return this.originalList;
    }

    /**
     * Change the content of the recyclerview with the given games, so it can be filtered
     * @param games
     */
    public void filterList(List<Game> games){
        this.games = games;
        notifyDataSetChanged();
    }

    /**
     *
     * @return the number of element in data list
     */
    @Override
    public int getItemCount() {
        return this.games.size();
    }

}
