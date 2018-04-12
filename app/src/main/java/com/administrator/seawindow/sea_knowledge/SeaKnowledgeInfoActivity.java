package com.administrator.seawindow.sea_knowledge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.seawindow.R;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.view.CommonTitleBar;
import com.squareup.picasso.Picasso;

public class SeaKnowledgeInfoActivity extends AppCompatActivity {
    private static final String TAG = "SeaKnowledgeInfoActivit";
    private TextView title;
    private TextView text;
    private ImageView imageView;
    private String titleKn, textKn, imageUrl;
    private CommonTitleBar commonTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_knowledge_info);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            titleKn = bundle.getString("title");
            textKn = bundle.getString("text");
            imageUrl = bundle.getString("image");
        }
        Log.e(TAG, "onCreate: " + title);
        commonTitleBar = findViewById(R.id.title_bar);
        commonTitleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText(titleKn);
        text = findViewById(R.id.text);
        text.setText(textKn);
        imageView = findViewById(R.id.image_hot);
        Picasso.with(this).load(imageUrl).into(imageView);
    }
}
