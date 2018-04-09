package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.seawindow.R;
import com.administrator.seawindow.adapter.SeaActivityRecyclerAdapter;
import com.administrator.seawindow.bean.ActivityBean;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
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

public class SeaActivityFragment extends Fragment {
    private static final String TAG = "SeaActivityFragment";
    private RecyclerView gridview_activity;
    private List<ActivityBean> mList;
    private SwipeRefreshLayout vpSwipeRefreshLayout;
    private SeaActivityRecyclerAdapter mAdapter;
    private final int GET_ACTIVITY_DATA = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_ACTIVITY_DATA:
                    vpSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
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
        mAdapter = new SeaActivityRecyclerAdapter(mList, getActivity());
        gridview_activity.setAdapter(mAdapter);
        mAdapter.setmNewsClickListener(new SeaActivityRecyclerAdapter.NewsClickListener() {
            @Override
            public void onNewsClick(int position) {

            }
        });
        vpSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.getmData().clear();
                mList.clear();
                getData();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getData(){
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_ACTIVITY);
                try {
                    if(response != null){
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getActivityData json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:getActivityData e = " + e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        JSONArray array = obj.getJSONArray("activity");
                        for(int i = 0;i<array.length();i++){
                            JSONObject objChild = array.getJSONObject(i);
                            ActivityBean bean = new ActivityBean();
                            bean.setActivityTitle(objChild.optString("activityTitle"));
                            bean.setId(objChild.optInt("id"));
                            bean.setText(objChild.optString("text"));
                            mList.add(bean);
                        }
                        Message msg = mHandler.obtainMessage(GET_ACTIVITY_DATA);
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
