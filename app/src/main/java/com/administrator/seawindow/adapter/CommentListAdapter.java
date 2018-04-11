package com.administrator.seawindow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.CommentBean;
import com.administrator.seawindow.view.ImageCircleView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by Hero on 2018/4/7.
 */

public class CommentListAdapter extends RecyclerView.Adapter{
    private static final String TAG = "CommentListAdapter";
    private List<CommentBean> mData;
    private Context mContext;
    private LayoutInflater inflater;

    public CommentListAdapter(List<CommentBean> mData, Context mContext) {
        inflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
    }

    public List<CommentBean> getData(){
        return mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.hot_spot_comment_item, parent, false);
        return new SeaHotCommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentBean bean = mData.get(position);
        String image = bean.getImage();
        String nickName = bean.getNickName();
        String commentText = bean.getText();
        if (!TextUtils.isEmpty(image)) {
            Random random = new Random(1000);
            int rand = random.nextInt();
            Picasso.with(mContext).load(image + "?" + rand).into(((SeaHotCommentsViewHolder)holder).icv_comment_item);
        } else {
            Picasso.with(mContext).load(R.drawable.def_head).into(((SeaHotCommentsViewHolder)holder).icv_comment_item);
        }
        ((SeaHotCommentsViewHolder)holder).tv_comment_name.setText(nickName);
        ((SeaHotCommentsViewHolder)holder).tv_comment_item.setText(commentText);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class SeaHotCommentsViewHolder extends RecyclerView.ViewHolder{
        private ImageCircleView icv_comment_item;
        private TextView tv_comment_name, tv_comment_item;

        public SeaHotCommentsViewHolder(View itemView) {
            super(itemView);
            icv_comment_item = itemView.findViewById(R.id.icv_comment_item);
            tv_comment_name = itemView.findViewById(R.id.tv_comment_name);
            tv_comment_item = itemView.findViewById(R.id.tv_comment_item);
        }
    }
}
