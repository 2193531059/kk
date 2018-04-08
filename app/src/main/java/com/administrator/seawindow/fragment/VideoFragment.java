package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.seawindow.R;
import com.administrator.seawindow.VideoInfoActivity;
import com.administrator.seawindow.adapter.VideoRecyclerAdapter;
import com.administrator.seawindow.bean.VideoBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.utils.ToastUtil;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class VideoFragment extends Fragment  {
    private static final String TAG = "VideoFragment";
    private VpSwipeRefreshLayout vpSwipeRefreshLayout;
    private RecyclerView gridview_videoes;
    private VideoRecyclerAdapter mAdapter;
    private List<VideoBean> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout, null);
        initData();
        initView(view);
        return view;
    }

    private void initView(View view){
        Log.e(TAG, "initView: -------------------");
        vpSwipeRefreshLayout = view.findViewById(R.id.sr_swpierefresh);
        gridview_videoes = view.findViewById(R.id.gridview);
        mAdapter = new VideoRecyclerAdapter(mList, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        gridview_videoes.setLayoutManager(linearLayoutManager);
        gridview_videoes.setHasFixedSize(true);
        gridview_videoes.setAdapter(mAdapter);

        mAdapter.setmVideoClickListener(new VideoRecyclerAdapter.VideoClickListener() {
            @Override
            public void onVideoClick(int position) {
                List<VideoBean> data = mAdapter.getmData();
                VideoBean bean = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                OpenActivityUtil.openActivity(getActivity(), bundle, VideoInfoActivity.class);
            }
        });

        vpSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vpSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
    }

    private void initData(){
        mList = new ArrayList<>();
        VideoBean bean = new VideoBean();
        bean.setId(1);
        bean.setTitle("视频：南海争端 作为大国国民我们究竟应该怎么看南海问题");
        bean.setVideo(ConstantPool.HOST + "oceanVideo/1523072151801.mp4");
        mList.add(bean);
    }
}
