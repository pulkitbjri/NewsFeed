package com.example.myapplication.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CountryUtil {
    public static String getCountryCode(String name){
        JsonParser parser = new JsonParser();

        JsonArray countryCodes = parser.parse(CountryCodes.jsonArray).getAsJsonArray();
        for (JsonElement countryCode : countryCodes) {
            JsonObject object = countryCode.getAsJsonObject();
            if (object.get("name").getAsString().equalsIgnoreCase(name))
                return object.get("iso").getAsString();
        }
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
        } catch (IOException ioe) {

        }
        return null;
    }
    public static String getCountryFromLocale(Context context)
    {
        return context.getResources().getConfiguration().locale.getDisplayCountry();
    }
}
