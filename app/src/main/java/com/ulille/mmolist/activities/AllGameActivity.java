package com.ulille.mmolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ulille.mmolist.R;
import com.ulille.mmolist.adapters.AbstractGameAdapter;
import com.ulille.mmolist.adapters.GameAdapterGrid;
import com.ulille.mmolist.adapters.GameAdapterList;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.viewmodel.GameViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllGameActivity extends AppCompatActivity {

    private final String LAYOUT_GRID = "grid";
    private final String LAYOUT_LIST = "list";
    private final String LAYOUT_KEY = "layoutKey";
    private final String POSITION_KEY = "positionKey";
    RecyclerView recyclerViewAllGames;
    AbstractGameAdapter adapterAllGame;
    ImageButton buttonGrid;
    ImageButton buttonList;
    GameViewModel viewModelGames;
    Single<List<Game>> observableListGames;
    String layout = "";
    int position = 0;
    ActivityResultLauncher<Intent> detailsActivityLauncher;

    public void setOnClickGrid(View v){
        position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
        layout = LAYOUT_GRID;
        this.adapterAllGame = new GameAdapterGrid(this);
        this.recyclerViewAllGames.setLayoutManager(
                new GridLayoutManager(
                        AllGameActivity.this, 2));

        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;

        recyclerViewAllGames.setAdapter(this.adapterAllGame);
        changeButtonClickable(layout);
    }

    public void setOnClickList(View v){
        this.adapterAllGame = new GameAdapterList(this);
        position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
        layout = LAYOUT_LIST;
        recyclerViewAllGames.setLayoutManager(
                new LinearLayoutManager(
                        AllGameActivity.this));

        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;

        recyclerViewAllGames.setAdapter(this.adapterAllGame);
        changeButtonClickable(layout);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            layout = savedInstanceState.getString(LAYOUT_KEY);
            position = savedInstanceState.getInt(POSITION_KEY);
        }
        setContentView(R.layout.all_game);
        recyclerViewAllGames = findViewById(R.id.recyclerViewAllGames);

        buttonGrid = findViewById(R.id.buttonGrid);
        buttonGrid.setOnClickListener(this::setOnClickGrid);

        buttonList = findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this::setOnClickList);

        RecyclerView.LayoutManager layoutManager;
        if(layout != null && layout.equals(LAYOUT_GRID)){
            layoutManager = new GridLayoutManager(this, 2);
            changeButtonClickable(layout);
            this.adapterAllGame = new GameAdapterGrid(this);
        }else{
            layoutManager = new LinearLayoutManager(this);
            changeButtonClickable(layout);
            this.adapterAllGame = new GameAdapterList(this);
        }

        recyclerViewAllGames.setLayoutManager(layoutManager);
        viewModelGames = new ViewModelProvider(this).get(GameViewModel.class);
        this.observableListGames = viewModelGames.getAllGamesAndFavoriteMerged();
        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;

        recyclerViewAllGames.setAdapter(adapterAllGame);

        detailsActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null) {
                            int position = data.getIntExtra("position", 0);
                            boolean favorite = data.getBooleanExtra("favorite", false);
                            List<Game> games = adapterAllGame.getGames();
                            Game game = games.get(position);
                            game.setFavorite(favorite);
                            adapterAllGame.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void subscribeAllGames(List<Game> games){
        this.adapterAllGame.setGames(games);
        recyclerViewAllGames.scrollToPosition(position);
    }

    public void insertFavorite(Game game) {
        this.viewModelGames.insertFavorite(game);
    }

    public void deleteFavorite(Game game) {
        this.viewModelGames.deleteFavorite(game);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
        outState.putInt(POSITION_KEY, position);
        outState.putCharSequence(LAYOUT_KEY, layout);
        super.onSaveInstanceState(outState);
    }

    private void changeButtonClickable(String layout){
        final int TRANSPARENCY_VAL = 64;
        final int TRANSPARENCY_BASE = 255;
        if(layout.equals(LAYOUT_GRID)){
            buttonGrid.getBackground().setAlpha(TRANSPARENCY_VAL);
            buttonList.getBackground().setAlpha(TRANSPARENCY_BASE);
            buttonGrid.setClickable(false);
            buttonGrid.setEnabled(false);
            buttonList.setClickable(true);
            buttonList.setEnabled(true);
        }else{
            buttonGrid.getBackground().setAlpha(TRANSPARENCY_BASE);
            buttonList.getBackground().setAlpha(TRANSPARENCY_VAL);
            buttonGrid.setClickable(true);
            buttonGrid.setEnabled(true);
            buttonList.setClickable(false);
            buttonList.setEnabled(false);
        }
    }

    public void openDetailsActivity(Game game, int position) {
        Intent detailsActivityIntent = new Intent(this, GameDetailsActivity.class);
        detailsActivityIntent.putExtra("idGame", game.getId());
        detailsActivityIntent.putExtra("favorite", game.isFavorite());
        detailsActivityIntent.putExtra("position", position);
        detailsActivityLauncher.launch(detailsActivityIntent);
    }
}
