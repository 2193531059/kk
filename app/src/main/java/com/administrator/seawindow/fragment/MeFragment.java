package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.seawindow.R;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MeFragment extends Fragment {
    private static final String TAG = "MeFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_layout, null);
        return view;
    }
}
