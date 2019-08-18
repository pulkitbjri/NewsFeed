package com.example.myapplication.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Models.BaseNews;
import com.example.myapplication.Models.MainNewsModel;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.SavedNews;
import com.example.myapplication.R;
import com.example.myapplication.database.NewsDatabase;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainInnerRVAdapter extends RecyclerView.Adapter<MainInnerRVAdapter.BaseViewHolder> {

    ArrayList<BaseNews> list;
    public MainInnerRVAdapter(ArrayList<BaseNews> list)
    {
        this.list=list;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==0)
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shimmer_news_view,parent,false);
        else
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_view,parent,false);

        return getViewHolder(view,viewType);
    }

    private BaseViewHolder getViewHolder(View view, int viewType){
        if (viewType==0)
            return new ShimmerViewHolder(view);
        else if (viewType==2)
            return new SavedViewHolder(view);
        else
            return new ViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        if (list.size()==0)
            return 0;
        else
            if (list.get(position) instanceof News)
                return 1;
            else
                return 2;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return list.size()==0?3:list.size();
    }

    public class ViewHolder extends BaseViewHolder{

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


        @Override
        public void setData() {
            News news= (News) list.get(getAdapterPosition());

            Picasso.get().load(news.getUrlToImage()).into(imageView);
            name.setText(news.getTitle());
            source.setText(news.getSource().getName());
            if (news.getLiked())
                like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_liked));
            else
                like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_like));

            like.setOnClickListener(view -> {

                SavedNews savedNews=new SavedNews(news.getId(),news.getType(),news.getAuthor(),news.getContent(),news.getDescription(),news.getPublishedAt(),
                        news.getTitle(),news.getUrl(),news.getUrlToImage(),news.getSource());
                news.setLiked(!news.getLiked());

                Observable.fromCallable(() -> {
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
        }
    }
    public class SavedViewHolder extends BaseViewHolder{

        TextView name;
        TextView source;
        ImageView imageView,like;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            imageView=itemView.findViewById(R.id.imageView);
            like=itemView.findViewById(R.id.like);
            source=itemView.findViewById(R.id.source);


        }


        @Override
        public void setData() {
            SavedNews news= (SavedNews) list.get(getAdapterPosition());

            Picasso.get().load(news.getUrlToImage()).into(imageView);
            name.setText(news.getTitle());
            source.setText(news.getSource().getName());
            like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_liked));

            like.setOnClickListener(view -> {

                Observable.fromCallable(() -> {
                        NewsDatabase.getDatabase(itemView.getContext()).newsDao().unLike(news);

                    return false;
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((result) -> {
                            notifyItemChanged(getAdapterPosition());
                        });

            });
        }
    }

    public class ShimmerViewHolder extends BaseViewHolder{
        ShimmerFrameLayout shimmerFrameLayout;

        public ShimmerViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout=itemView.findViewById(R.id.shimmer);
            if (shimmerFrameLayout!=null)
                shimmerFrameLayout.startShimmer();
        }


        @Override
        public void setData() {

        }
    }
    abstract class BaseViewHolder  extends RecyclerView.ViewHolder{

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        abstract public void setData();
    }
}
