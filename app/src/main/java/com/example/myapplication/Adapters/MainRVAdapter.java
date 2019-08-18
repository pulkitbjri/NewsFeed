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

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.ViewHolder> {

    ArrayList<MainNewsModel> list;
    MainActivity mainActivity;
    public MainRVAdapter(ArrayList<MainNewsModel> list, MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;
        this.list=list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_news_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        RecyclerView rv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            rv=itemView.findViewById(R.id.rv);
        }


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
            else if (!mainNewsModel.getType().equalsIgnoreCase("sources"))
            {
                NewsDatabase.getDatabase(mainActivity).newsDao().reposByType(mainNewsModel.getType()).
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
}
