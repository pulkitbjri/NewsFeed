package com.example.myapplication.Viewmodels;

import android.app.Application;
import android.location.Location;
import android.util.Log;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.Models.*;
import com.example.myapplication.Network.NewsClient;
import com.example.myapplication.Network.NewsService;
import com.example.myapplication.Utils.CountryUtil;
import com.example.myapplication.Utils.LocationUtil;
import com.example.myapplication.database.NewsDatabase;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.myapplication.Utils.CountryUtil.getCountryCode;
import static com.example.myapplication.Utils.CountryUtil.getCountryFromLocale;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<String> uIdata=new MutableLiveData<>();
    NewsService service;
    private Application application;
    private LocationUtil locationUtil;
    private String countryID="all";
    private String countryName="";
    MutableLiveData<ArrayList<MainNewsModel>> mainList= new MutableLiveData<>();
    ArrayList<MainNewsModel> typeList=new ArrayList<>();

    public MutableLiveData<ArrayList<MainNewsModel>> getMainList() {
        return mainList;
    }

    {
        MainNewsModel mainViewModel1=new MainNewsModel("Trending Local News","local_trending"
                    , new MutableLiveData<>(),null);
        MainNewsModel mainViewModel2=new MainNewsModel("Trending World News","world_trending"
                , new MutableLiveData<>(),null);
        MainNewsModel mainViewModel3=new MainNewsModel("Everything","everything"
                , new MutableLiveData<>(),null);
        MainNewsModel mainViewModel4=new MainNewsModel("Local Sources","local_sources"
                ,null, new MutableLiveData<>());
        MainNewsModel mainViewModel5=new MainNewsModel("All Sources","sources"
                ,null, new MutableLiveData<>());
        MainNewsModel mainViewModel6=new MainNewsModel("Saved","saved"
                , new MutableLiveData<>(),null);

        mainViewModel1.getNewsList().postValue(new ArrayList<BaseNews>());
        mainViewModel2.getNewsList().postValue(new ArrayList<BaseNews>());
        mainViewModel3.getNewsList().postValue(new ArrayList<BaseNews>());
        mainViewModel4.getSourceList().postValue(new ArrayList<Sources>());
        mainViewModel5.getSourceList().postValue(new ArrayList<Sources>());
        mainViewModel6.getNewsList().postValue(new ArrayList<BaseNews>());

        typeList.add(mainViewModel1);
        typeList.add(mainViewModel2);
        typeList.add(mainViewModel3);
        typeList.add(mainViewModel4);
        typeList.add(mainViewModel5);
        typeList.add(mainViewModel6);

    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        locationUtil=new LocationUtil(application.getApplicationContext());
        mainList.postValue(typeList);
        service= NewsClient.getClient().create(NewsService.class);
    }

    public void requestCountry(boolean isLocPermissionGranted) {
        uIdata.postValue("Fetching Location...");
        if (isLocPermissionGranted)
        {
            locationUtil.getLastLocation().observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Location>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccess(Location location) {
                            getCountryName(CountryUtil.getCountryName(application,location.getLatitude(),location.getLongitude()));
                        }

                        @Override
                        public void onError(Throwable e) {
                            getCountryName(getCountryFromLocale(application.getApplicationContext()));

                        }
                    });

        }
        else
            getCountryName(getCountryFromLocale(application.getApplicationContext()));
    }

    private void getCountryName(String countryNameFull) {
        uIdata.postValue(String.format("Location set to %s", countryNameFull));
        countryID= getCountryCode(countryNameFull);
        countryName= countryNameFull;

        getListData();
    }

    private void getListData() {
        for (int i = 0; i < typeList.size(); i++) {
            switch (typeList.get(i).getType())
            {
                case "local_trending" :
                    getDataDeomAPI(i,service.getTopHeadlines(NewsClient.APIKey,countryID));
                    break;
                case "world_trending" :
                    getDataDeomAPI(i,service.getAllTopHeadlines(NewsClient.APIKey,"en"));
                    break;
                case "everything" :
                    getDataDeomAPI(i,service.getEverything(countryName.toLowerCase(),NewsClient.APIKey));
                    break;
                case "sources" :
                    getSourcesDataDeomAPI(i,service.getAllSources(NewsClient.APIKey));
                    break;
                case "local_sources" :
                    getSourcesDataDeomAPI(i,service.getLocalSources(countryID.toLowerCase(),NewsClient.APIKey));
                    break;
                case "saved" :
                    break;
            }
        }


    }

    private void getDataDeomAPI(int pos, Single<Response<NewsSearchResponse>> topHeadlines) {
        topHeadlines.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<NewsSearchResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<NewsSearchResponse> newsSearchResponseResponse) {
                        Log.i(TAG, "onSuccess: ");
                        List<News> newsList=newsSearchResponseResponse.body().getArticles();

                        for (int i = 0; i < newsList.size(); i++) {
                            newsList.get(i).setType(typeList.get(pos).getType());
                        }
                        Observable.fromCallable(() -> {
                            NewsDatabase.getDatabase(application).newsDao().insertAndDeleteInTransaction(newsSearchResponseResponse.body().getArticles(),
                                    typeList.get(pos).getType());

                            return false;
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((result) -> {
                                    Log.i(TAG, "onSuccess: "+result);
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void getSourcesDataDeomAPI(int pos, Single<Response<SourceSearchResponse>> topHeadlines) {
        topHeadlines.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SourceSearchResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<SourceSearchResponse> newsSearchResponseResponse) {
                        Log.i(TAG, "onSuccess: ");
                        List<Sources> newsList=newsSearchResponseResponse.body().getSources();

                        for (int i = 0; i < newsList.size(); i++) {
                            newsList.get(i).setType(typeList.get(pos).getType());
                        }
                        Observable.fromCallable(() -> {
                            NewsDatabase.getDatabase(application).newsDao().insertAndDeleteInTransactionSources(newsSearchResponseResponse.body().getSources(),
                                    typeList.get(pos).getType());

                            return false;
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((result) -> {
                                    Log.i(TAG, "onSuccess: "+ typeList.get(pos).getType());
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    private void populateExternalRecyclerView() {

    }


    public MutableLiveData<String> getuIdata() {
        return uIdata;
    }

}

