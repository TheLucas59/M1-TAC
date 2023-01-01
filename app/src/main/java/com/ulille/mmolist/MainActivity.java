package com.ulille.mmolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ulille.mmolist.activities.MenuActivity;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> secondActivityLauncher = createSecondActivityLauncher();
    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(mOnClickStart);
    }

    private ActivityResultLauncher<Intent> createSecondActivityLauncher() {
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

    private final View.OnClickListener mOnClickStart = view -> {
        Intent startActivityIntent = new Intent(MainActivity.this, MenuActivity.class);
        secondActivityLauncher.launch(startActivityIntent);
    };

    }