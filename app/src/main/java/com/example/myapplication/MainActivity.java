package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapters.MainRVAdapter;
import com.example.myapplication.Models.MainNewsModel;
import com.example.myapplication.Viewmodels.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION = 1;
    final String[] permisstions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    private RecyclerView recyclerView;

    MainViewModel mainViewModel;

    ArrayList<MainNewsModel> list=new ArrayList<>();
    MainRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAndroidPermissions();
        }

        setListners();
        bindAdapter();


//        mainViewModel.startObservingListData();
    }

    private void bindAdapter() {
        adapter=new MainRVAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setListners() {
        mainViewModel.getuIdata().observe(this, s -> {
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        });
        mainViewModel.getMainList().observe(this, mainNewsModels -> {
            list.addAll(mainNewsModels);
            adapter.notifyDataSetChanged();
        });

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
        mainViewModel.requestCountry(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==LOCATION)
        {
            if (grantResults[0]==RESULT_CANCELED)
            {
                mainViewModel.requestCountry(false);

            }
        }
    }
}
