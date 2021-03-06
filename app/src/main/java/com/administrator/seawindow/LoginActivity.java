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
import com.administrator.seawindow.utils.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

public class LoginActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private EditText mobile_text, password_text;
    private Button login_button, bt_login_register;
    private DialogUtil dialogUtil;
    private String nickName;
    private String phoneNum;
    private String password;
    private String email;
    private int id = -1;

    private final int LOGIN_SUCCESS = 0;
    private final int LOGIN_FAILED = 1;
    private final int LOGIN_START = 2;
    private final int NET_ERROR = 3;
    private final int SERVICE_ERROR = 4;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:
                    if (dialogUtil != null) {
                        if (dialogUtil.isDialogShowing()) {
                            dialogUtil.dismissDialog();
                        }
                    }
                    ToastUtil.show(LoginActivity.this, "登录成功");
                    PreferenceUtil.setLOGINSTATE(LoginActivity.this, id);
                    HashMap<String,String> userInfo = new HashMap<>();
                    userInfo.put("nickName",nickName);
                    userInfo.put("phoneNumber",phoneNum);
                    userInfo.put("password",password);
                    userInfo.put("email",email);
                    PreferenceUtil.setUserInfo(LoginActivity.this, userInfo);
                    OpenActivityUtil.openActivity(LoginActivity.this, MainActivity.class);
                    finish();
                    break;
                case LOGIN_FAILED:
                    if (dialogUtil != null) {
                        if (dialogUtil.isDialogShowing()) {
                            dialogUtil.dismissDialog();
                        }
                    }
                    ToastUtil.show(LoginActivity.this, "登录失败！请检查用户名和密码！");
                    break;
                case LOGIN_START:
                    if (dialogUtil == null) {
                        dialogUtil = new DialogUtil(LoginActivity.this, null, false);
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
                    ToastUtil.show(LoginActivity.this, R.string.login_net_error);
                    break;
                case SERVICE_ERROR:
                    if (dialogUtil != null) {
                        if (dialogUtil.isDialogShowing()) {
                            dialogUtil.dismissDialog();
                        }
                    }
                    ToastUtil.show(LoginActivity.this, R.string.service_error);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = PreferenceUtil.getLOGINSTATE(this);
        if (id != -1) {
            OpenActivityUtil.openActivity(this, MainActivity.class);
        } else {
            setContentView(R.layout.activity_login);
            initView();
            setListener();
        }
    }

    private void initView(){
        mobile_text = findViewById(R.id.mobile_text);
        password_text = findViewById(R.id.password_text);

        login_button = findViewById(R.id.login_button);
        bt_login_register = findViewById(R.id.bt_login_register);
    }

    private void setListener(){
        login_button.setOnClickListener(this);
        bt_login_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                String userMobile = mobile_text.getText().toString().trim();
                password = password_text.getText().toString().trim();
                if (TextUtils.isEmpty(userMobile) || TextUtils.isEmpty(password)) {
                    ToastUtil.show(getApplicationContext(), R.string.login_params_null);
                    return;
                }
                if (!isMobile(userMobile)) {
                    ToastUtil.show(getApplicationContext(), R.string.login_params_error);
                    return;
                }
                login(userMobile, password);
                break;
            case R.id.bt_login_register:
                OpenActivityUtil.openActivity(this, RegisterActivity.class);
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

    @SuppressLint("StaticFieldLeak")
    private void login(final String mobile, final String password){
        Message msg = mHandler.obtainMessage(LOGIN_START);
        mHandler.sendMessage(msg);
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Log.e(TAG, "doInBackground: url = " + ConstantPool.LOGIN +
                        "?type=1&phoneNumber=" + mobile  + "&password=" + password);
                Response response = HttpUtils.getInstance().request(ConstantPool.LOGIN +
                        "?type=1&phoneNumber=" + mobile  + "&password=" + password);
                try {
                    if (response != null) {
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:login json = " + json);
                    }
                } catch (IOException e) {
                    Message msg = mHandler.obtainMessage(NET_ERROR);
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    if (json.contains("失败")) {
                        Message msg = mHandler.obtainMessage(LOGIN_FAILED);
                        mHandler.sendMessage(msg);
                    } else {
                        try {
                            JSONObject obj = new JSONObject(json);
                            JSONObject obj1 = obj.optJSONObject("user");
                            nickName = obj1.optString("nickName");
                            phoneNum = obj1.optString("phoneNumber");
                            id = obj1.optInt("uid");
                            email = obj1.optString("email");

                            Message msg = mHandler.obtainMessage(LOGIN_SUCCESS);
                            mHandler.sendMessage(msg);
                        } catch (JSONException e) {
                            Message msg = mHandler.obtainMessage(SERVICE_ERROR);
                            mHandler.sendMessage(msg);
                            e.printStackTrace();
                        }
                    }
                } else {
                    Message msg = mHandler.obtainMessage(SERVICE_ERROR);
                    mHandler.sendMessage(msg);
                }
            }
        }.execute();
    }
}
