package com.ulille.mmolist.repositories;

import com.ulille.mmolist.api.MMOClient;
import com.ulille.mmolist.api.MMOInterface;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class APIRepository {
    private static final MMOInterface apiInterface = MMOClient.getRetrofit().create(MMOInterface.class);

    public Single<List<Game>> getAll() {
        return apiInterface.getAll();
    }

    public Single<GameDetails> getGameDetails(int id) {
        return apiInterface.getGameDetails(id);
    }
}
