package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Models.MainNewsModel;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.SavedNews;
import com.example.myapplication.NewsDetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.NewsDatabase;
import com.squareup.picasso.Picasso;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsListAdapter extends PagedListAdapter<News,NewsListAdapter.ViewHolder> {


    private final Context context;

    public NewsListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_view_full,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView source;
        ImageView imageView,like;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            imageView=itemView.findViewById(R.id.imageView);
            like=itemView.findViewById(R.id.like);
            source=itemView.findViewById(R.id.source);
        }


        public void setData() {
            News news=  getCurrentList().get(getAdapterPosition());

            if (news.getUrlToImage()!=null && !news.getUrlToImage().isEmpty())
                Picasso.get().load(news.getUrlToImage()).into(imageView);
            name.setText(news.getTitle());
            source.setText(news.getSource().getName());
            if (news.getLiked())
                like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_liked));
            else
                like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_like));

            like.setOnClickListener(view -> {

                Observable.fromCallable(() -> {
                    SavedNews savedNews=new SavedNews(news.getType(),news.getAuthor(),news.getContent(),news.getDescription(),news.getPublishedAt(),
                            news.getTitle(),news.getUrl(),news.getUrlToImage(),news.getSource());
                    news.setLiked(!news.getLiked());

                    if (!news.getLiked())
                        NewsDatabase.getDatabase(itemView.getContext()).newsDao().unLike(savedNews);
                    else
                        NewsDatabase.getDatabase(itemView.getContext()).newsDao().like(savedNews);

                    return false;
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((result) -> {
                            notifyItemChanged(getAdapterPosition());
                        });

            });
            itemView.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                itemView.getContext().startActivity(browserIntent);
            });
        }



    }


    private static final DiffUtil.ItemCallback<News> DIFF_CALLBACK =new DiffUtil.ItemCallback<News>() {
        @Override
        public boolean areItemsTheSame(News oldItem, News newItem) {
            return oldItem.getTitle().equalsIgnoreCase(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(News oldItem, News newItem) {
            return oldItem.equals(newItem);
        }
    };

}
