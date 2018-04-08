package com.administrator.seawindow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.bean.VideoBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoRecyclerAdapter extends RecyclerView.Adapter{
    private static final String TAG = "SeaHotSpotRecyclerAdapt";
    private List<VideoBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private VideoClickListener mVideoClickListener;

    public VideoRecyclerAdapter(List<VideoBean> mData, Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
    }

    public void setmVideoClickListener(VideoClickListener mVideoClickListener){
        this.mVideoClickListener = mVideoClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_item_video, parent, false);
        return new VideosViewHolder(itemView);
    }

    public List<VideoBean> getmData(){
        return mData;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VideoBean bean = mData.get(position);
        String title = bean.getTitle();
        Log.e(TAG, "onBindViewHolder: title = " + title);
        String video = bean.getVideo();
        ((VideosViewHolder)holder).videoTitle.setText(title);
        ((VideosViewHolder)holder).part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoClickListener.onVideoClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface VideoClickListener{
        void onVideoClick(int position);
    }

    class VideosViewHolder extends RecyclerView.ViewHolder{
        private ImageView videoCover;
        private TextView videoTitle, videoText;
        private RelativeLayout part;

        public VideosViewHolder(View itemView) {
            super(itemView);
            videoCover = itemView.findViewById(R.id.imageView);
            videoTitle = itemView.findViewById(R.id.title_video);
            videoText = itemView.findViewById(R.id.text_video);
            part = itemView.findViewById(R.id.part);
        }
    }
}
