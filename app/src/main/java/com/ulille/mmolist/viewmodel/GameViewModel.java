package com.ulille.mmolist.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;
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

    public Single<List<Game>> getAllGames() {
        return apiRepository.getAll();
    }

    public Single<GameDetails> getGameDetails(int id) {
        return apiRepository.getGameDetails(id);
    }

    public Single<List<Game>> getAllGamesAndFavoriteMerged() {
        Observable<Game> apiGames = getAllGames().flatMapObservable(Observable::fromIterable);
        Observable<Game> dbGames = getAllFavoriteGames().flatMapObservable(Observable::fromIterable);

        Observable<Game> mergedGames = Observable.merge(apiGames, dbGames)
                .groupBy(Game::getId)
                .flatMapSingle(grp -> grp.toList()
                        .map(l -> {
                            if(l.size() > 1) {
                                return Game.markAsFavorite(l.stream().findFirst().get());
                            }
                            return l.stream().findFirst().get();
                        })
                );

        return mergedGames.toList();
    }

    public Single<List<Game>> getAllFavoriteGames() {
        return databaseRepository.getAllFavoriteGames()
                .flatMapObservable(Observable::fromIterable)
                    .map(Game::markAsFavorite).toList();
    }

    public void insertFavorite(Game game) {
        databaseRepository.insertFavorite(game);
    }

    public void deleteFavorite(Game game) {
        databaseRepository.deleteFavorite(game);
    }
}
