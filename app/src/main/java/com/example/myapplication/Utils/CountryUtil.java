package com.example.myapplication.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CountryUtil {
    public static String getCountryCode(String name){

        return "all";
    }
    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
        } catch (IOException ioe) { }
        return null;
    }
    public static String getCountryFromLocale(Context context)
    {
        return context.getResources().getConfiguration().locale.getDisplayCountry();
    }
}
