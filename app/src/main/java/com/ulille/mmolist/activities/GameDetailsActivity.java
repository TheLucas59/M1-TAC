package com.ulille.mmolist.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ulille.mmolist.R;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.api.model.GameDetails;
import com.ulille.mmolist.api.model.Screenshot;
import com.ulille.mmolist.viewmodel.GameViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import util.Constant;

public class GameDetailsActivity extends AppCompatActivity {
    TextView gameTitle;
    GameViewModel viewModelGames;
    ImageButton buttonFavoriteDetail;
    ImageView gameThumbnail;
    TextView tvCategorieEdit;
    TextView tvDescriptionEdit;
    TextView tvPlatformEdit;
    TextView tvPublisherEdit;
    TextView tvDeveloperEdit;
    TextView tvRequirementEdit;
    ImageButton gameScreenshot1;
    TextView tvCountScreenshot;
    Boolean favorite = false;
    Game game;
    int idGame = -1;
    int position = 0;

    /**
     * Retrieve information from savedInstanceState about the game, and retrieve component in the view.
     * Set button as favorite if it's one
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Gson gson = new Gson();
            game = gson.fromJson(extras.getString(Constant.GAME), Game.class);
            idGame = game.getId();
            favorite = game.isFavorite();
            position = extras.getInt(Constant.POSITION);
        } else {
            String errMess = "Unable to fetch this game";
            Toast.makeText(this, errMess, Toast.LENGTH_LONG).show();
        }

        viewModelGames = new ViewModelProvider(this).get(GameViewModel.class);

        buttonFavoriteDetail = findViewById(R.id.buttonFavoriteDetail);
        gameTitle = findViewById(R.id.gameTitle);
        gameThumbnail = findViewById(R.id.gameThumbnail);

        tvCategorieEdit = findViewById(R.id.tvCategorieEdit);
        tvDescriptionEdit = findViewById(R.id.tvDescriptionEdit);

        tvPublisherEdit = findViewById(R.id.tvPublisherEdit);
        tvPlatformEdit = findViewById(R.id.tvPlatformEdit);
        tvRequirementEdit = findViewById(R.id.tvRequirementEdit);
        tvDeveloperEdit = findViewById(R.id.tvDeveloperEdit);

        tvCountScreenshot = findViewById(R.id.countScreenshot);

        gameScreenshot1 = findViewById(R.id.gameScreenshot1);

        viewModelGames.getGameDetails(idGame).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(getGameDetailObserver());

        if(favorite) {
            buttonFavoriteDetail.setImageResource(R.drawable.pngwing_com2);
        }
        else {
            buttonFavoriteDetail.setImageResource(R.drawable.pngwing_com);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backCallback();
            }
        });
    }

    /**
     * Feed the view with details when they are available
     * @return
     */
    private DisposableSingleObserver<GameDetails> getGameDetailObserver() {
        return new DisposableSingleObserver<GameDetails>() {

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(GameDetailsActivity.this,
                        "You are not connected to the internet.", Toast.LENGTH_LONG).show();

                gameTitle.setText(game.getTitle());
                tvDescriptionEdit.setText(HtmlCompat.fromHtml(game.getShortDescription(),HtmlCompat.FROM_HTML_MODE_LEGACY));
                tvCategorieEdit.setText(game.getGenre());
                tvPlatformEdit.setText(game.getPlatform());
                tvPublisherEdit.setText(game.getPublisher());
                tvDeveloperEdit.setText(game.getDeveloper());
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(GameDetails game) {
                gameTitle.setText(game.getTitle());
                Glide.with(getApplicationContext())
                        .load(game.getThumbnail())
                        .fitCenter()
                        .into(gameThumbnail);

                List<Screenshot> screenshots = game.getScreenshots();
                if(screenshots.size() > 0) {
                    String[] allURIArr = new String[screenshots.size()];
                    for (int i = 0; i < screenshots.size(); i++) {
                        allURIArr[i] = screenshots.get(i).getImage();
                    }

                    Glide.with(getApplicationContext())
                            .load(screenshots.get(0).getImage())
                            .fitCenter()
                            .into(gameScreenshot1);

                    gameScreenshot1.setOnClickListener(view -> {
                        Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                        intent.putExtra(Constant.URISIMAGE, allURIArr);
                        startActivity(intent);
                    });
                }
                else {
                    gameScreenshot1.setImageResource(R.drawable.questionmark);
                }

                tvCategorieEdit.setText(game.getGenre());
                tvDescriptionEdit.setText(HtmlCompat.fromHtml(game.getDescription(),HtmlCompat.FROM_HTML_MODE_LEGACY));
                tvPlatformEdit.setText(game.getPlatform());
                tvPublisherEdit.setText(game.getPublisher());
                tvRequirementEdit.setText(game.getMinimumSystemRequirements());
                tvDeveloperEdit.setText(game.getDeveloper());
                if(screenshots.size() > 0) {
                    tvCountScreenshot.setText("+" + screenshots.size());
                }

                buttonFavoriteDetail.setOnClickListener(view -> {
                    if (favorite) {
                        buttonFavoriteDetail.setImageResource(R.drawable.pngwing_com);
                        viewModelGames.deleteFavorite(GameDetails.getGameFromGameDetails(game));
                    } else {
                        buttonFavoriteDetail.setImageResource(R.drawable.pngwing_com2);
                        viewModelGames.insertFavorite(GameDetails.getGameFromGameDetails(game));
                    }
                    favorite = !favorite;
                });
            }
        };
    }

    public void backCallback() {
        Intent intent = new Intent();
        intent.putExtra(Constant.FAVORITE, favorite);
        intent.putExtra(Constant.IDGAME, idGame);
        intent.putExtra(Constant.POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
