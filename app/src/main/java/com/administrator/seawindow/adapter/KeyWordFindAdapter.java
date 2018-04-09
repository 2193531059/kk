package com.administrator.seawindow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.KeyWordBean;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */

public class KeyWordFindAdapter extends RecyclerView.Adapter{

    private List<SeaHotSpotBean> mData;
    private LayoutInflater inflater;
    private Context context;
    private MyKeyWordClickListener clickListener;

    public KeyWordFindAdapter(Context context, List<SeaHotSpotBean> mList) {
        inflater = LayoutInflater.from(context);
        mData = mList;
        this.context = context;
    }

    public void setClickListener(MyKeyWordClickListener listener){
        clickListener = listener;
    }

    public List<SeaHotSpotBean> getmData(){
        return mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_news, parent, false);
        return new KeyWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SeaHotSpotBean hotBean = mData.get(position);
        String title = hotBean.getTitle();
        String picUrl = hotBean.getImage();
        Picasso.with(context).load(picUrl).into(((KeyWordViewHolder)holder).newsCover);
        ((KeyWordViewHolder)holder).newsTitle.setText(title);
        ((KeyWordViewHolder)holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickKeyWord(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface MyKeyWordClickListener{
        void onClickKeyWord(int position);
    }

    class KeyWordViewHolder extends RecyclerView.ViewHolder{
        private ImageView newsCover;
        private TextView newsTitle;
        private View view;

        public KeyWordViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            newsCover = (ImageView) itemView.findViewById(R.id.iv_newsImage);
            newsTitle = (TextView) itemView.findViewById(R.id.tv_news_describe);
        }
    }
}
