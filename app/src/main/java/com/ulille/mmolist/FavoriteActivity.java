package com.ulille.mmolist;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FavoriteActivity extends AppCompatActivity {
    private final String TITLE = getString(R.string.favoriteTitle);
    TextView titlePageGames;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_all_game);
        titlePageGames = findViewById(R.id.titlePageGames);
        titlePageGames.setText(TITLE);
    }
}
