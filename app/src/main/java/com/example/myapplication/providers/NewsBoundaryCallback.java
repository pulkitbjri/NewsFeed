package com.example.myapplication.providers;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.NewsSearchResponse;
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
    private final int NETWORK_PAGE_SIZE = 50;

    public NewsBoundaryCallback(String query, NewsService service, NewsRepositoryLocal repositoryLocal){

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

        query = query + "in:name,description";
        isRequestInProgress = true;

        retrofit2.Call<NewsSearchResponse> callback = newsService.searchNews(query, "");
        callback.enqueue(new Callback<NewsSearchResponse>() {
            @Override
            public void onResponse(retrofit2.Call<NewsSearchResponse> call, Response<NewsSearchResponse> response) {

                if (response.isSuccessful()) {
                    List<News> repos = response.body().getArticles();
                    newsRepositoryLocal.insert(repos, () -> {
                        lastRequestedPage++;
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
                Log.d("HERE1", "hereabc");

            }
        });

    }
}
