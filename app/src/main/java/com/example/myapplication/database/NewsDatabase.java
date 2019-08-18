package com.example.myapplication.database;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.SavedNews;

@Database(
        entities = {News.class, SavedNews.class},
        version = 1,
        exportSchema = true
)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsRepo newsDao();

    private static NewsDatabase newsDatabase = null;

    public static NewsDatabase getDatabase(final Context context) {

        if (newsDatabase == null) {
            synchronized (NewsDatabase.class) {
                if (newsDatabase == null) {

                    newsDatabase = Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, "news.db")
                            .build();

                }
            }
        }

        return newsDatabase;
    }


}
