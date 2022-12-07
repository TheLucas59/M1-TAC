package com.ulille.mmolist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllGameActivity extends AppCompatActivity {

    private final int TRANSPARENCY_VAL = 64;
    private final int TRANSPARENCY_BASE = 255;
    RecyclerView recyclerViewAllGames;
    RecyclerView.Adapter adapterAllGame;
    ImageButton buttonGrid;
    ImageButton buttonList;
    ViewModelProvider viewModelProvider;

    View.OnClickListener onClickGrid = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
        setContentView(R.layout.v_all_game);
        recyclerViewAllGames = findViewById(R.id.recyclerViewAllGames);

        buttonGrid = findViewById(R.id.buttonGrid);
        buttonGrid.setOnClickListener(onClickGrid);
        buttonList = findViewById(R.id.buttonList);
        buttonList.setOnClickListener(onClickList);

        buttonList.setClickable(false);
        buttonList.setEnabled(false);
        buttonList.getBackground().setAlpha(TRANSPARENCY_VAL);


        recyclerViewAllGames.setAdapter(adapterAllGame);
        recyclerViewAllGames.setLayoutManager(new LinearLayoutManager(this));

        //viewModelProvider = new ViewModelProvider(this).get(GameViewModel.class);
    }


}
