package com.administrator.seawindow.utils;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import com.administrator.seawindow.R;

/**
 * Created by Administrator on 2017/6/26.
 */
public class DialogUtil {
    private Dialog progressDialog;
    private Activity mActivity;
    private TextView msg;
    private String content;
    public DialogUtil(Activity activity,String text,boolean flag) {
        mActivity = activity;
        progressDialog = new Dialog(mActivity, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.layout_dialog);
        progressDialog.setCancelable(flag);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        content = text;
        msg.setText(content);
    }

    public DialogUtil(Activity activity,int resId,boolean flag) {
        mActivity = activity;
        progressDialog = new Dialog(mActivity, R.style.progress_dialog);
        progressDialog.setOwnerActivity(mActivity);
        progressDialog.setContentView(R.layout.layout_dialog);
        progressDialog.setCancelable(flag);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        content = mActivity.getResources().getString(resId);
        msg.setText(content);
    }

    public void setOutSideCancle(boolean flag){
        if(progressDialog != null){
            progressDialog.setCanceledOnTouchOutside(flag);
        }
    }

    public void setText(String content){
        this.content = content;
        if(msg != null){
            msg.setText(content);
        }
    }
    public void setText(int resId){
        this.content = mActivity.getResources().getString(resId);
        if(msg != null){
            msg.setText(content);
        }
    }
    public void showDialog(){

            progressDialog.show();
    }

    public void dismissDialog(){
        if (isDialogShowing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public boolean isDialogShowing(){
        return progressDialog.isShowing();
    }
}
