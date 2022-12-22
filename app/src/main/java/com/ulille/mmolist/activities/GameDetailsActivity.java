package com.ulille.mmolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    TextView tvPlatformEdit;
    TextView tvPublisherEdit;
    TextView tvDeveloperEdit;
    TextView tvRequirementEdit;
    ImageButton gameScreenshot1;
    TextView tvCountScreenshot;
    Boolean favorite = false;

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
        buttonFavoriteDetail.setOnClickListener(view -> {
            if (favorite) {
                buttonFavoriteDetail.setImageResource(R.drawable.pngwing_com);
            } else {
                buttonFavoriteDetail.setImageResource(R.drawable.pngwing_com2);
            }
            favorite = !favorite;
        });
    }

    private DisposableSingleObserver<GameDetails> getGameDetailObserver() {
        return new DisposableSingleObserver<GameDetails>() {

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("ERR", e.getMessage());
            }

            @Override
            public void onSuccess(GameDetails game) {
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
                Glide.with(getApplicationContext())
                        .load(screenshots.get(0).getImage())
                        .fitCenter()
                        .into(gameScreenshot1);


                    gameScreenshot1.setOnClickListener(view -> {
                        Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                        intent.putExtra("urisImage", allURIArr);
                        startActivity(intent);
                    });

                tvCategorieEdit.setText(game.getGenre());
                tvDescriptionEdit.setText(HtmlCompat.fromHtml(game.getDescription(),HtmlCompat.FROM_HTML_MODE_LEGACY));
                tvPlatformEdit.setText(game.getPlatform());
                tvPublisherEdit.setText(game.getPublisher());
                tvRequirementEdit.setText(game.getMinimumSystemRequirements());
                tvDeveloperEdit.setText(game.getDeveloper());
                if(screenshots.size() > 0) {
                    tvCountScreenshot.setText("+" + screenshots.size());
                }
            }
        };
    }
}
