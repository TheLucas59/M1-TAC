package com.ulille.mmolist.api;

import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface with the http request that are allowed by the API
 */
public interface MMOInterface {
    @GET("games")
    Single<List<Game>> getAll();

    @GET("game")
    Single<GameDetails> getGameDetails(@Query("id") int id);
}
