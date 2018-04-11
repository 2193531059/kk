package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.administrator.seawindow.R;
import com.administrator.seawindow.adapter.PinnedHeaderPhoneExpandableAdapter;
import com.administrator.seawindow.bean.SeaKnowledgeBean;
import com.administrator.seawindow.sea_knowledge.CoastalTourismActivity;
import com.administrator.seawindow.sea_knowledge.OceanSailingActivity;
import com.administrator.seawindow.sea_knowledge.SeaDefenceActivity;
import com.administrator.seawindow.sea_knowledge.SeaResouceActivity;
import com.administrator.seawindow.sea_knowledge.SeaSecretActivity;
import com.administrator.seawindow.sea_knowledge.SeaZoologyActivity;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.view.PinnedHeaderExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 */

public class SeaKnowledgeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SeaKnowledgeFragment";
    private SwipeRefreshLayout sr_swpierefresh;
    private PinnedHeaderExpandableListView mListView;
    private PinnedHeaderPhoneExpandableAdapter mAdapter;

    private ArrayList<String> groupList;
    private ArrayList<ArrayList<SeaKnowledgeBean>> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seaknowledge_layout, null);
        groupList = new ArrayList<>();
        groupList.add("海洋奥秘");
        groupList.add("海洋生态");
        groupList.add("海洋资源");
        groupList.add("海洋国防");
        groupList.add("远洋航海");
        groupList.add("滨海旅游");
        mList = new ArrayList<>();
        initView(view);
        getData();
        setListener();
        return view;
    }

    private void initView(View view){
        sr_swpierefresh = view.findViewById(R.id.sr_swpierefresh);
        mListView = view.findViewById(R.id.expandable_scan_phone);
        mAdapter = new PinnedHeaderPhoneExpandableAdapter(getActivity(), groupList, mList, mListView);
        mListView.setAdapter(mAdapter);
    }

    private void setListener(){
        sr_swpierefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr_swpierefresh.setRefreshing(true);
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
                Log.e(TAG, "doInBackground: url = " + ConstantPool.GET_KNOWLEDGE);
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_KNOWLEDGE);
                try {
                    if (response != null) {
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getKnowledge json = " + json);
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
                        JSONObject obj = new JSONObject(json);
                        JSONArray array = obj.getJSONArray("category");
                        for(int i = 0;i<array.length();i++){
                            JSONObject objChild = array.getJSONObject(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sr_swpierefresh.setRefreshing(false);
                    }
                });
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {

    }
}
