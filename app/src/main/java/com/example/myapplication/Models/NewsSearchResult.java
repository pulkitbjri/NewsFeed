package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import com.example.myapplication.Models.News;

public class NewsSearchResult {
    private LiveData<PagedList<News>> data;
    private LiveData<String> networkErrors;

    public NewsSearchResult(LiveData<PagedList<News>> data, LiveData<String> networkErrors){

        this.data=data;
        this.networkErrors=networkErrors;
    }

    public LiveData<PagedList<News>> getData() {
        return data;
    }

    public void setData(LiveData<PagedList<News>> data) {
        this.data = data;
    }

    public LiveData<String> getNetworkErrors() {
        return networkErrors;
    }

    public void setNetworkErrors(LiveData<String> networkErrors) {
        this.networkErrors = networkErrors;
    }
}
