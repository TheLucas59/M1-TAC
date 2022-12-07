package com.ulille.mmolist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    GameAdapter adapterAllGame;
    ImageButton buttonGrid;
    ImageButton buttonList;
    GameViewModel viewModelGames;

    View.OnClickListener onClickGrid = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            recyclerViewAllGames.getAdapter().getItemViewType(R.layout.v_gamecard_grid);
            recyclerViewAllGames.setLayoutManager(
                    new GridLayoutManager(
                            AllGameActivity.this, GridLayoutManager.DEFAULT_SPAN_COUNT));
            buttonGrid.setClickable(false);
            buttonGrid.setEnabled(false);
            buttonList.setClickable(true);
            buttonList.setEnabled(true);
            buttonGrid.getBackground().setAlpha(TRANSPARENCY_VAL);
            buttonList.getBackground().setAlpha(TRANSPARENCY_BASE);

        }
    };

    View.OnClickListener onClickList = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                recyclerViewAllGames.getAdapter().getItemViewType(R.layout.v_gamecard_list);
                recyclerViewAllGames.setLayoutManager(
                        new LinearLayoutManager(
                                AllGameActivity.this, LinearLayoutManager.VERTICAL, false));
                buttonGrid.setClickable(true);
                buttonGrid.setEnabled(true);
                buttonList.setClickable(false);
                buttonList.setEnabled(false);
                buttonList.getBackground().setAlpha(TRANSPARENCY_VAL);
                buttonGrid.getBackground().setAlpha(TRANSPARENCY_BASE);
            }
        };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapterAllGame = new GameAdapter();
        setContentView(R.layout.v_all_game);
        recyclerViewAllGames = findViewById(R.id.recyclerViewAllGames);

        buttonGrid = findViewById(R.id.buttonGrid);
        buttonGrid.setOnClickListener(onClickGrid);
        buttonList = findViewById(R.id.buttonList);
        buttonList.setOnClickListener(onClickList);
        buttonList.setClickable(false);
        buttonList.setEnabled(false);
        buttonList.getBackground().setAlpha(TRANSPARENCY_VAL);

        recyclerViewAllGames.setLayoutManager(new LinearLayoutManager(this));

        viewModelGames = new ViewModelProvider(this).get(GameViewModel.class);
        Observable<List<Game>> observableListGames = viewModelGames.getAllGames();
        observableListGames.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeAllGames) ;
        recyclerViewAllGames.setAdapter(adapterAllGame);
    }

    private void subscribeAllGames(List<Game> games){
        this.adapterAllGame.setGames(games);
    }


}
