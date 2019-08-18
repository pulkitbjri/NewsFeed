package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.SavedNews;
import com.example.myapplication.database.NewsDatabase;
import com.squareup.picasso.Picasso;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsDetailsActivity extends AppCompatActivity {

    ImageView imageView,like;
    TextView source;
    TextView title;
    TextView desc;
    TextView date;
    private String titleID;
    private String sourceID;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        bindviews();
        getIntentData();
        getValuesandSetData();
    }

    private void getValuesandSetData() {
        if (sourceID.equalsIgnoreCase("Saved"))

            NewsDatabase.getDatabase(this).newsDao().getSavedNewsbyTitle("%" +titleID+"%" )
                    .observe(this, savedNews -> {
                        if (savedNews==null)
                            return;
                        if (savedNews.getUrlToImage()!=null && !savedNews.getUrlToImage().isEmpty())
                            Picasso.get().load(savedNews.getUrlToImage()).into(imageView);
                        source.setText(String.format(NewsDetailsActivity.this.getString(R.string.source), savedNews.getSource().getName()));
                        title.setText(savedNews.getTitle());
                        desc.setText(savedNews.getDescription());
                        date.setText(String.format(NewsDetailsActivity.this.getString(R.string.published_at), savedNews.getPublishedAt()));
                        button.setOnClickListener(view -> openWeb(savedNews.getUrl()));
                        like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_liked));
                        like.setOnClickListener(view -> {
                            likeImage(false,null,savedNews);
                        });
                    });
        else
            NewsDatabase.getDatabase(this).newsDao().getNewsbyTitle("%" +titleID+"%" )
                    .observe(this, savedNews -> {
                        if (savedNews==null)
                            return;

                        if (savedNews.getUrlToImage()!=null && !savedNews.getUrlToImage().isEmpty())
                            Picasso.get().load(savedNews.getUrlToImage()).into(imageView);
                        source.setText(String.format(NewsDetailsActivity.this.getString(R.string.source), savedNews.getSource().getName()));
                        title.setText(savedNews.getTitle());
                        desc.setText(savedNews.getDescription());
                        date.setText(String.format(NewsDetailsActivity.this.getString(R.string.published_at), savedNews.getPublishedAt()));
                        button.setOnClickListener(view -> openWeb(savedNews.getUrl()));
                        if (savedNews.getLiked())
                            like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_liked));
                        else
                            like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_like));
                        like.setOnClickListener(view -> {
                            likeImage(savedNews.getLiked(),savedNews,null);
                        });

                    });

    }

    private void likeImage(boolean b, News news , SavedNews saved) {
        SavedNews savedNews;
        if (news==null)
        {
            savedNews=saved;
        }
        else {

            savedNews=new SavedNews(news.getType(),news.getAuthor(),news.getContent(),news.getDescription(),news.getPublishedAt(),
                news.getTitle(),news.getUrl(),news.getUrlToImage(),news.getSource());
            news.setLiked(!news.getLiked());
            if (news.getLiked())
                like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_liked));
            else
                like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_like));
        }

        Observable.fromCallable(() -> {
            if (news==null) {
                if (!news.getLiked())
                    NewsDatabase.getDatabase(this).newsDao().unLike(savedNews);
                else
                    NewsDatabase.getDatabase(this).newsDao().like(savedNews);
            }
            else
                NewsDatabase.getDatabase(this).newsDao().unLike(savedNews);
            return false;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                });
    }

    private void openWeb(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void getIntentData() {
        titleID=getIntent().getStringExtra("title");
        sourceID=getIntent().getStringExtra("source");
    }

    private void bindviews() {
        imageView=findViewById(R.id.imageView);
        like=findViewById(R.id.like);
        source=findViewById(R.id.source);
        title=findViewById(R.id.title);
        desc=findViewById(R.id.desc);
        date=findViewById(R.id.date);
        button=findViewById(R.id.button);
    }
}
