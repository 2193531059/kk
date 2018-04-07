package com.administrator.seawindow;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.administrator.seawindow.video_player.MyCustomGSYPlayer;
import com.administrator.seawindow.video_player.MyGSYPlayer;

public class VideoInfoActivity extends Activity {

    private String path = "http://192.168.1.101:8080/Ocean_admin/modelVideo/1522992150308.mp4";
    private String pathBai = "http://baishi.baidu.com/watch/5431322122707399614.html?frm=FuzzySearch&page=videoMultiNeed";
    private MyGSYPlayer myGSYPlayer;
    private MyCustomGSYPlayer gsyVideoPlayer;
    private FrameLayout play_part;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);
        gsyVideoPlayer = findViewById(R.id.ijkplayer);
        play_part = findViewById(R.id.play_view);
        initVideo();
    }

    private void initVideo(){
        myGSYPlayer = new MyGSYPlayer();
        myGSYPlayer.registerListener(new MyGSYPlayer.RefreshListener() {
            @Override
            public void onRefreshVideo() {

            }
        });
        initVideoAreaSize();
        myGSYPlayer.initPlayer(this, gsyVideoPlayer);
        myGSYPlayer.startPlay("测试视频", pathBai);
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
