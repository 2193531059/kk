package com.administrator.seawindow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.SeaKnowledgeBean;
import com.administrator.seawindow.sea_knowledge.CoastalTourismActivity;
import com.administrator.seawindow.sea_knowledge.OceanSailingActivity;
import com.administrator.seawindow.sea_knowledge.SeaDefenceActivity;
import com.administrator.seawindow.sea_knowledge.SeaResouceActivity;
import com.administrator.seawindow.sea_knowledge.SeaSecretActivity;
import com.administrator.seawindow.sea_knowledge.SeaZoologyActivity;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class SeaKnowledgeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SeaKnowledgeFragment";
    private RelativeLayout sea_secret, sea_zoology, sea_resource, sea_defence, ocean_sailing, coastal_tourism;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seaknowledge_layout, null);
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
    }

    private void setListener(){
        sea_secret.setOnClickListener(this);
        sea_zoology.setOnClickListener(this);
        sea_resource.setOnClickListener(this);
        sea_defence.setOnClickListener(this);
        ocean_sailing.setOnClickListener(this);
        coastal_tourism.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sea_secret:
                OpenActivityUtil.openActivity(getActivity(), SeaSecretActivity.class);
                break;
            case R.id.sea_zoology:
                OpenActivityUtil.openActivity(getActivity(), SeaZoologyActivity.class);
                break;
            case R.id.sea_resource:
                OpenActivityUtil.openActivity(getActivity(), SeaResouceActivity.class);
                break;
            case R.id.sea_defence:
                OpenActivityUtil.openActivity(getActivity(), SeaDefenceActivity.class);
                break;
            case R.id.ocean_sailing:
                OpenActivityUtil.openActivity(getActivity(), OceanSailingActivity.class);
                break;
            case R.id.coastal_tourism:
                OpenActivityUtil.openActivity(getActivity(), CoastalTourismActivity.class);
                break;
        }
    }
}
