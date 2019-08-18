package com.example.myapplication.providers;


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.example.myapplication.Models.News;
import com.example.myapplication.Network.NewsService;
import com.example.myapplication.Models.NewsSearchResult;

//Repository class that works with local and remote data sources.
public class NewsaRepositoryLocalAndNetwork {

    private NewsService newsService;
    private NewsRepositoryLocal newsRepositoryLocal;


    public NewsaRepositoryLocalAndNetwork(NewsService service, NewsRepositoryLocal repositoryLocal) {
        this.newsRepositoryLocal = repositoryLocal;
        this.newsService = service;

    }

    public NewsSearchResult Search(String query) {
        // Get data from the local cache
        DataSource.Factory<Integer, News> dataSourceFactory = newsRepositoryLocal.reposByTitle(query);

        NewsBoundaryCallback boundaryCallback = new NewsBoundaryCallback(query, newsService, newsRepositoryLocal);

        LiveData<String> networkErrors = boundaryCallback.getNetworkErrors();

        PagedList.Config config=new PagedList.Config.Builder()
                .setPageSize(10)
                .setPrefetchDistance(3)
                .setEnablePlaceholders(true)
                .build();
        //returns LiveData
        LiveData<PagedList<News>> data = new LivePagedListBuilder(dataSourceFactory, 200)
                .setBoundaryCallback(boundaryCallback)
                .build();

        return new NewsSearchResult(data, networkErrors);
    }
}
