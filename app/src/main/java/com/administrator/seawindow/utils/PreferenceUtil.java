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
    private static String NICKNAME = "seaWindow.nickName";
    private static String PASSWORD = "seaWindow.password";
    private static String PHONENUM = "seaWindow.phoneNum";
    private static String EMAIL = "seaWindow.email";
    private static String HEAD_PHOTO = "seaWindow.headPhoto";

    public static void setLOGINSTATE(Context context, int id){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LOGINSTATE, id);
        editor.commit();
    }

    public static int getLOGINSTATE(Context context){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        return preferences.getInt(LOGINSTATE, -1);
    }

    public static void setUserInfo(Context context, HashMap<String,String> userInfo){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String nickName = userInfo.get("nickName");
        String phoneNum = userInfo.get("phoneNumber");
        String password = userInfo.get("password");
        String email = userInfo.get("email");
        editor.putString(NICKNAME,nickName);
        editor.putString(PHONENUM,phoneNum);
        editor.putString(PASSWORD,password);
        editor.putString(EMAIL,email);
        editor.commit();
    }

    public static void setUserHeadphoto(Context context, String photoAdr){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(HEAD_PHOTO,photoAdr);
        editor.commit();
    }

    public static String getUserHeadphoto(Context context){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        return preferences.getString(HEAD_PHOTO, "");
    }

    public static HashMap<String, String> getUserInfo(Context context){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        String nickName = preferences.getString(NICKNAME,"");
        String phoneNum = preferences.getString(PHONENUM, "");
        String password = preferences.getString(PASSWORD,"");
        String email = preferences.getString(EMAIL,"");
        HashMap<String,String> map = new HashMap<>();
        map.put("nickName",nickName);
        map.put("phoneNum",phoneNum);
        map.put("password",password);
        map.put("email",email);
        return map;
    }

    public static void loginOut(Context context){
        SharedPreferences preferences = context.getSharedPreferences("pre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NICKNAME,"");
        editor.putString(PHONENUM,"");
        editor.putString(PASSWORD,"");
        editor.putString(EMAIL,"");
        editor.putBoolean(LOGINSTATE, false);
        editor.commit();
    }
}
