package com.example.myapplication.providers;


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.example.myapplication.Network.NewsService;
import com.example.myapplication.Models.NewsSearchResult;

//Repository class that works with local and remote data sources.
public class NewsaRepositoryLocalAndNetwork {

    private final int DATABASE_PAGE_SIZE = 20;

    private NewsService newsService;
    private NewsRepositoryLocal newsRepositoryLocal;


    public NewsaRepositoryLocalAndNetwork(NewsService service, NewsRepositoryLocal repositoryLocal) {
        this.newsRepositoryLocal = repositoryLocal;
        this.newsService = service;

    }

    public NewsSearchResult Search(String query) {
        // Get data from the local cache
        DataSource.Factory dataSourceFactory = newsRepositoryLocal.reposByTitle(query);

        NewsBoundaryCallback boundaryCallback = new NewsBoundaryCallback(query, newsService, newsRepositoryLocal);

        LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();


        //returns LiveData
        LiveData data = new LivePagedListBuilder(dataSourceFactory,
                new PagedList.Config.Builder().setPageSize(20)
                        .setPrefetchDistance(10)
                        .setEnablePlaceholders(true)
                        .build())
                .setBoundaryCallback(boundaryCallback).build();

        return new NewsSearchResult(data, networkErrors);
    }
}
