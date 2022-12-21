package com.ulille.mmolist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ulille.mmolist.api.model.GameDetails;
import com.ulille.mmolist.database.dao.GameDetailsDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GameDetails.class}, version = 1, exportSchema = false)
public abstract class GameDatabase extends RoomDatabase {

    public abstract GameDetailsDAO gameDetailsDAO();

    private static volatile GameDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GameDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GameDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GameDatabase.class, "game_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
