package com.ulille.mmolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;
import com.ulille.mmolist.database.GameDatabase;
import com.ulille.mmolist.repositories.APIRepository;
import com.ulille.mmolist.repositories.DatabaseRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class GameViewModel extends AndroidViewModel {

    private APIRepository apiRepository;
    private DatabaseRepository databaseRepository;

    public GameViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository();
        databaseRepository = new DatabaseRepository(application);
    }

    public Observable<List<Game>> getAllGames() {
        return apiRepository.getAll();
    }

    public Single<GameDetails> getGameDetails(int id) {
        return apiRepository.getGameDetails(id);
    }

    public Single<List<GameDetails>> getAllFavoriteGames() {
        return databaseRepository.getAllFavoriteGames();
    }

    public Single<GameDetails> getFavoriteGame(int id) {
        return databaseRepository.getFavoriteGame(id);
    }

    public void insertFavorite(GameDetails gameDetails) {
       databaseRepository.insertFavorite(gameDetails);
    }

    public void deleteFavorite(GameDetails gameDetails) {
        databaseRepository.deleteFavorite(gameDetails);
    }
}
