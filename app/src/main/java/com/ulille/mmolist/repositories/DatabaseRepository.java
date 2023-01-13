package com.ulille.mmolist.repositories;

import android.app.Application;

import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.database.GameDatabase;
import com.ulille.mmolist.database.dao.GameDAO;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * Class that will be called from JavaCode to interact with Room database by passing request through Room
 */
public class DatabaseRepository {

    private final GameDatabase db;
    private final GameDAO gameDAO;

    public DatabaseRepository(Application application) {
        db = GameDatabase.getDatabase(application);
        gameDAO = db.gameDAO();
    }

    public void insertFavorite(Game game) {
        GameDatabase.databaseWriteExecutor.execute(() -> gameDAO.insert(game));
    }

    public void deleteFavorite(Game game) {
        GameDatabase.databaseWriteExecutor.execute(() -> gameDAO.delete(game));
    }

    public Single<List<Game>> getAllFavoriteGames() {
        return db.gameDAO().getAllFavoriteGames();
    }

    public Single<Game> getFavoriteGame(int id) {
        return db.gameDAO().getFavoriteGame(id);
    }

}
