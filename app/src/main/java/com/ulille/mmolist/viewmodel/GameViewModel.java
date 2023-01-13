package com.ulille.mmolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;
import com.ulille.mmolist.repositories.APIRepository;
import com.ulille.mmolist.repositories.DatabaseRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 * GameViewModel will interact with the different repository so it can return to view the datas
 */
public class GameViewModel extends AndroidViewModel {

    private final APIRepository apiRepository;
    private final DatabaseRepository databaseRepository;

    public GameViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository();
        databaseRepository = new DatabaseRepository(application);
    }

    /**
     *
     * @return All Game from the API
     */
    public Single<List<Game>> getAllGames() {
        return apiRepository.getAll();
    }

    /**
     * Interrogate API to get details about a game
     * @param id the id of the game we want details of
     * @return The details of a game
     */
    public Single<GameDetails> getGameDetails(int id) {
        return apiRepository.getGameDetails(id);
    }

    /**
     * Interrogate both the API and the local database to get AllGame and mark the one favorites
     * as favorites
     * @return AllGame with the favorites marked
     */
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
                            if(l.stream().findFirst().isPresent()) {
                                return l.stream().findFirst().get();
                            }
                            return null;
                        })
                );

        return mergedGames.toList();
    }

    /**
     * Interrogate the local database to get all favorites
     * @return The list of favorite games
     */
    public Single<List<Game>> getAllFavoriteGames() {
        return databaseRepository.getAllFavoriteGames()
                .flatMapObservable(Observable::fromIterable)
                    .map(Game::markAsFavorite).toList();
    }

    /**
     * Insert a favorite in local database
     * @param game The game to be inserted
     */
    public void insertFavorite(Game game) {
        databaseRepository.insertFavorite(game);
    }

    /**
     * Remove a favorite from the local database
     * @param game The game to be deleted
     */
    public void deleteFavorite(Game game) {
        databaseRepository.deleteFavorite(game);
    }
}
