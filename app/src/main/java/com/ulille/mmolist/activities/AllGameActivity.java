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

import com.google.gson.Gson;
import com.ulille.mmolist.R;
import com.ulille.mmolist.adapters.AbstractGameAdapter;
import com.ulille.mmolist.adapters.GameAdapterGrid;
import com.ulille.mmolist.adapters.GameAdapterList;
import com.ulille.mmolist.api.model.Game;
import com.ulille.mmolist.viewmodel.GameViewModel;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import util.Constant;
import util.NeedInternet;

/**
 * Class AllGameActivity: Activity that can be either FavoriteGame or AllGame
 */
public class AllGameActivity extends AppCompatActivity implements NeedInternet {

    SearchView searchView;
    RecyclerView recyclerViewAllGames;
    AbstractGameAdapter<?> adapterAllGame;
    ImageButton buttonGrid;
    ImageButton buttonList;
    GameViewModel viewModelGames;
    Single<List<Game>> observableListGames;
    String layout = "";
    int position = 0;
    ActivityResultLauncher<Intent> detailsActivityLauncher;
    String activityName;

    /**
     * Listener for button grid, change the recyclerViewLayout, and the adapter to GameAdapterGrid
     */
    public void setOnClickGrid() {
        if(checkInternet()) {
            this.adapterAllGame = new GameAdapterGrid(this, activityName);
            position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
            layout = Constant.LAYOUT_GRID;
            this.recyclerViewAllGames.setLayoutManager(
                    new GridLayoutManager(
                            AllGameActivity.this, 2));

            observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::subscribeAllGames);
            recyclerViewAllGames.setAdapter(this.adapterAllGame);
            changeButtonClickable(layout);
        }
    }

    /**
     * Listener for button list, change the recyclerViewLayout, and the adapter to GameAdapterList
     */
    public void setOnClickList() {
        if(checkInternet()) {
            this.adapterAllGame = new GameAdapterList(this, activityName);
            position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
            layout = Constant.LAYOUT_LIST;
            recyclerViewAllGames.setLayoutManager(
                    new LinearLayoutManager(
                            AllGameActivity.this));

            observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::subscribeAllGames);

            recyclerViewAllGames.setAdapter(this.adapterAllGame);

            changeButtonClickable(layout);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.activityName = intent.getExtras().getString(Constant.EXTRA_ACTIVITY_NAME);

        if (savedInstanceState != null) {
            layout = savedInstanceState.getString(Constant.LAYOUT_KEY);
            position = savedInstanceState.getInt(Constant.POSITION_KEY);
        }
        setContentView(R.layout.all_game);
        recyclerViewAllGames = findViewById(R.id.recyclerViewAllGames);

        buttonGrid = findViewById(R.id.buttonGrid);
        buttonGrid.setOnClickListener(v -> setOnClickGrid());

        buttonList = findViewById(R.id.buttonList);
        buttonList.setOnClickListener(v -> setOnClickList());

        searchView = findViewById(R.id.searchView);

        RecyclerView.LayoutManager layoutManager;
        //Restore the previous layout
        if (layout != null && layout.equals(Constant.LAYOUT_GRID)) {
            layoutManager = new GridLayoutManager(this, 2);
            changeButtonClickable(layout);
            this.adapterAllGame = new GameAdapterGrid(this, this.activityName);
        } else {
            layoutManager = new LinearLayoutManager(this);
            changeButtonClickable(layout);
            this.adapterAllGame = new GameAdapterList(this, this.activityName);
        }

        recyclerViewAllGames.setLayoutManager(layoutManager);
        viewModelGames = new ViewModelProvider(this).get(GameViewModel.class);

        //Check if it's favorite page or AllGame page
        if (activityName.equals(Constant.EXTRAS_ALL_GAME)) {
            this.observableListGames = viewModelGames.getAllGamesAndFavoriteMerged();
            searchView.setVisibility(View.INVISIBLE);
        } else if (activityName.equals(Constant.EXTRAS_FAVORITE)) {
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
                        if (data != null) {
                            int position = data.getIntExtra(Constant.POSITION, 0);
                            boolean favorite = data.getBooleanExtra(Constant.FAVORITE, false);
                            List<Game> games = adapterAllGame.getOriginalList();
                            Game game = games.get(position);
                            game.setFavorite(favorite);
                            adapterAllGame.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(this, "Une erreur est survenue.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Resubmit the search, and clear focus of searchView to avoid overlapping
     */
    @Override
    protected void onResume() {
        super.onResume();
        searchView.setQuery(searchView.getQuery(), true);
        searchView.clearFocus();
    }

    /**
     * @param games the list that will be fed to adapter
     */
    private void subscribeAllGames(List<Game> games) {
        this.adapterAllGame.setGames(games);
        recyclerViewAllGames.scrollToPosition(position);
        this.searchView.setQuery(this.searchView.getQuery(), true);
    }

    /**
     * Insert the given game in local database
     *
     * @param game the game to mark as favorite
     */
    public void insertFavorite(Game game) {
        this.viewModelGames.insertFavorite(game);
    }

    /**
     * Delete the given game in local database
     *
     * @param game the game to unmark as favorite
     */
    public void deleteFavorite(Game game) {
        this.viewModelGames.deleteFavorite(game);
    }

    /**
     * Save position, and layout style to restore them in the on create
     *
     * @param outState Bundle in which we save what we want
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int position = this.recyclerViewAllGames.getChildAdapterPosition(this.recyclerViewAllGames.getChildAt(0));
        outState.putInt(Constant.POSITION_KEY, position);
        outState.putCharSequence(Constant.LAYOUT_KEY, layout);
        super.onSaveInstanceState(outState);
    }

    /**
     * Change transparency and clickable param of the previous layout, allowing to click only on other than current layout
     *
     * @param layout the string representing the current layout, either grid or list
     */
    private void changeButtonClickable(String layout) {
        final int TRANSPARENCY_VAL = 64;
        final int TRANSPARENCY_BASE = 255;
        if (layout.equals(Constant.LAYOUT_GRID)) {
            buttonGrid.getBackground().setAlpha(TRANSPARENCY_VAL);
            buttonList.getBackground().setAlpha(TRANSPARENCY_BASE);
            buttonGrid.setClickable(false);
            buttonGrid.setEnabled(false);
            buttonList.setClickable(true);
            buttonList.setEnabled(true);
        } else {
            buttonGrid.getBackground().setAlpha(TRANSPARENCY_BASE);
            buttonList.getBackground().setAlpha(TRANSPARENCY_VAL);
            buttonGrid.setClickable(true);
            buttonGrid.setEnabled(true);
            buttonList.setClickable(false);
            buttonList.setEnabled(false);
        }
    }

    /**
     * Open DetailsActivity for the given game
     *
     * @param game     The game that we want details of
     * @param position Position of the game in the list, so we can scroll back to it on return
     */
    public void openDetailsActivity(Game game, int position) {
        Gson gson = new Gson();
        Intent detailsActivityIntent = new Intent(this, GameDetailsActivity.class);
        detailsActivityIntent.putExtra(Constant.IDGAME, game.getId());

        String gameJson = gson.toJson(game);
        detailsActivityIntent.putExtra(Constant.GAME, gameJson);

        detailsActivityIntent.putExtra(Constant.FAVORITE, game.isFavorite());
        detailsActivityIntent.putExtra(Constant.POSITION, position);
        detailsActivityLauncher.launch(detailsActivityIntent);
    }

    /**
     * Listener for searchBox in favorite Activity
     */
    final SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String search) {
            List<Game> originalList = adapterAllGame.getOriginalList();
            if (search.equals("")) {
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

    public boolean checkInternet() {
        if(Constant.FAVORITE.equals(this.activityName)) {
            return true;
        }
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            if (ipAddr.equals("")) {
                Toast.makeText(AllGameActivity.this, "You don't have access to internet", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        catch(Exception e) {
            Toast.makeText(AllGameActivity.this, "You don't have access to internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
