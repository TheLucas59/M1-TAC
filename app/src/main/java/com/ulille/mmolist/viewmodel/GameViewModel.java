package com.ulille.mmolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;
import com.ulille.mmolist.repositories.APIRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class GameViewModel extends AndroidViewModel {

    private APIRepository apiRepository;

    public GameViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository();
    }

    public Observable<List<Game>> getAllGames() {
        return apiRepository.getAll();
    }

    public Single<GameDetails> getGameDetails(int id) {
        return apiRepository.getGameDetails(id);
    }
}