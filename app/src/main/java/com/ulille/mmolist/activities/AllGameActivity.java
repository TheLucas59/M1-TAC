package com.ulille.mmolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

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
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import util.Constant;

public class AllGameActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerViewAllGames;
    AbstractGameAdapter adapterAllGame;
    ImageButton buttonGrid;
    ImageButton buttonList;
    GameViewModel viewModelGames;
    Single<List<Game>> observableListGames;
    String layout = "";
    int position = 0;
    ActivityResultLauncher<Intent> detailsActivityLauncher;
    String activityName;

    public void setOnClickGrid(View v){
        this.adapterAllGame = new GameAdapterGrid(this, activityName);
        position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
        layout = Constant.LAYOUT_GRID;
        this.recyclerViewAllGames.setLayoutManager(
                new GridLayoutManager(
                        AllGameActivity.this, 2));

        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;

        recyclerViewAllGames.setAdapter(this.adapterAllGame);
        changeButtonClickable(layout);
    }

    public void setOnClickList(View v){
        this.adapterAllGame = new GameAdapterList(this, activityName);
        position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
        layout = Constant.LAYOUT_LIST;
        recyclerViewAllGames.setLayoutManager(
                new LinearLayoutManager(
                        AllGameActivity.this));

        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;

        recyclerViewAllGames.setAdapter(this.adapterAllGame);

        changeButtonClickable(layout);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.activityName = intent.getExtras().getString(Constant.EXTRA_ACTIVITY_NAME);

        if(savedInstanceState != null){
            layout = savedInstanceState.getString(Constant.LAYOUT_KEY);
            position = savedInstanceState.getInt(Constant.POSITION_KEY);
        }
        setContentView(R.layout.all_game);
        recyclerViewAllGames = findViewById(R.id.recyclerViewAllGames);

        buttonGrid = findViewById(R.id.buttonGrid);
        buttonGrid.setOnClickListener(this::setOnClickGrid);

        buttonList = findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this::setOnClickList);

        searchView = findViewById(R.id.searchView);

        RecyclerView.LayoutManager layoutManager;
        if(layout != null && layout.equals(Constant.LAYOUT_GRID)){
            layoutManager = new GridLayoutManager(this, 2);
            changeButtonClickable(layout);
            this.adapterAllGame = new GameAdapterGrid(this, this.activityName);
        }else{
            layoutManager = new LinearLayoutManager(this);
            changeButtonClickable(layout);
            this.adapterAllGame = new GameAdapterList(this, this.activityName);
        }

        recyclerViewAllGames.setLayoutManager(layoutManager);
        viewModelGames = new ViewModelProvider(this).get(GameViewModel.class);

        if(activityName.equals(Constant.EXTRAS_ALL_GAME)) {
            this.observableListGames = viewModelGames.getAllGamesAndFavoriteMerged();
            searchView.setVisibility(View.INVISIBLE);
        }else if(activityName.equals(Constant.EXTRAS_FAVORITE)) {
            this.observableListGames = viewModelGames.getAllFavoriteGames();
            searchView.setOnQueryTextListener(textListener);
        }
        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames);

        recyclerViewAllGames.setAdapter(adapterAllGame);

        detailsActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null) {
                            int position = data.getIntExtra(Constant.POSITION, 0);
                            boolean favorite = data.getBooleanExtra(Constant.FAVORITE, false);
                            List<Game> games = adapterAllGame.getOriginalList();
                            Game game = games.get(position);
                            game.setFavorite(favorite);
                            adapterAllGame.notifyDataSetChanged();
                        }
                    }
                    else {
                        Toast.makeText(this, "Une erreur est survenue.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setQuery(searchView.getQuery(), true);
        searchView.clearFocus();
    }

    private void subscribeAllGames(List<Game> games){
        this.adapterAllGame.setGames(games);
        recyclerViewAllGames.scrollToPosition(position);
        this.searchView.setQuery(this.searchView.getQuery(), true);
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
        outState.putInt(Constant.POSITION_KEY, position);
        outState.putCharSequence(Constant.LAYOUT_KEY, layout);
        super.onSaveInstanceState(outState);
    }

    private void changeButtonClickable(String layout){
        final int TRANSPARENCY_VAL = 64;
        final int TRANSPARENCY_BASE = 255;
        if(layout.equals(Constant.LAYOUT_GRID)){
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
        detailsActivityIntent.putExtra(Constant.IDGAME, game.getId());
        detailsActivityIntent.putExtra(Constant.FAVORITE, game.isFavorite());
        detailsActivityIntent.putExtra(Constant.POSITION, position);
        detailsActivityLauncher.launch(detailsActivityIntent);
    }

    SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String search) {
            List<Game> originalList = adapterAllGame.getOriginalList();
            if(search.equals("")){
                adapterAllGame.filterList(originalList);
                return true;
            }
            List<Game> filteredList = originalList.stream().
                    filter(game -> game.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
            adapterAllGame.filterList(filteredList);
            return filteredList.size() != 0;
        }

        @Override
        public boolean onQueryTextChange(String search) {
            return onQueryTextSubmit(search);
        }
    };
}
