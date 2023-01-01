package com.ulille.mmolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ulille.mmolist.R;

public class MenuActivity extends AppCompatActivity {
    Button buttonAllGame;
    Button buttonFavorite;
    Button buttonRandom;
    Button buttonCredit;

    ActivityResultLauncher<Intent> secondActivityLauncher = createActivityLauncher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_main_menu);

        buttonAllGame = findViewById(R.id.buttonAllGame);
        buttonFavorite = findViewById(R.id.buttonFavorite);
        buttonRandom = findViewById(R.id.buttonRandom);
        buttonCredit = findViewById(R.id.buttonCredit);


        buttonAllGame.setOnClickListener(mOnClickAllGame);
        buttonFavorite.setOnClickListener(mOnClickFavorite);
        buttonRandom.setOnClickListener(mOnClickRandom);
        buttonCredit.setOnClickListener(mOnClickCredit);
    }

    private final View.OnClickListener mOnClickAllGame = view -> {
        Intent startActivityIntent = new Intent(MenuActivity.this, AllGameActivity.class);
        secondActivityLauncher.launch(startActivityIntent);
    };

    private final View.OnClickListener mOnClickFavorite = view -> {
        Intent startActivityIntent = new Intent(MenuActivity.this, FavoriteActivity.class);
        secondActivityLauncher.launch(startActivityIntent);
    };

    private final View.OnClickListener mOnClickRandom = view -> {
        Intent startActivityIntent = new Intent(MenuActivity.this, RandomActivity.class);
        secondActivityLauncher.launch(startActivityIntent);
    };

    private final View.OnClickListener mOnClickCredit = view -> {
        Intent startActivityIntent = new Intent(MenuActivity.this, CreditActivity.class);
        secondActivityLauncher.launch(startActivityIntent);
    };



    private ActivityResultLauncher<Intent> createActivityLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    if (resultCode == RESULT_CANCELED) {
                        Log.d("CRASH", "Menu activity crashed");
                    }
                }
        );
    }
}
