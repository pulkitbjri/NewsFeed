package com.example.myapplication.Network;


import com.example.myapplication.Models.NewsSearchResponse;
import com.example.myapplication.Models.SourceSearchResponse;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {


    @GET("top-headlines")
    Call<NewsSearchResponse> searchNews(@Query("apiKey") String api,@Query("q") String query,
                                                    @Query("country") String country,@Query("page") int page);
    @GET("top-headlines")
    Call<NewsSearchResponse> searchNewsAll(@Query("apiKey") String api,@Query("q") String query,
                                           @Query("language") String language ,@Query("page") int page);

    @GET("top-headlines")
    Call<NewsSearchResponse> everything(@Query("apiKey") String api,@Query("q") String query, @Query("page") int page);



    @GET("top-headlines")
    Single<Response<NewsSearchResponse>> getTopHeadlines(@Query("apiKey") String api,@Query("country") String country);
    @GET("top-headlines")
    Single<Response<NewsSearchResponse>> getAllTopHeadlines(@Query("apiKey") String api,@Query("language") String language);
    @GET("everything")
    Single<Response<NewsSearchResponse>> getEverything(@Query("q") String query, @Query("apiKey") String api);
    @GET("sources")
    Single<Response<SourceSearchResponse>> getAllSources(@Query("apiKey") String api);
    @GET("sources")
    Single<Response<SourceSearchResponse>> getLocalSources(@Query("country") String country, @Query("apiKey") String api);

}
