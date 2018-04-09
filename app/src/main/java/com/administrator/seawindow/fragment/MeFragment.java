package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.administrator.seawindow.LoginActivity;
import com.administrator.seawindow.R;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.utils.PreferenceUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "MeFragment";
    private String nickName;
    private String phoneNum;
    private TextView et_name_info, et_phone_info;
    private Button loginout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_layout, null);
        getData();
        initView(view);
        return view;
    }

    private void getData(){
        HashMap<String, String> userInfo = PreferenceUtil.getUserInfo(getActivity());
        nickName = userInfo.get("nickName");
        phoneNum = userInfo.get("phoneNum");
    }

    private void initView(View view){
        et_name_info = view.findViewById(R.id.et_name_info);
        et_phone_info = view.findViewById(R.id.et_phone_info);
        et_name_info.setText(nickName);
        et_phone_info.setText(phoneNum);
        loginout = view.findViewById(R.id.loginout);
        loginout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginout:
                loginOut();
                break;
        }
    }

    private void loginOut(){
        PreferenceUtil.loginOut(getActivity());
        OpenActivityUtil.openActivity(getActivity(), LoginActivity.class);
        getActivity().finish();
    }
}
