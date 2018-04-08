package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.administrator.seawindow.SeaHotSpotInfoActivity;
import com.administrator.seawindow.adapter.SeaHotSpotRecyclerAdapter;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;
import com.google.gson.Gson;

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

public class SeaHotSpotFragment extends Fragment {
    private static final String TAG = "SeaHotSpotFragment";
    private RecyclerView gridview_news;
    private SeaHotSpotRecyclerAdapter mAdapter;
    private List<SeaHotSpotBean> mList;
    private SwipeRefreshLayout sr_swpierefresh;
    private final int GET_HOT_SPOT_SUCCESS = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_HOT_SPOT_SUCCESS:
                    mAdapter = new SeaHotSpotRecyclerAdapter(mList, getActivity());
                    gridview_news.setAdapter(mAdapter);
                    mAdapter.setmNewsClickListener(new SeaHotSpotRecyclerAdapter.NewsClickListener() {
                        @Override
                        public void onNewsClick(int position) {
                            SeaHotSpotBean bean = mList.get(position);
                            Log.e(TAG, "onNewsClick: bean = " + bean);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean", bean);
                            OpenActivityUtil.openActivity(getActivity(), bundle, SeaHotSpotInfoActivity.class);
                        }
                    });
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seahotspot_layout, null);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initData(){
        mList = new ArrayList<>();
        getHotSpot();
    }

    private void initView(View view){
        gridview_news = view.findViewById(R.id.gridview_news);
        sr_swpierefresh = view.findViewById(R.id.sr_swpierefresh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        gridview_news.setLayoutManager(linearLayoutManager);
        gridview_news.setHasFixedSize(true);
    }

    private void setListener(){
        sr_swpierefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr_swpierefresh.setRefreshing(true);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getHotSpot(){
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_HOT_SPOT);
                try {
                    if(response != null){
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getHotSpot json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:getHotSpot e = " + e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONArray array = new JSONArray(json);
                        for(int i = 0; i < array.length(); i++){
                            JSONObject obj = array.getJSONObject(i);
                            if (obj != null) {
                                int id = obj.optInt("id");
                                String image = obj.optString("image");
                                String publishTime = obj.optString("publishTime");
                                String source = obj.optString("source");
                                String text = obj.optString("text");
                                String title = obj.optString("title");
                                String comments = obj.optString("comments");
                                String[] splitComment = comments.split("-");

                                SeaHotSpotBean bean = new SeaHotSpotBean();
                                bean.setText(text);
                                bean.setTitle(title);
                                bean.setComments(splitComment);
                                bean.setId(id);
                                bean.setImage(image);
                                bean.setPublishTime(publishTime);
                                bean.setSource(source);
                                mList.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onPostExecute:getHotSpot e = " + e.getMessage());
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage(GET_HOT_SPOT_SUCCESS);
                    mHandler.sendMessage(msg);
                }
            }
        }.execute();
    }
}
