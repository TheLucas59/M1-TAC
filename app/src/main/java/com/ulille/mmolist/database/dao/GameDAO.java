package com.ulille.mmolist.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ulille.mmolist.api.model.Game;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface GameDAO {

    @Query("SELECT * FROM Game")
    Single<List<Game>> getAllFavoriteGames();

    @Query("SELECT * FROM Game WHERE id = :id")
    Single<Game> getFavoriteGame(int id);

    @Insert
    void insert(Game game);

    @Delete
    void delete(Game game);
}
