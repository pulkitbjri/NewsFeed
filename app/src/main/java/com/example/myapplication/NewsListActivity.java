package com.example.myapplication;

import android.app.Notification;
import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapters.NewsListAdapter;
import com.example.myapplication.Models.MainNewsModel;
import com.example.myapplication.Models.News;
import com.example.myapplication.Viewmodels.MainViewModel;
import com.example.myapplication.Viewmodels.NewsListViewModel;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import java.util.concurrent.TimeUnit;

public class NewsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    NewsListViewModel newsListViewModel;
    String type;
    String countryID;
    NewsListAdapter adapter;
    private TextView emptyList;
    private String title;
    Toolbar toolbar;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        bindViews();
        getIntentValues();

        newsListViewModel = ViewModelProviders.of(this).get(NewsListViewModel.class);
        newsListViewModel.setData(type,countryID);

        setUpRecycler();
        setupListners();

        toolbar.setTitle(title);
        newsListViewModel.searchNews("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

         searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        fromView().debounce(900, TimeUnit.MILLISECONDS)
//                .filter(s -> !s.isEmpty())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String o) {
                        newsListViewModel.searchNews(o);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return super.onCreateOptionsMenu(menu);

    }

    private void setupListners() {

        newsListViewModel.getNewss().observe(this, repos -> {
            showEmptyList(repos.size()==0);
            adapter.submitList(repos);

        });

        newsListViewModel.getNetworkErrors().observe(this, s -> Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show());
        toolbar.setNavigationOnClickListener(view -> onBackPressed());


    }

    private void showEmptyList(boolean show) {


        if (show) {
            emptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    private void setUpRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
         adapter=new NewsListAdapter(this);
         recyclerView.setAdapter(adapter);
    }

    private void getIntentValues() {
        type=  getIntent().getStringExtra("type");
        title=  getIntent().getStringExtra("title");
        countryID= getIntent().getStringExtra("country");
    }

    private void bindViews() {
        recyclerView=findViewById(R.id.recyclerView);
        emptyList=findViewById(R.id.emptyList);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

    }

    public  Observable<String> fromView() {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                subject.onNext(text);
                return true;
            }
        });

        return subject;
    }
}
