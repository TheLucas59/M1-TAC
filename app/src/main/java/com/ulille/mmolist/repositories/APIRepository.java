package com.ulille.mmolist.repositories;

import com.ulille.mmolist.api.MMOClient;
import com.ulille.mmolist.api.MMOInterface;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * Class that will be called from JavaCode to interact with the API by passing request through Retrofit
 */
public class APIRepository {
    private static final MMOInterface apiInterface = MMOClient.getRetrofit().create(MMOInterface.class);

    public Single<List<Game>> getAll() {
        return apiInterface.getAll();
    }

    public Single<GameDetails> getGameDetails(int id) {
        return apiInterface.getGameDetails(id);
    }
}
