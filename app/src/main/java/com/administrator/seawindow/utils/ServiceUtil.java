package com.administrator.seawindow.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Administrator on 2017/5/8.
 */
public class ServiceUtil {
    public static boolean sDvRegistered = false;

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean getNetworkState(Context context) {
        ConnectivityManager manager    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         activeInfo = manager.getActiveNetworkInfo();
        boolean             netState   = false;
        if (activeInfo != null && activeInfo.isConnected() && activeInfo.isAvailable()) {
            netState = true;
        }
        else {
            netState = false;
        }

        return netState;
    }
}
