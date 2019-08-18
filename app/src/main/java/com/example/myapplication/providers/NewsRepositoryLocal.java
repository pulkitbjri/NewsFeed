package com.example.myapplication.providers;

import androidx.paging.DataSource;
import com.example.myapplication.interfaces.InsertFinished;
import com.example.myapplication.Models.News;
import com.example.myapplication.database.NewsRepo;

import java.util.List;
import java.util.concurrent.Executor;


//Class that handles the DAO local data source.
public class NewsRepositoryLocal {

    public String type;
    private NewsRepo newsRepo;
    private Executor executor;
    public String countryID;
    public NewsRepositoryLocal(NewsRepo dao, Executor exe, String type, String countryID){
        this.newsRepo=dao;
        this.executor=exe;
        this.type=type;
        this.countryID=countryID;
    }

    public void insert(final List<News> repos, final InsertFinished finished){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                newsRepo.insert(repos);
                finished.insertFinished();
            }
        });
    }


    public DataSource.Factory<Integer, News> reposByTitle(String query){
        return newsRepo.newsByTitle(type,"%" + query.replace(' ', '%') + "%");
    }


}
