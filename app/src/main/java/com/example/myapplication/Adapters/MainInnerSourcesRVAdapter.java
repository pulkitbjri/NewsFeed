package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Models.BaseNews;
import com.example.myapplication.Models.News;
import com.example.myapplication.Models.SavedNews;
import com.example.myapplication.Models.Sources;
import com.example.myapplication.R;
import com.example.myapplication.database.NewsDatabase;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;

public class MainInnerSourcesRVAdapter extends RecyclerView.Adapter<MainInnerSourcesRVAdapter.BaseViewHolder> {

    ArrayList<Sources> list;
    public MainInnerSourcesRVAdapter(ArrayList<Sources> list)
    {
        this.list=list;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==0)
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shimmer_source_view,parent,false);
        else
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.source_view,parent,false);

        return getViewHolder(view,viewType);
    }

    private BaseViewHolder getViewHolder(View view, int viewType){
        if (viewType==0)
            return new ShimmerViewHolder(view);
        else
            return new ViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        if (list.size()==0)
            return 0;
        else
            return 1;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);


        }


        @Override
        public void setData() {
            Sources news= (Sources) list.get(getAdapterPosition());
            name.setText(news.getName());

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
