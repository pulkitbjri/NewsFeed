package com.example.myapplication.Utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.util.concurrent.Callable;

public class LocationUtil {
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;

    public LocationUtil(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public Single<Location> getLastLocation()
    {
        return Single.create(singleEmitter -> {
            if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                singleEmitter.onError(null);
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(singleEmitter::onSuccess)
                    .addOnFailureListener(e -> singleEmitter.onError(null));
        });
    }
}