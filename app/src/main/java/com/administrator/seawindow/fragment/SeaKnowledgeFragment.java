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
import com.administrator.seawindow.sea_knowledge.CoastalTourismActivity;
import com.administrator.seawindow.sea_knowledge.OceanSailingActivity;
import com.administrator.seawindow.sea_knowledge.SeaDefenceActivity;
import com.administrator.seawindow.sea_knowledge.SeaResouceActivity;
import com.administrator.seawindow.sea_knowledge.SeaSecretActivity;
import com.administrator.seawindow.sea_knowledge.SeaZoologyActivity;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 */

public class SeaKnowledgeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SeaKnowledgeFragment";
    private RelativeLayout sea_secret, sea_zoology, sea_resource, sea_defence, ocean_sailing, coastal_tourism;
    private HashMap<String,String> map;
    private SwipeRefreshLayout sr_swpierefresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seaknowledge_layout, null);
        map = new HashMap<>();
        getData();
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view){
        sea_secret = view.findViewById(R.id.sea_secret);
        sea_zoology = view.findViewById(R.id.sea_zoology);
        sea_resource = view.findViewById(R.id.sea_resource);
        sea_defence = view.findViewById(R.id.sea_defence);
        ocean_sailing = view.findViewById(R.id.ocean_sailing);
        coastal_tourism = view.findViewById(R.id.coastal_tourism);
        sr_swpierefresh = view.findViewById(R.id.sr_swpierefresh);
    }

    private void setListener(){
        sea_secret.setOnClickListener(this);
        sea_zoology.setOnClickListener(this);
        sea_resource.setOnClickListener(this);
        sea_defence.setOnClickListener(this);
        ocean_sailing.setOnClickListener(this);
        coastal_tourism.setOnClickListener(this);
        sr_swpierefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr_swpierefresh.setRefreshing(true);
                map.clear();
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
                            map.put(objChild.optString("cateName"), objChild.optString("text"));
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
        String secretText = null;
        String zoologyText = null;
        String resourceText = null;
        String defenceText = null;
        String sailingText = null;
        String tourismText = null;
        if (map != null) {
            secretText = map.get("海洋奥秘");
            zoologyText = map.get("海洋生态");
            resourceText = map.get("海洋资源");
            defenceText = map.get("海洋国防");
            sailingText = map.get("远洋航海");
            tourismText = map.get("滨海旅游");
        }
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.sea_secret:
                bundle.putString("text", secretText);
                OpenActivityUtil.openActivity(getActivity(),bundle, SeaSecretActivity.class);
                break;
            case R.id.sea_zoology:
                bundle.putString("text", zoologyText);
                OpenActivityUtil.openActivity(getActivity(), SeaZoologyActivity.class);
                break;
            case R.id.sea_resource:
                bundle.putString("text", resourceText);
                OpenActivityUtil.openActivity(getActivity(), SeaResouceActivity.class);
                break;
            case R.id.sea_defence:
                bundle.putString("text", defenceText);
                OpenActivityUtil.openActivity(getActivity(), SeaDefenceActivity.class);
                break;
            case R.id.ocean_sailing:
                bundle.putString("text", sailingText);
                OpenActivityUtil.openActivity(getActivity(), OceanSailingActivity.class);
                break;
            case R.id.coastal_tourism:
                bundle.putString("text", tourismText);
                OpenActivityUtil.openActivity(getActivity(), CoastalTourismActivity.class);
                break;
        }
    }
}
