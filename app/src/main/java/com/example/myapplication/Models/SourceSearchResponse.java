package com.example.myapplication.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SourceSearchResponse {


    @SerializedName("sources")
    @Expose
    private List<Sources> sources = null;

    public List<Sources> getSources() {
        return sources;
    }


}
