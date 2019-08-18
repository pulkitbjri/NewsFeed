package com.example.myapplication.Viewmodels;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import androidx.room.Room;
import com.example.myapplication.Models.MainNewsModel;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.NewsSearchResult;
import com.example.myapplication.Network.NewsClient;
import com.example.myapplication.Network.NewsService;
import com.example.myapplication.database.NewsDatabase;
import com.example.myapplication.providers.NewsRepositoryLocal;
import com.example.myapplication.providers.NewsaRepositoryLocalAndNetwork;

import java.util.concurrent.Executors;

public class NewsListViewModel extends AndroidViewModel {
    private Application application;
    private String countryID="all";
    private String type;
    private MutableLiveData<String> queryLiveData;


    public LiveData<PagedList<News>> getNewss() {
        return newss;
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    private LiveData<NewsSearchResult> newsResult;
    private LiveData<PagedList<News>> newss;
    private LiveData<String> networkErrors;

    public NewsListViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    private void init() {

        NewsRepositoryLocal repositoryLocal=new NewsRepositoryLocal(NewsDatabase.getDatabase(application).newsDao(),
                Executors.newSingleThreadExecutor(),type,countryID);

        NewsaRepositoryLocalAndNetwork repositoryLocalAndNetwork=new NewsaRepositoryLocalAndNetwork(
                NewsClient.getClient().create(NewsService.class),repositoryLocal);

        queryLiveData = new MutableLiveData<String>();

        newsResult = Transformations.map(queryLiveData, repositoryLocalAndNetwork::Search);

        newss = Transformations.switchMap(newsResult, NewsSearchResult::getData);

        networkErrors = Transformations.switchMap(newsResult, NewsSearchResult::getNetworkErrors);

    }


    public void setData(String type, String countryID) {
        this.countryID=countryID;
        this.type=type;

        init();

    }
    public void searchNews(String query){
        queryLiveData.postValue(query);

    }
}
