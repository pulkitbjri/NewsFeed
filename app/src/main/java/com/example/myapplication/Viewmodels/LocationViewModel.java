package com.example.myapplication.Viewmodels;

import android.app.Application;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.Utils.CountryUtil;
import com.example.myapplication.Utils.LocationUtil;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocationViewModel extends AndroidViewModel {

    private Application application;
    private LocationUtil locationUtil;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        locationUtil=new LocationUtil(application.getApplicationContext());
    }

    public void requestCountry(boolean isLocPermissionGranted) {
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
                            getCountryName(CountryUtil.getCountryFromLocale(application.getApplicationContext()));

                        }
                    });

        }
        else
            getCountryName(CountryUtil.getCountryFromLocale(application.getApplicationContext()));
    }

    private void getCountryName(String countryNameFull) {
        
    }
}
