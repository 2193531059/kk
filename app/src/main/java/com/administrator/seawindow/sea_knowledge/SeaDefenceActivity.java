package com.administrator.seawindow.sea_knowledge;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.administrator.seawindow.R;
import com.administrator.seawindow.view.CommonTitleBar;

public class SeaDefenceActivity extends Activity {
    private CommonTitleBar commonTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_defence);
        commonTitleBar = findViewById(R.id.ct);
        commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
