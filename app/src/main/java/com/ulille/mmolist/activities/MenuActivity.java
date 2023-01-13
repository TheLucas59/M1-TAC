package com.ulille.mmolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.ulille.mmolist.R;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.viewmodel.GameViewModel;

import java.net.InetAddress;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import util.Constant;
import util.NeedInternet;

/**
 * Allow to navigate between the differents activities, act as a menu
 */
public class MenuActivity extends AppCompatActivity implements NeedInternet {
    Button buttonAllGame;
    Button buttonFavorite;
    Button buttonRandom;
    Button buttonCredit;

    final ActivityResultLauncher<Intent> secondActivityLauncher = createActivityLauncher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        buttonAllGame = findViewById(R.id.buttonAllGame);
        buttonFavorite = findViewById(R.id.buttonFavorite);
        buttonRandom = findViewById(R.id.buttonRandom);
        buttonCredit = findViewById(R.id.buttonCredit);


        buttonAllGame.setOnClickListener(mOnClickAllGame);
        buttonFavorite.setOnClickListener(mOnClickFavorite);
        buttonRandom.setOnClickListener(mOnClickRandom);
        buttonCredit.setOnClickListener(mOnClickCredit);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        checkInternet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
    }

    private final View.OnClickListener mOnClickAllGame = view -> {
        if(checkInternet()) {
            Intent startActivityIntent = new Intent(MenuActivity.this, AllGameActivity.class);
            startActivityIntent.putExtra(Constant.EXTRA_ACTIVITY_NAME, Constant.EXTRAS_ALL_GAME);
            secondActivityLauncher.launch(startActivityIntent);
        }
    };

    private final View.OnClickListener mOnClickFavorite = view -> {
        Intent startActivityIntent = new Intent(MenuActivity.this, AllGameActivity.class);
        startActivityIntent.putExtra(Constant.EXTRA_ACTIVITY_NAME, Constant.EXTRAS_FAVORITE);
        secondActivityLauncher.launch(startActivityIntent);
    };

    private final View.OnClickListener mOnClickRandom = view -> {
        if(checkInternet()) {
            GameViewModel gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

            Single<List<Game>> apiGames = gameViewModel.getAllGamesAndFavoriteMerged();
            apiGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getListGameObserver());
        }
    };

    private final View.OnClickListener mOnClickCredit = view -> {
        Intent startActivityIntent = new Intent(MenuActivity.this, CreditActivity.class);
        secondActivityLauncher.launch(startActivityIntent);
    };

    private ActivityResultLauncher<Intent> createActivityLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    if (resultCode == RESULT_CANCELED) {
                        Log.d("CRASH", "Menu activity crashed");
                    }
                }
        );
    }

    /**
     * Rand a number and try to get it from the API, repeat until it get one, it is used to feed RandomActivity
     * @return A random game
     */
    private DisposableSingleObserver<List<Game>> getListGameObserver() {
        return new DisposableSingleObserver<List<Game>>() {
            @Override
            public void onSuccess(@NonNull List<Game> games) {
                Game randomGame = null;
                while(games != null && (randomGame == null || randomGame.isFavorite())) {
                    Random r = new Random();
                    randomGame = games.get(r.nextInt(games.size()));
                }

                Intent startActivityIntent = new Intent(MenuActivity.this, GameDetailsActivity.class);
                Gson gson = new Gson();
                String gameJson = gson.toJson(randomGame);
                startActivityIntent.putExtra(Constant.GAME, gameJson);
                secondActivityLauncher.launch(startActivityIntent);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getApplicationContext(), "Impossible de récupérer les jeux", Toast.LENGTH_LONG).show();
            }
        };
    }

    public boolean checkInternet() {
        try{
            InetAddress ipAddr = InetAddress.getByName("google.com");
            if(ipAddr.equals("")){
                Toast.makeText(MenuActivity.this, "You don't have access to internet", Toast.LENGTH_SHORT).show();
                buttonAllGame.getBackground().setAlpha(64);
                buttonRandom.getBackground().setAlpha(64);
                return false;
            }
            buttonAllGame.getBackground().setAlpha(255);
            buttonRandom.getBackground().setAlpha(255);
            return true;
        }catch(Exception e){
            Toast.makeText(MenuActivity.this, "You don't have access to internet", Toast.LENGTH_SHORT).show();
            buttonAllGame.getBackground().setAlpha(64);
            buttonRandom.getBackground().setAlpha(64);
            return false;
        }
    }
}
