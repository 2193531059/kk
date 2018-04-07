package com.administrator.seawindow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.administrator.seawindow.listener.Action;
import com.administrator.seawindow.utils.OpenActivityUtil;
import com.administrator.seawindow.view.CountdownView;

public class SplashActivity extends Activity {
    private CountdownView countdownView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        countdownView = findViewById(R.id.countdownView);
        countdownView.setText(getString(R.string.jump_out));
        countdownView.setTime(5000);
        countdownView.star();
        countdownView.setOnFinishAction(new Action() {
            @Override
            public void onAction() {
                OpenActivityUtil.openActivity(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.finish();
            }
        });
        countdownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenActivityUtil.openActivity(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.finish();
            }
        });

    }
}
