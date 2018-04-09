package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.administrator.seawindow.R;
import com.administrator.seawindow.adapter.KeyWordFindAdapter;
import com.administrator.seawindow.bean.KeyWordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */

public class FindFragment extends Fragment{
    private static final String TAG = "FindFragment";
    private EditText autoCompleteTextView;
    private RecyclerView keyWordRecycler;
    private KeyWordFindAdapter mAdapter;
    private List<KeyWordBean> mList;
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
                List<KeyWordBean> datas = mAdapter.getmData();
                KeyWordBean bean = datas.get(position);
                //.......................
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: s = " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void requestKeyWordList(){

    }
}
