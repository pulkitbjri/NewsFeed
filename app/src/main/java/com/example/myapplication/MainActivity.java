package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION = 1;
    public static final int DAILY_REMINDER_REQUEST_CODE = 10;
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

        setUpAlarm();

//        mainViewModel.startObservingListData();
    }

    private void setUpAlarm() {
        setReminder(this,AlarmReceiver.class,4,57);
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

    public void openListActivity(MainNewsModel mainNewsModel) {
        Intent intent=new Intent(this,NewsListActivity.class);
        intent.putExtra("country",mainViewModel.getCountryID());
        intent.putExtra("type",mainNewsModel.getType());
        intent.putExtra("title",mainNewsModel.getTitle());
        startActivity(intent);

    }


    public static void setReminder(Context context, Class<?> cls, int hour, int min)
    {
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);
        // cancel already scheduled reminders
//        cancelReminder(context,cls);

//        if(setcalendar.before(calendar))
//            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
