package com.administrator.seawindow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by Administrator on 2018/4/4.
 */

public class PreferenceUtil {
    private static String LOGINSTATE = "seaWindow.loginState";
    private static String NICKNAME = "seaWindow.loginState";
    private static String PASSWORD = "seaWindow.loginState";
    private static String PHONENUM = "seaWindow.loginState";

    public static void setLOGINSTATE(Context context, boolean loginState){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGINSTATE, loginState);
        editor.commit();
    }

    public static boolean getLOGINSTATE(Context context){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        return preferences.getBoolean(LOGINSTATE, false);
    }

    public static void setUserInfo(Context context, HashMap<String,String> userInfo){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String nickName = userInfo.get("nickName");
        String phoneNum = userInfo.get("phoneNumber");
        String password = userInfo.get("password");
        editor.putString(NICKNAME,nickName);
        editor.putString(PHONENUM,phoneNum);
        editor.putString(PASSWORD,password);
    }

    public HashMap<String, String> getUserInfo(Context context){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        String nickName = preferences.getString(NICKNAME,"");
        String phoneNum = preferences.getString(PHONENUM, "");
        String password = preferences.getString(PASSWORD,"");
        HashMap<String,String> map = new HashMap<>();
        map.put("nickName",nickName);
        map.put("phoneNum",phoneNum);
        map.put("password",password);
        return map;
    }
}
