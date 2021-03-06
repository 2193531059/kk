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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.administrator.seawindow.R;
import com.administrator.seawindow.SeaHotSpotInfoActivity;
import com.administrator.seawindow.adapter.KeyWordFindAdapter;
import com.administrator.seawindow.bean.ActivityBean;
import com.administrator.seawindow.bean.CommentBean;
import com.administrator.seawindow.bean.KeyWordBean;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/9.
 */

public class FindFragment extends Fragment{
    private static final String TAG = "FindFragment";
    private EditText autoCompleteTextView;
    private RecyclerView keyWordRecycler;
    private KeyWordFindAdapter mAdapter;
    private List<SeaHotSpotBean> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_layout, container,false);
        autoCompleteTextView = view.findViewById(R.id.search);
        keyWordRecycler = view.findViewById(R.id.keyWordRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        keyWordRecycler.setLayoutManager(linearLayoutManager);
        keyWordRecycler.setHasFixedSize(true);
        mList = new ArrayList<>();
        mAdapter = new KeyWordFindAdapter(getActivity(), mList);
        keyWordRecycler.setAdapter(mAdapter);
        mAdapter.setClickListener(new KeyWordFindAdapter.MyKeyWordClickListener() {
            @Override
            public void onClickKeyWord(int position) {
                List<SeaHotSpotBean> datas = mAdapter.getmData();
                SeaHotSpotBean bean = datas.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                OpenActivityUtil.openActivity(getActivity(), bundle, SeaHotSpotInfoActivity.class);
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: s = " + s);
                if (!TextUtils.isEmpty(s)) {
                    mList.clear();
                    mAdapter.getmData().clear();
                    requestKeyWordList(s.toString());
                } else {
                    mList.clear();
                    mAdapter.getmData().clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void requestKeyWordList(final String s){
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Log.e(TAG, "doInBackground:requestKeyWordList url " + ConstantPool.SEARCH + s);
                Response response = HttpUtils.getInstance().request(ConstantPool.SEARCH + s);
                try {
                    if(response != null){
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:requestKeyWordList json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:requestKeyWordList e = " + e);
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
                            JSONObject oceanNews = obj.optJSONObject("oceanHotNews");
                            int id = oceanNews.optInt("id");
                            String image = oceanNews.optString("image");
                            String publishTime = oceanNews.optString("publishTime");
                            String source = oceanNews.optString("source");
                            String text = oceanNews.optString("text");
                            String title = oceanNews.optString("title");
                            JSONArray comments = obj.optJSONArray("comments");
                            List<CommentBean> com = new ArrayList<>();
                            for(int j = 0;j<comments.length();j++){
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
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
}
