package com.example.myapplication.Network;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsClient {

    // 6b758068f9be4370a39cd30a4bb86f23

    public static final String LIVE_BASE_URL = "https://newsapi.org/v2/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {

            //Initializing HttpLoggingInterceptor to receive the HTTP event logs

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);


            //Building the HTTPClient with the logger

            OkHttpClient httpClient = new OkHttpClient.Builder()

                    .addInterceptor(loggingInterceptor)

                    .build();



            retrofit = new Retrofit.Builder()
                    .baseUrl(LIVE_BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
