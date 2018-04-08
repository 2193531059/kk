package com.administrator.seawindow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.ActivityBean;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeaActivityRecyclerAdapter extends RecyclerView.Adapter{
    private static final String TAG = "SeaHotSpotRecyclerAdapt";
    private List<ActivityBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private NewsClickListener mNewsClickListener;

    public SeaActivityRecyclerAdapter(List<ActivityBean> mData, Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
    }

    public void setmNewsClickListener(NewsClickListener mNewsClickListener){
        this.mNewsClickListener = mNewsClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_item_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ActivityBean bean = mData.get(position);
        String title = bean.getActivityTitle();
        String text = bean.getText();
        ((NewsViewHolder)holder).activityTitle.setText(title);
        ((NewsViewHolder)holder).activityText.setText(text);
        ((NewsViewHolder)holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewsClickListener.onNewsClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface NewsClickListener{
        void onNewsClick(int position);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        private TextView activityTitle, activityText;
        private View view;

        public NewsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            activityTitle = itemView.findViewById(R.id.tv_activity_title);
            activityText = itemView.findViewById(R.id.tv_activity_text);
        }
    }
}
