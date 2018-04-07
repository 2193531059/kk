package com.administrator.seawindow.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.seawindow.R;

import java.util.List;

/**
 * Created by Hero on 2018/4/7.
 */

public class CommentListAdapter extends RecyclerView.Adapter{
    private List<String> mData;
    private Context mContext;
    private LayoutInflater inflater;

    public CommentListAdapter(List<String> mData, Context mContext) {
        inflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
    }

    public List<String> getData(){
        return mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.hot_spot_comment_item, parent, false);
        return new SeaHotCommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String commentText = mData.get(position);
        ((SeaHotCommentsViewHolder)holder).tv_comment_item.setText(commentText);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class SeaHotCommentsViewHolder extends RecyclerView.ViewHolder{
        private ImageView icv_comment_item;
        private TextView tv_comment_name, tv_comment_item;

        public SeaHotCommentsViewHolder(View itemView) {
            super(itemView);
            icv_comment_item = itemView.findViewById(R.id.icv_comment_item);
            tv_comment_name = itemView.findViewById(R.id.tv_comment_name);
            tv_comment_item = itemView.findViewById(R.id.tv_comment_item);
        }
    }
}
