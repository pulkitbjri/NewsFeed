package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Viewmodels.LocationViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION = 1;
    final String[] permisstions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    private RecyclerView recyclerView;

    LocationViewModel locationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAndroidPermissions();
        }
        bindViews();

        locationViewModel= ViewModelProviders.of(this).get(LocationViewModel.class);


    }

    private void bindViews() {
        recyclerView=findViewById(R.id.recyclerView);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void requestAndroidPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(permisstions,LOCATION);
            return;
        }
        locationViewModel.requestCountry(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==LOCATION)
        {
            if (grantResults[0]==RESULT_CANCELED)
            {

            }
        }
    }
}
