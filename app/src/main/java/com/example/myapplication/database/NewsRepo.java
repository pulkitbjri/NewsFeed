package com.example.myapplication.database;



import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.*;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.SavedNews;

import java.util.List;

@Dao
public interface NewsRepo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<News> repos);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void like(SavedNews repos);

    @Delete
    void unLike(SavedNews repos);


    @Query("Select * from news where (title LIKE :queryString) OR (description LIKE" +
            ":queryString) ")
    DataSource.Factory<Integer, News> reposByTitle(String queryString);

    @Query("Select * from news where type = :queryString ")
    LiveData<List<News>> reposByType(String queryString);


    @Query("Select * from savednews LIMIT 20")
    LiveData<List<SavedNews>> getSavedNews();

    @Query("Select * from news ")
    LiveData<List<News>> repos();

    @Query("delete from news where type = :queryString ")
    void removeReposByType(String queryString);

    @Transaction
    default void insertAndDeleteInTransaction(List<News> newProduct, String type) {
        // Anything inside this method runs in a single transaction.
        removeReposByType(type);
        insert(newProduct);

    }

}
