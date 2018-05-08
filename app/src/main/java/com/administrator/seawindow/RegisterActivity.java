package com.administrator.seawindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.administrator.seawindow.listener.MyTextWatcher;
import com.administrator.seawindow.utils.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class RegisterActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "RegisterActivity";
    private EditText et_username_register, et_userphone_register, et_password_register, et_confirm_password_register;
    private EditText et_email_register;
    private Button register_button;
//    private MyTextWatcher mTextWatcher;
    private DialogUtil dialogUtil;

    private final int REGISTER_SUCCESS = 0;
    private final int REGISTER_FAILED = 1;
    private final int REGISTER_STARTED = 2;
    private final int NET_ERROR = 3;
    private final int SERVICE_ERROR = 4;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REGISTER_SUCCESS:
                    if (dialogUtil != null) {
                        if (dialogUtil.isDialogShowing()) {
                            dialogUtil.dismissDialog();
                        }
                    }
                    OpenActivityUtil.openActivity(RegisterActivity.this, LoginActivity.class);
                    finish();
                    break;
                case REGISTER_FAILED:
                    if (dialogUtil != null) {
                        if (dialogUtil.isDialogShowing()) {
                            dialogUtil.dismissDialog();
                        }
                    }
                    ToastUtil.show(RegisterActivity.this, R.string.phone_is_registered);
                    break;
                case REGISTER_STARTED:
                    if (dialogUtil == null) {
                        dialogUtil = new DialogUtil(RegisterActivity.this, null, false);
                    }
                    if (!dialogUtil.isDialogShowing()) {
                        dialogUtil.showDialog();
                    }
                    break;
                case NET_ERROR:
                    if (dialogUtil != null) {
                        if (dialogUtil.isDialogShowing()) {
                            dialogUtil.dismissDialog();
                        }
                    }
                    ToastUtil.show(RegisterActivity.this, R.string.login_net_error);
                    break;
                case SERVICE_ERROR:
                    if (dialogUtil != null) {
                        if (dialogUtil.isDialogShowing()) {
                            dialogUtil.dismissDialog();
                        }
                    }
                    ToastUtil.show(RegisterActivity.this, R.string.service_error);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setListener();
    }

    private void initView(){
        et_username_register = findViewById(R.id.et_username_register);
        et_userphone_register = findViewById(R.id.et_userphone_register);
        et_password_register = findViewById(R.id.et_password_register);
        et_confirm_password_register = findViewById(R.id.et_confirm_password_register);
        register_button = findViewById(R.id.register_button);
        et_email_register = findViewById(R.id.et_email_register);

//        mTextWatcher = new MyTextWatcher(et_username_register, MyTextWatcher.TYPE_USERNAME);
    }

    private void setListener(){
        register_button.setOnClickListener(this);
    }

    @SuppressLint("StaticFieldLeak")
    private void register(final String userName, final String phoneNum, final String password, final String emailAddress){
        Log.e(TAG, "register: ----------------");
        Message startMsg = mHandler.obtainMessage(REGISTER_STARTED);
        mHandler.sendMessage(startMsg);
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Log.e(TAG, "register: url = " + ConstantPool.REGISTER +
                        "?type=1&phoneNumber=" + phoneNum + "&nickName=" + userName + "&password=" + password + "&email=" + emailAddress);
                Response response = HttpUtils.getInstance().request(ConstantPool.REGISTER +
                        "?type=1&phoneNumber=" + phoneNum + "&nickName=" + userName + "&password=" + password + "&email=" + emailAddress);
                try {
                    if(response != null){
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:register json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:register e = " + e);
                    Message msg = mHandler.obtainMessage(NET_ERROR);
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    Log.e(TAG, "onPostExecute: !TextUtils.isEmpty(json)");
                    try {
                        JSONObject obj = new JSONObject(json);
                        String message = obj.optString("message");
                        if (message.contains("已注册")) {
                            Log.e(TAG, "onPostExecute: REGISTER_FAILED");
                            Message msg = mHandler.obtainMessage(REGISTER_FAILED);
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        Message msg = mHandler.obtainMessage(SERVICE_ERROR);
                        mHandler.sendMessage(msg);
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onPostExecute: REGISTER_SUCCESS");
                    Message msg = mHandler.obtainMessage(REGISTER_SUCCESS);
                    mHandler.sendMessage(msg);
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                String username = et_username_register.getText().toString().trim();
                String mobile = et_userphone_register.getText().toString().trim();
                String password = et_password_register.getText().toString().trim();
                String confirmPaw = et_confirm_password_register.getText().toString().trim();
                String email = et_email_register.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPaw) || TextUtils.isEmpty(email)) {
                    ToastUtil.show(getApplicationContext(),R.string.register_params_error);
                    return;
                }
//                if (mTextWatcher.judgeText() < 0) {
//                    return;
//                }
                if (!isMobile(mobile)) {
                    ToastUtil.show(getApplicationContext(),R.string.login_params_error);
                    return;
                }
                if (!isEmail(email)) {
                    ToastUtil.show(getApplicationContext(),R.string.login_email_error);
                    return;
                }
                if (password.length() < 6 || password.length() > 20) {
                    ToastUtil.show(getApplicationContext(),R.string.rule_password);
                    return;
                }
                if (!password.equals(confirmPaw)) {
                    ToastUtil.show(getApplicationContext(),R.string.register_password_diff);
                    return;
                }

                register(username, mobile, password, email);
                break;
        }
    }

    private boolean isMobile(String number){
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    private boolean isEmail(String emailAddress){
        String regex_email = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" +
                "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if (emailAddress.matches(regex_email)) {
            return true;
        }
        return false;
    }
}
