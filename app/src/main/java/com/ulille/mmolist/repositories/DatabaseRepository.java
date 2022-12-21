package com.ulille.mmolist.repositories;

import android.app.Application;

import com.ulille.mmolist.api.model.GameDetails;
import com.ulille.mmolist.database.GameDatabase;
import com.ulille.mmolist.database.dao.GameDetailsDAO;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class DatabaseRepository {

    private GameDatabase db;
    private GameDetailsDAO gameDetailsDAO;

    public DatabaseRepository(Application application) {
        db = GameDatabase.getDatabase(application);
        gameDetailsDAO = db.gameDetailsDAO();
    }

    public void insertFavorite(GameDetails gameDetails) {
        GameDatabase.databaseWriteExecutor.execute(() -> {
            gameDetailsDAO.insert(gameDetails);
        });
    }

    public void deleteFavorite(GameDetails gameDetails) {
        GameDatabase.databaseWriteExecutor.execute(() -> {
            gameDetailsDAO.delete(gameDetails);
        });
    }

    public Single<List<GameDetails>> getAllFavoriteGames() {
        return db.gameDetailsDAO().getAllFavoriteGames();
    }

    public Single<GameDetails> getFavoriteGame(int id) {
        return db.gameDetailsDAO().getFavoriteGame(id);
    }

}
