package com.administrator.seawindow.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/4.
 */

public class HttpUtils {
    private static final String TAG = "HttpUtils";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static HttpUtils instance;
    private OkHttpClient mHttpClient;

    public static HttpUtils getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
        return instance;
    }

    private HttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        mHttpClient = builder.build();
    }

    public Response request(String url, Object parameters) {
        String      json        = new Gson().toJson(parameters);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = mHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            } else {
                return response;
            }
        } catch (Exception err) {
            return null;
        }
    }

    public Response request(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = mHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            } else {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response request(String url, Object parameters, HashMap<String, String> headers) {
        String      json        = new Gson().toJson(parameters);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json");

        if (null != headers) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        Request request = builder.build();

        try {
            Response response = mHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            } else {
                return response;
            }
        } catch (Exception err) {
            return null;
        }
    }

    public void doPostHeadPhoto(final String url, JSONObject jsonObject, final File file){

        //建立body，然后设置这个body里面放的数据类型是什么。
        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        //建立请求
        Request request = new Request.Builder().post(body).url(url).build();
        //定义Call
        Call call1 = mHttpClient.newCall(request);
        //执行Call
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (file != null) {
                    RequestBody body;
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("headPicture", "headPicture.jpg", RequestBody.create(MediaType.parse("image/jpg"), file));
                    body = builder.build();
                    final Request request = new Request.Builder().post(body).url(url).build();
                    Call call2 = mHttpClient.newCall(request);
                    call2.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "onFailure: e = " + e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.e(TAG, "onResponse: response = " + response.body().string());
                        }
                    });
                }
            }
        });




    }
}
