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

import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.utils.PreferenceUtil;
import com.administrator.seawindow.utils.ServiceUtil;
import com.administrator.seawindow.utils.ToastUtil;
import com.administrator.seawindow.view.EditTextWithDel;

import java.io.IOException;

import okhttp3.Response;

public class LoginActivity extends Activity implements View.OnClickListener, EditTextWithDel.MyClickListener{
    private static final String TAG = "LoginActivity";
    private EditTextWithDel mobile_text, password_text;
    private Button login_button, bt_login_register, bt_forget_password;

    private final int LOGIN_SUCCESS = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogin = PreferenceUtil.getLOGINSTATE(this);
        if (isLogin) {
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
        bt_forget_password = findViewById(R.id.bt_forget_password);
    }

    private void setListener(){
        login_button.setOnClickListener(this);
        bt_login_register.setOnClickListener(this);
        bt_forget_password.setOnClickListener(this);
        mobile_text.registerClickListener(this);
        password_text.registerClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                String userMobile = mobile_text.getText().toString().trim();
                String password = password_text.getText().toString().trim();
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

    @Override
    public void isClick() {

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
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.LOGIN +
                        "?type=1&phoneNumber=" + mobile  + "&password=" + password);
                try {
                    if (response != null) {
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:login json = " + json);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                PreferenceUtil.setLOGINSTATE(LoginActivity.this, true);

                Message msg = mHandler.obtainMessage(LOGIN_SUCCESS);
                mHandler.sendMessage(msg);
            }
        }.execute();
    }
}
