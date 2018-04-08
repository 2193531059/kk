package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.ActivityBean;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class SeaActivityFragment extends Fragment {
    private RecyclerView gridview_activity;
    private List<ActivityBean> mList;
    private VpSwipeRefreshLayout vpSwipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seaactivity_layout, null);
        initData();
        initView(view);
        return view;
    }

    private void initData(){
        mList = new ArrayList<>();
        getData();
    }

    private void initView(View view){
        vpSwipeRefreshLayout = view.findViewById(R.id.sr_swpierefresh);
        gridview_activity = view.findViewById(R.id.gridview_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        gridview_activity.setLayoutManager(linearLayoutManager);
        gridview_activity.setHasFixedSize(true);

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

    private void getData(){

    }
}
