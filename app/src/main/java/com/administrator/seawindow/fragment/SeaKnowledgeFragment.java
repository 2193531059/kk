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

import com.administrator.seawindow.R;
import com.administrator.seawindow.adapter.PinnedHeaderPhoneExpandableAdapter;
import com.administrator.seawindow.bean.SeaKnowledgeBean;
import com.administrator.seawindow.sea_knowledge.SeaKnowledgeInfoActivity;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.view.PinnedHeaderExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

    private ArrayList<SeaKnowledgeBean> secreatList;
    private ArrayList<SeaKnowledgeBean> zoolegyList;
    private ArrayList<SeaKnowledgeBean> resourceList;
    private ArrayList<SeaKnowledgeBean> defenceList;
    private ArrayList<SeaKnowledgeBean> oceanfarList;
    private ArrayList<SeaKnowledgeBean> toursimList;
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
        setListener();
        return view;
    }

    private void initView(View view){
        secreatList = new ArrayList<>();
        zoolegyList = new ArrayList<>();
        resourceList = new ArrayList<>();
        defenceList = new ArrayList<>();
        oceanfarList = new ArrayList<>();
        toursimList = new ArrayList<>();
        sr_swpierefresh = view.findViewById(R.id.sr_swpierefresh);
        mListView = view.findViewById(R.id.expandable_scan_phone);
        mAdapter = new PinnedHeaderPhoneExpandableAdapter(getActivity(), groupList, mList, mListView);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PinnedHeaderPhoneExpandableAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int groupPosition, int childPosition) {
                ArrayList<ArrayList<SeaKnowledgeBean>> datas = mAdapter.getData();
                SeaKnowledgeBean bean = datas.get(groupPosition).get(childPosition);
                Log.e(TAG, "OnItemClick: -------" + bean.getCateName());
                Bundle bundle = new Bundle();
                bundle.putString("title", bean.getCateName());
                bundle.putString("text", bean.getText());
                bundle.putString("image", bean.getImageUrl());
                OpenActivityUtil.openActivity(getActivity(), bundle, SeaKnowledgeInfoActivity.class);
            }

            @Override
            public void onGroupExpand(int groupPosition) {
                int id = -1;
                Log.e(TAG, "onGroupExpand: " + groupPosition);
                switch (groupPosition) {
                    case 0:
                        secreatList.clear();
                        id = 1;
                        break;
                    case 1:
                        zoolegyList.clear();
                        id = 2;
                        break;
                    case 2:
                        resourceList.clear();
                        id = 3;
                        break;
                    case 3:
                        defenceList.clear();
                        id = 4;
                        break;
                    case 4:
                        oceanfarList.clear();
                        id = 5;
                        break;
                    case 5:
                        toursimList.clear();
                        id = 6;
                        break;
                }
                if (id != -1) {
                    getData(id);
                }
            }
        });
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
    private void getData(final int iid) {
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Log.e(TAG, "doInBackground: url = " + ConstantPool.GET_KNOWLEDGE);
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_KNOWLEDGE + "?id=" + iid);
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
                            String cateName = objChild.optString("cateName");
                            int id = objChild.optInt("id");
                            String image = objChild.optString("image");
                            String text = objChild.optString("text");
                            SeaKnowledgeBean bean = new SeaKnowledgeBean();
                            bean.setCateName(cateName);
                            bean.setImageUrl(image);
                            bean.setId(id);
                            bean.setText(text);
                            if (iid == 1) {
                                secreatList.add(bean);
                            } else if (iid == 2) {
                                zoolegyList.add(bean);
                            } else if (iid == 3) {
                                resourceList.add(bean);
                            } else if (iid == 4) {
                                defenceList.add(bean);
                            } else if (iid == 5) {
                                oceanfarList.add(bean);
                            } else if (iid == 6) {
                                toursimList.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (iid == 1) {
                            mAdapter.addChild(secreatList);
                        } else if (iid == 2) {
                            mAdapter.addChild(zoolegyList);
                        } else if (iid == 3) {
                            mAdapter.addChild(resourceList);
                        } else if (iid == 4) {
                            mAdapter.addChild(defenceList);
                        } else if (iid == 5) {
                            mAdapter.addChild(oceanfarList);
                        } else if (iid == 6) {
                            mAdapter.addChild(toursimList);
                        }
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
