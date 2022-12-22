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
    Single<List<GameDetails>> getAllFavoriteGames();

    @Query("SELECT * FROM GameDetails WHERE id = :id")
    Single<GameDetails> getFavoriteGame(int id);

    @Insert
    void insert(GameDetails gameDetails);

    @Delete
    void delete(GameDetails gameDetails);
}
