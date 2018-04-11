package com.administrator.seawindow.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.administrator.seawindow.MainActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

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

    public void doPostHeadPhoto(final Context context, final String url, Map<String, Object> params, final File file){
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("headImage", filename, body);
        }
        if (params != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : params.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException ex) {
                Log.e("lfq" ,"onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.e("lfq", response.message() + " , body " + str);
                    PreferenceUtil.setUserHeadphoto(context, str);
                    Intent intent = new Intent();
                    intent.setAction(ConstantPool.UPLOAD_HEAD);
                    context.sendBroadcast(intent);
                } else {
                    Log.e("lfq" ,response.message() + " error : body " + response.body().string());
                }
            }
        });
    }
}
