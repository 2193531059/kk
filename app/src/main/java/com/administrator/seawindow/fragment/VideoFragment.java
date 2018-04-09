package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.utils.ToastUtil;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 */

public class VideoFragment extends Fragment  {
    private static final String TAG = "VideoFragment";
    private SwipeRefreshLayout vpSwipeRefreshLayout;
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
                String url = ConstantPool.HOST + bean.getVideo();
                Log.e(TAG, "onVideoClick: url = " + url);
                String title = bean.getTitle();
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", title);
                OpenActivityUtil.openActivity(getActivity(), bundle, VideoInfoActivity.class);
            }
        });

        vpSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                mAdapter.getmData().clear();
                getData();
            }
        });
    }

    private void initData(){
        mList = new ArrayList<>();
        getData();
    }

    @SuppressLint("StaticFieldLeak")
    private void getData(){
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_VIDEO);
                try {
                    if (response != null) {
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getData json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:getData e = " + e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONArray array = new JSONArray(json);
                        for(int i = 0;i<array.length();i++){
                            JSONObject objChild = array.getJSONObject(i);
                            VideoBean bean = new VideoBean();
                            bean.setId(objChild.optInt("id"));
                            bean.setTitle(objChild.optString("title"));
                            bean.setVideo(objChild.optString("video"));
                            mList.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        vpSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.execute();
    }
}
