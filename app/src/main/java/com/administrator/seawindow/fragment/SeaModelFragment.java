package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.seawindow.R;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 */

public class SeaModelFragment extends Fragment {
    private static final String TAG = "SeaModelFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seamodel_layout, null);
        getUrl();
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void getUrl(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.GET_MODEL);
                try {
                    if (response != null) {
                        String json = response.body().string();
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

            }
        }.execute();
    }
}
