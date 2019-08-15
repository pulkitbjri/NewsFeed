package com.example.myapplication.database;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import com.example.myapplication.Models.News;

import java.util.List;

@Dao
public interface NewsRepo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<News> repos);



}
