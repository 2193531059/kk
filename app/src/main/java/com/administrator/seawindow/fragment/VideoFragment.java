package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.seawindow.R;
import com.administrator.seawindow.utils.ToastUtil;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;

/**
 * Created by Administrator on 2018/4/3.
 */

public class VideoFragment extends Fragment  {
    private VpSwipeRefreshLayout vpSwipeRefreshLayout;
    private RecyclerView gridview_videoes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout, null);
        vpSwipeRefreshLayout = view.findViewById(R.id.sr_swpierefresh);
        gridview_videoes = view.findViewById(R.id.gridview);
        return view;
    }
}
