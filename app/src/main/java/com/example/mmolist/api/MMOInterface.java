package com.example.mmolist.api;

import com.example.mmolist.api.model.Game;
import com.example.mmolist.api.model.GameDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MMOInterface {
    @GET("games")
    public Call<List<Game>> getAll();

    @GET("game")
    public Call<GameDetails> getGameDetails(@Query("id") int id);
}
