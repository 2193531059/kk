package com.administrator.seawindow.sea_knowledge;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.view.CommonTitleBar;

public class SeaZoologyActivity extends Activity {
    private CommonTitleBar commonTitleBar;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_zoology);
        commonTitleBar = findViewById(R.id.ct);
        commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textView = findViewById(R.id.text);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle == null) {
            return;
        }
        String text = bundle.getString("text", "");

        textView.setText(text);
    }
}
