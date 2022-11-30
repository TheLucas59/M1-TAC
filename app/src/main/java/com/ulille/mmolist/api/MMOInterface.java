package com.ulille.mmolist.api;

import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MMOInterface {
    @GET("games")
    public Observable<List<Game>> getAll();

    @GET("game")
    public Single<GameDetails> getGameDetails(@Query("id") int id);
}