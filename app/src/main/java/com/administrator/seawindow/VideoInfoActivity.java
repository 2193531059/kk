package com.administrator.seawindow;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.administrator.seawindow.bean.VideoBean;
import com.administrator.seawindow.video_player.MyCustomGSYPlayer;
import com.administrator.seawindow.video_player.MyGSYPlayer;
import com.administrator.seawindow.view.CommonTitleBar;

public class VideoInfoActivity extends Activity {

    private String path, title;
    private MyGSYPlayer myGSYPlayer;
    private MyCustomGSYPlayer gsyVideoPlayer;
    private FrameLayout play_part;
    private CommonTitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        titleBar = findViewById(R.id.ct_play_video);
        titleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoInfoActivity.this.finish();
            }
        });

        Bundle bundle = getIntent().getBundleExtra("bundle");
        VideoBean bean = (VideoBean) bundle.getSerializable("bean");
        path = bean.getVideo();
        title = bean.getTitle();

        gsyVideoPlayer = findViewById(R.id.ijkplayer);
        play_part = findViewById(R.id.play_view);
        initVideo();
    }

    private void initVideo(){
        if (path == null) {
            return;
        }
        myGSYPlayer = new MyGSYPlayer();
        myGSYPlayer.registerListener(new MyGSYPlayer.RefreshListener() {
            @Override
            public void onRefreshVideo() {

            }
        });
        initVideoAreaSize();
        myGSYPlayer.initPlayer(this, gsyVideoPlayer);
        myGSYPlayer.startPlay(title, path);
    }

    private void initVideoAreaSize() {
        play_part.post(new Runnable() {
            @Override
            public void run() {
                int width = play_part.getWidth();
                int cachedHeight = width * 3 / 4;
                ViewGroup.LayoutParams videoLayoutParams = play_part.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                play_part.setLayoutParams(videoLayoutParams);
            }
        });
    }
}
