package com.example.myapplication.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.MainNewsModel;
import com.example.myapplication.Models.News;
import com.example.myapplication.R;
import com.example.myapplication.Viewmodels.MainViewModel;
import com.example.myapplication.database.NewsDatabase;

import java.util.ArrayList;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.BaseViewHolder> {

    ArrayList<MainNewsModel> list;
    MainActivity mainActivity;
    public MainRVAdapter(ArrayList<MainNewsModel> list, MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;
        this.list=list;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_news_view,parent,false);
        if (viewType==1)
            return new SourcesViewHolder(view);
        else
            return new ViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType().equalsIgnoreCase("sources") ||
                list.get(position).getType().equalsIgnoreCase("local_sources") )
        {
            return 1;
        }
        else
            return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends BaseViewHolder{

        TextView name;
        RecyclerView rv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            rv=itemView.findViewById(R.id.rv);
        }


        @Override
        public void setData() {
            MainNewsModel mainNewsModel=list.get(getAdapterPosition());
            name.setText(mainNewsModel.getTitle());
            MainInnerRVAdapter adapter=new MainInnerRVAdapter(mainNewsModel.getNewsList().getValue());
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext(),RecyclerView.HORIZONTAL,false));
            rv.setAdapter(adapter);
            if (mainNewsModel.getType().equalsIgnoreCase("saved")){
                NewsDatabase.getDatabase(mainActivity).newsDao().getSavedNews().
                        observe(mainActivity, news -> {
                            if (news!=null && news.size()!=0)
                            {
                                itemView.setVisibility(View.VISIBLE);
                                mainNewsModel.getNewsList().getValue().clear();
                                mainNewsModel.getNewsList().getValue().addAll(news);
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                itemView.setVisibility(View.GONE);
                            }
                        });

            }
            else
            {
                NewsDatabase.getDatabase(mainActivity).newsDao().newsByType(mainNewsModel.getType()).
                        observe(mainActivity, news -> {
                            if (news!=null)
                            {
                                mainNewsModel.getNewsList().getValue().addAll(news);
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        }


    }
    public class SourcesViewHolder extends BaseViewHolder{

        TextView name;
        RecyclerView rv;

        public SourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            rv=itemView.findViewById(R.id.rv);
        }


        @Override
        public void setData() {
            MainNewsModel mainNewsModel=list.get(getAdapterPosition());
            name.setText(mainNewsModel.getTitle());
            MainInnerSourcesRVAdapter adapter=new MainInnerSourcesRVAdapter(mainNewsModel.getSourceList().getValue());
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext(),RecyclerView.HORIZONTAL,false));
            rv.setAdapter(adapter);
            NewsDatabase.getDatabase(mainActivity).newsDao().sourcesByType(mainNewsModel.getType()).
                    observe(mainActivity, news -> {
                        if (news!=null)
                        {
                            mainNewsModel.getSourceList().getValue().addAll(news);
                            adapter.notifyDataSetChanged();
                        }
                    });
        }


    }

    abstract class BaseViewHolder  extends RecyclerView.ViewHolder{

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        abstract public void setData();
    }
}
