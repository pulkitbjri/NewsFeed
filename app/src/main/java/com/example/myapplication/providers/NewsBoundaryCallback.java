package com.example.myapplication.providers;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.NewsSearchResponse;
import com.example.myapplication.Network.NewsClient;
import com.example.myapplication.Network.NewsService;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class NewsBoundaryCallback extends PagedList.BoundaryCallback{

    private String query;
    private NewsService newsService;
    private NewsRepositoryLocal newsRepositoryLocal;
    private MutableLiveData<String> networkErrors;

    private int lastRequestedPage = 1;
    private boolean isRequestInProgress = false;

    public NewsBoundaryCallback(String query, NewsService service, NewsRepositoryLocal repositoryLocal){
        super();
        this.query=query;
        this.newsService=service;
        this.newsRepositoryLocal=repositoryLocal;
        networkErrors = new MutableLiveData<>();
    }

    public MutableLiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    @Override
    public void onZeroItemsLoaded() {
        requestAndSaveData(query);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
        requestAndSaveData(query);
    }

    private void requestAndSaveData(String query) {
        if (isRequestInProgress)
            return;

        isRequestInProgress = true;

        retrofit2.Call<NewsSearchResponse> callback = getService();
        callback.enqueue(new Callback<NewsSearchResponse>() {
            @Override
            public void onResponse(retrofit2.Call<NewsSearchResponse> call, Response<NewsSearchResponse> response) {

                if (response.isSuccessful()) {
                    lastRequestedPage++;
                    List<News> news = response.body().getArticles();
                    for (int i = 0; i < news.size() ; i++) {
                        news.get(i).setType(newsRepositoryLocal.type);
                    }
                    newsRepositoryLocal.insert(news, () -> {
                        isRequestInProgress = false;
                    });

                } else {
                    networkErrors.postValue(response.errorBody().toString());
                    isRequestInProgress = false;
                }


            }

            @Override
            public void onFailure(retrofit2.Call<NewsSearchResponse> call, Throwable t) {

                networkErrors.postValue(t.getMessage());
                isRequestInProgress = false;
            }
        });

    }

    private retrofit2.Call<NewsSearchResponse> getService(){
        if (newsRepositoryLocal.type.equalsIgnoreCase("local_trending"))
            return newsService.searchNews(NewsClient.APIKey,query, newsRepositoryLocal.countryID,lastRequestedPage);
        else if (newsRepositoryLocal.type.equalsIgnoreCase("world_trending"))
        {
            return newsService.searchNewsAll(NewsClient.APIKey,query, "en",lastRequestedPage);
        }
        else
            return newsService.everything(NewsClient.APIKey,query,lastRequestedPage);

    }
}
