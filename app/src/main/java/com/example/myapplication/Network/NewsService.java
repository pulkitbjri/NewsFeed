package com.example.myapplication.Network;


import com.example.myapplication.Models.NewsSearchResponse;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {


    @GET("top-headlines")
    Call<NewsSearchResponse> searchNews(@Query("q") String query,

                                                    @Query("country") String country);



    @GET("top-headlines")
    Single<Response<NewsSearchResponse>> getTopHeadlines(@Query("apiKey") String api,@Query("country") String country);
    @GET("top-headlines")
    Single<Response<NewsSearchResponse>> getAllTopHeadlines(@Query("apiKey") String api,@Query("language") String language);
    @GET("everything")
    Single<Response<NewsSearchResponse>> getEverything(@Query("q") String query, @Query("apiKey") String api);
    @GET("sources")
    Single<Response<NewsSearchResponse>> getAllSources( @Query("apiKey") String api);
    @GET("sources")
    Single<Response<NewsSearchResponse>> getLocalSources(@Query("country") String country, @Query("apiKey") String api);

}
