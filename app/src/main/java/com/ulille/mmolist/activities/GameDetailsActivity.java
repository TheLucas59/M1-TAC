package com.ulille.mmolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.ulille.mmolist.R;
import com.ulille.mmolist.api.model.GameDetails;
import com.ulille.mmolist.api.model.Screenshot;
import com.ulille.mmolist.viewmodel.GameViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GameDetailsActivity extends AppCompatActivity {
    TextView gameTitle;
    GameViewModel viewModelGames;
    ImageButton buttonFavoriteDetail;
    ImageView gameThumbnail;
    TextView tvCategorieEdit;
    TextView tvDescriptionEdit;
    ImageButton gameScreenshot1;
    ImageButton gameScreenshot2;
    ImageButton gameScreenshot3;
    ImageButton gameScreenshot4;
    List<ImageButton> gameScreenshots = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_game_details);
        int idGame = -1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idGame = extras.getInt("idGame");
        } else {
            String errMess = "Unable to fetch this game";
            Toast.makeText(this, errMess, Toast.LENGTH_LONG).show();
        }
        Log.d("pouet", idGame + "");

        viewModelGames = new ViewModelProvider(this).get(GameViewModel.class);

        gameTitle = findViewById(R.id.gameTitle);
        gameThumbnail = findViewById(R.id.gameThumbnail);

        tvCategorieEdit = findViewById(R.id.tvCategorieEdit);
        tvDescriptionEdit = findViewById(R.id.tvDescriptionEdit);

        gameScreenshot1 = findViewById(R.id.gameScreenshot1);
        gameScreenshot2 = findViewById(R.id.gameScreenshot2);
        gameScreenshot3 = findViewById(R.id.gameScreenshot3);
        gameScreenshot4 = findViewById(R.id.gameScreenshot4);
        gameScreenshots.addAll(Arrays.asList(gameScreenshot1, gameScreenshot2, gameScreenshot3, gameScreenshot4));
        viewModelGames.getGameDetails(idGame).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(getGameDetailObserver());
    }

    private DisposableSingleObserver<GameDetails> getGameDetailObserver() {
        return new DisposableSingleObserver<GameDetails>() {

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("pouet", e.getMessage());
            }

            @Override
            public void onSuccess(GameDetails game) {
                Log.d("pouet", game.getScreenshots().get(1).getImage() + "");
                gameTitle.setText(game.getTitle());
                Glide.with(getApplicationContext())
                        .load(game.getThumbnail())
                        .fitCenter()
                        .into(gameThumbnail);
                List<Screenshot> screenshots = game.getScreenshots();
                String[] allURIArr = new String[screenshots.size()];
                for (int i = 0; i < screenshots.size(); i++) {
                    allURIArr[i] = screenshots.get(i).getImage();
                }
                for (int i = 0; i < screenshots.size(); i++) {
                    if (i < 3) {
                        Glide.with(getApplicationContext())
                                .load(screenshots.get(i).getImage())
                                .fitCenter()
                                .into(gameScreenshots.get(i));
                    }
                    gameScreenshots.get(i).setOnClickListener(view -> {
                        Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                        intent.putExtra("urisImage", allURIArr);
                        startActivity(intent);
                    });
                }

                tvCategorieEdit.setText(game.getGenre());
                Log.d("pouet", game.getDescription());
                tvDescriptionEdit.setText(HtmlCompat.fromHtml(game.getDescription(),HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        };
    }
}
