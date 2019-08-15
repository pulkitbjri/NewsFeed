package com.example.myapplication.Viewmodels;

import androidx.lifecycle.ViewModel;
import com.example.myapplication.Utils.CountryUtil;
import com.example.myapplication.Utils.LocationUtil;

public class LocationViewModel extends ViewModel {

    public void requestCountry(boolean isLocPermissionGranted) {
        if (isLocPermissionGranted)
        {

        }
//        else
//            CountryUtil.getCountryFromLocale()
    }
}
