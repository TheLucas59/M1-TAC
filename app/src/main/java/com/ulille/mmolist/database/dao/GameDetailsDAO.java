package com.ulille.mmolist.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ulille.mmolist.api.model.GameDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameDetailsDAO {

    @Query("SELECT * FROM GameDetails")
    public Single<List<GameDetails>> getAllFavoriteGames();

    @Query("SELECT * FROM GameDetails WHERE id = :id")
    public Single<GameDetails> getFavoriteGame(int id);

    @Insert
    public void insert(GameDetails gameDetails);

    @Delete
    public void delete(GameDetails gameDetails);
}
