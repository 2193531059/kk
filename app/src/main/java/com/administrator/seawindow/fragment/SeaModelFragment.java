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
import com.administrator.seawindow.VideoInfoActivity;
import com.administrator.seawindow.adapter.SeaHotSpotRecyclerAdapter;
import com.administrator.seawindow.adapter.SeaModelRecyclerAdapter;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.bean.SeaModelBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 */

public class SeaModelFragment extends Fragment {
    private static final String TAG = "SeaModelFragment";
    private RecyclerView gridview_model;
    private SwipeRefreshLayout sr_swpierefresh;
    private SeaModelRecyclerAdapter mAdapter;
    private List<SeaModelBean> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seamodel_layout, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        sr_swpierefresh = view.findViewById(R.id.sr_swpierefresh);
        gridview_model = view.findViewById(R.id.gridview_model);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        gridview_model.setLayoutManager(linearLayoutManager);
        gridview_model.setHasFixedSize(true);

        sr_swpierefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter == null) {
                    sr_swpierefresh.setRefreshing(false);
                    return;
                }
                mList.clear();
                mAdapter.getmData().clear();
                getSeaModels();
            }
        });
    }

    private void initData(){
        mList = new ArrayList<>();

        mAdapter = new SeaModelRecyclerAdapter(mList, getActivity());
        gridview_model.setAdapter(mAdapter);
        mAdapter.setmNewsClickListener(new SeaModelRecyclerAdapter.NewsClickListener() {
            @Override
            public void onNewsClick(int position) {
                SeaModelBean bean = mList.get(position);
                Log.e(TAG, "onNewsClick: bean = " + bean);
                String url = bean.getVideo();
                String title = bean.getTitle();
                String text = bean.getText();
                Log.e(TAG, "onNewsClick: text = " + text);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", title);
                bundle.putString("text", text);
                OpenActivityUtil.openActivity(getActivity(), bundle, VideoInfoActivity.class);
            }
        });

        getSeaModels();
    }

    @SuppressLint("StaticFieldLeak")
    private void getSeaModels(){
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_MODEL);
                try {
                    if (response != null) {
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getSeaModels json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: e = " + e);
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
                            JSONObject obj = array.getJSONObject(i);
                            SeaModelBean bean = new SeaModelBean();
                            bean.setId(obj.optInt("id"));
                            bean.setText(obj.optString("text"));
                            bean.setTitle(obj.optString("title"));
                            bean.setVideo(obj.optString("video"));
                            mList.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sr_swpierefresh.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.execute();
    }
}
