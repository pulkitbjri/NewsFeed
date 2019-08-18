package com.example.myapplication.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsSearchResponse {

    @SerializedName("totalResults")
    @Expose
    private Long totalResults;

    @SerializedName("articles")
    @Expose
    private List<News> articles = null;


    public List<News> getArticles() {
        return articles;
    }


}
