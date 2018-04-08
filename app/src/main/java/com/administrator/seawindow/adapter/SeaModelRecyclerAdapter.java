package com.administrator.seawindow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.bean.SeaModelBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeaModelRecyclerAdapter extends RecyclerView.Adapter{
    private static final String TAG = "SeaHotSpotRecyclerAdapt";
    private List<SeaModelBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private NewsClickListener mNewsClickListener;

    public SeaModelRecyclerAdapter(List<SeaModelBean> mData, Context mContext) {
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
        SeaModelBean modelBean = mData.get(position);
        String title = modelBean.getTitle();
//        Picasso.with(mContext).load(picUrl).into(((NewsViewHolder)holder).newsCover);
        ((NewsViewHolder)holder).newsTitle.setText(title);
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
        private ImageView newsCover;
        private TextView newsTitle;
        private View view;

        public NewsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            newsCover = (ImageView) itemView.findViewById(R.id.iv_newsImage);
            newsTitle = (TextView) itemView.findViewById(R.id.tv_news_describe);
        }
    }
}
