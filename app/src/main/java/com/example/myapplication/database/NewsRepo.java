package com.example.myapplication.database;



import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.*;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.SavedNews;
import com.example.myapplication.Models.Sources;

import java.util.List;

@Dao
public interface NewsRepo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<News> repos);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void like(SavedNews repos);

    @Delete
    void unLike(SavedNews repos);


    @Query("Select * from news where ( (title LIKE :queryString) OR (description LIKE" +
            ":queryString) ) and type = :type ")
    DataSource.Factory<Integer, News> newsByTitle(String type,String queryString);


    @Query("Select * from news where type = :queryString ")
    LiveData<List<News>> newsByType(String queryString);

    @Query("Select * from sources where type = :queryString ")
    LiveData<List<Sources>> sourcesByType(String queryString);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSources(List<Sources> repos);

    @Query("Select * from savednews ")
    LiveData<List<SavedNews>> getSavedNews();


    @Query("Select * from savednews where title LIKE :title  ")
    LiveData<SavedNews> getSavedNewsbyTitle(String title);


    @Query("Select * from news where title LIKE :title  ")
    LiveData<News> getNewsbyTitle(String title);


    @Query("Select * from news ")
    LiveData<List<News>> repos();

    @Query("delete from news where type = :queryString ")
    void removeReposByType(String queryString);
    @Query("delete from sources where type = :queryString ")
    void removeSourcesByType(String queryString);

    @Transaction
    default void insertAndDeleteInTransaction(List<News> newProduct, String type) {
        // Anything inside this method runs in a single transaction.
        removeReposByType(type);
        insert(newProduct);

    }

    @Transaction
    default void insertAndDeleteInTransactionSources(List<Sources> newProduct, String type) {
        // Anything inside this method runs in a single transaction.
        removeSourcesByType(type);
        insertSources(newProduct);

    }

}
