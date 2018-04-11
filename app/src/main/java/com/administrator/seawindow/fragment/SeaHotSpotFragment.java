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
import com.administrator.seawindow.bean.CommentBean;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.EventBusEvent;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seahotspot_layout, null);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView(view);
        initData();
        setListener();
        return view;
    }

    @Subscribe
    public void onServiceEvent(EventBusEvent eventBusEvent) {
        String key = eventBusEvent.getEvent();
        Log.e(TAG, "onServiceEvent: ");
        switch (key) {
            case EventBusEvent.RFRESH_SEA_HOTNEWS:
                Log.e(TAG, "onServiceEvent: -------------------");
                mList.clear();
                mAdapter.getmData().clear();
                getHotSpot();
                break;
        }

    }

    private void initData() {
        mList = new ArrayList<>();

        mAdapter = new SeaHotSpotRecyclerAdapter(mList, getActivity());
        gridview_news.setAdapter(mAdapter);
        mAdapter.setmNewsClickListener(new SeaHotSpotRecyclerAdapter.NewsClickListener() {
            @Override
            public void onNewsClick(int position) {
                SeaHotSpotBean bean = mList.get(position);
                Log.e(TAG, "onNewsClick:---- " + bean.getImage());
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                OpenActivityUtil.openActivity(getActivity(), bundle, SeaHotSpotInfoActivity.class);
            }
        });

        getHotSpot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View view) {
        gridview_news = view.findViewById(R.id.gridview_news);
        sr_swpierefresh = view.findViewById(R.id.sr_swpierefresh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        gridview_news.setLayoutManager(linearLayoutManager);
        gridview_news.setHasFixedSize(true);
    }

    private void setListener() {
        sr_swpierefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter == null) {
                    sr_swpierefresh.setRefreshing(false);
                    return;
                }
                sr_swpierefresh.setRefreshing(true);
                mList.clear();
                mAdapter.getmData().clear();
                getHotSpot();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getHotSpot() {
        new AsyncTask<Void, Void, Void>() {
            String json = null;

            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_HOT_SPOT);
                try {
                    if (response != null) {
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
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            JSONObject oceanNews = obj.optJSONObject("oceanHotNews");
                            int id = oceanNews.optInt("id");
                            String image = oceanNews.optString("image");
                            Log.e(TAG, "onPostExecute: image = " + image);
                            String publishTime = oceanNews.optString("publishTime");
                            String source = oceanNews.optString("source");
                            String text = oceanNews.optString("text");
                            String title = oceanNews.optString("title");
                            JSONArray comments = obj.optJSONArray("comments");
                            List<CommentBean> com = new ArrayList<>();
                            for (int j = 0; j < comments.length(); j++) {
                                JSONObject comment = comments.optJSONObject(j);
                                CommentBean commentBean = new CommentBean();
                                commentBean.setImage(comment.optString("image"));
                                commentBean.setNewsID(comment.optInt("newsId"));
                                commentBean.setNickName(comment.optString("nickName"));
                                commentBean.setText(comment.optString("text"));
                                com.add(commentBean);
                            }

                            SeaHotSpotBean bean = new SeaHotSpotBean();
                            bean.setText(text);
                            bean.setTitle(title);
                            bean.setComments(com);
                            bean.setId(id);
                            bean.setImage(image);
                            bean.setPublishTime(publishTime);
                            bean.setSource(source);
                            mList.add(bean);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onPostExecute:getHotSpot e = " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (sr_swpierefresh != null) {
                            sr_swpierefresh.setRefreshing(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.execute();
    }
}
