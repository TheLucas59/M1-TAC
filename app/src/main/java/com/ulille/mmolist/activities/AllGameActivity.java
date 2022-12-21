package com.ulille.mmolist.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllGameActivity extends AppCompatActivity {

    private final int TRANSPARENCY_VAL = 64;
    private final int TRANSPARENCY_BASE = 255;
    RecyclerView recyclerViewAllGames;
    AbstractGameAdapter adapterAllGame;
    ImageButton buttonGrid;
    ImageButton buttonList;
    GameViewModel viewModelGames;
    Observable<List<Game>> observableListGames;

    public void setOnClickGrid(View v){

        this.adapterAllGame = new GameAdapterGrid(getApplicationContext());
        this.recyclerViewAllGames.setLayoutManager(
                new GridLayoutManager(
                        AllGameActivity.this, 2));

        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;

        recyclerViewAllGames.setAdapter(this.adapterAllGame);

        buttonGrid.setClickable(false);
        buttonGrid.setEnabled(false);
        buttonList.setClickable(true);
        buttonList.setEnabled(true);
        buttonGrid.getBackground().setAlpha(TRANSPARENCY_VAL);
        buttonList.getBackground().setAlpha(TRANSPARENCY_BASE);

    }

    public void setOnClickList(View v){
        this.adapterAllGame = new GameAdapterList(getApplicationContext());
        recyclerViewAllGames.setLayoutManager(
                new LinearLayoutManager(
                        AllGameActivity.this));

        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;

        recyclerViewAllGames.setAdapter(this.adapterAllGame);
        buttonList.setClickable(false);
        buttonList.setEnabled(false);
        buttonGrid.setClickable(true);
        buttonGrid.setEnabled(true);
        buttonGrid.getBackground().setAlpha(TRANSPARENCY_BASE);
        buttonList.getBackground().setAlpha(TRANSPARENCY_VAL);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapterAllGame = new GameAdapterList(getApplicationContext());
        setContentView(R.layout.v_all_game);
        recyclerViewAllGames = findViewById(R.id.recyclerViewAllGames);

        buttonGrid = findViewById(R.id.buttonGrid);
        buttonGrid.setOnClickListener(this::setOnClickGrid);

        buttonList = findViewById(R.id.buttonList);
        buttonList.setOnClickListener(this::setOnClickList);
        buttonList.setClickable(false);
        buttonList.setEnabled(false);
        buttonList.getBackground().setAlpha(TRANSPARENCY_VAL);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAllGames.setLayoutManager(layoutManager);
        viewModelGames = new ViewModelProvider(this).get(GameViewModel.class);
        this.observableListGames = viewModelGames.getAllGames();
        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;


        recyclerViewAllGames.setAdapter(adapterAllGame);
    }

    private void subscribeAllGames(List<Game> games){
        this.adapterAllGame.setGames(games);
    }

}
