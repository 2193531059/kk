package com.administrator.seawindow.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/5/11.
 */
public class OpenActivityUtil {
    /**
     * 打开Activity
     * @param activity
     * @param cls
     */
    public static void openActivity(Activity activity ,Class<?> cls){
        Intent intent = new Intent(activity,cls);
        activity.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity ,Class<?> cls,int requestCode){
        Intent intent = new Intent(activity,cls);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void openActivity(Activity activity, Bundle bundle, Class<?> cls){
        Intent intent = new Intent(activity,cls);
        if(bundle != null){
            intent.putExtra("bundle",bundle);
        }
        activity.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity, Bundle bundle, Class<?> cls ,int requestCode){
        Intent intent = new Intent(activity,cls);
        intent.putExtra("bundle",bundle);
        activity.startActivityForResult(intent,requestCode);
    }
}
