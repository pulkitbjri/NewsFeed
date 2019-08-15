package com.example.myapplication.database;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.myapplication.Models.News;

@Database(
        entities = {News.class},
        version = 1,
        exportSchema = false
)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsRepo newsDao();

    private static NewsDatabase newsDatabase = null;

    public static NewsDatabase getDatabase(final Context context) {

        if (newsDatabase == null) {
            synchronized (NewsDatabase.class) {
                if (newsDatabase == null) {

                    newsDatabase = Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, "Github.db")
                            .build();

                }
            }
        }

        return newsDatabase;
    }


}
