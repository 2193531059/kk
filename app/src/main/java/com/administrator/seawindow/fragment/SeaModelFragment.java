package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private SeaModelRecyclerAdapter mAdapter;
    private List<SeaModelBean> mList;
    private final int GET_SEAMODEL_SUCCESS = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_SEAMODEL_SUCCESS:
                    mAdapter = new SeaModelRecyclerAdapter(mList, getActivity());
                    gridview_model.setAdapter(mAdapter);
                    mAdapter.setmNewsClickListener(new SeaModelRecyclerAdapter.NewsClickListener() {
                        @Override
                        public void onNewsClick(int position) {
                            SeaModelBean bean = mList.get(position);
                            Log.e(TAG, "onNewsClick: bean = " + bean);
                            String url = bean.getVideo();
                            String title = bean.getTitle();
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            bundle.putString("title", title);
                            OpenActivityUtil.openActivity(getActivity(), bundle, VideoInfoActivity.class);
                        }
                    });
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seamodel_layout, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        gridview_model = view.findViewById(R.id.gridview_model);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        gridview_model.setLayoutManager(linearLayoutManager);
        gridview_model.setHasFixedSize(true);
    }

    private void initData(){
        mList = new ArrayList<>();
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
                        Log.e(TAG, "doInBackground: json = " + json);
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

                }
            }
        }.execute();
    }
}
