package com.administrator.seawindow;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.administrator.seawindow.video_player.MyCustomGSYPlayer;
import com.administrator.seawindow.video_player.MyGSYPlayer;
import com.administrator.seawindow.view.CommonTitleBar;

public class VideoInfoActivity extends AppCompatActivity {
    private static final String TAG = "VideoInfoActivity";
    private String path, title, text;
    private MyGSYPlayer myGSYPlayer;
    private MyCustomGSYPlayer gsyVideoPlayer;
    private FrameLayout play_part;
    private CommonTitleBar titleBar;
    private TextView text_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        titleBar = findViewById(R.id.ct_play_video);
        text_tv = findViewById(R.id.text);
        titleBar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myGSYPlayer != null) {
                    myGSYPlayer.close();
                }
                VideoInfoActivity.this.finish();
            }
        });

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            path = bundle.getString("url");
            title = bundle.getString("title");
            text = bundle.getString("text");
        }
        Log.e(TAG, "onCreate: text = " + text);
        if (!TextUtils.isEmpty(text)) {
            text_tv.setText(text);
        }

        gsyVideoPlayer = findViewById(R.id.ijkplayer);
        play_part = findViewById(R.id.play_view);
        initVideo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myGSYPlayer != null) {
            myGSYPlayer.close();
        }
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

    private void setVideoAreaSize() {
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

    private void setVideoAreaSizeLandScap() {
        play_part.post(new Runnable() {
            @Override
            public void run() {

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();

                ViewGroup.LayoutParams videoLayoutParams = play_part.getLayoutParams();
                videoLayoutParams.width = height;
                videoLayoutParams.height = width;
                play_part.setLayoutParams(videoLayoutParams);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setVideoAreaSize();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVideoAreaSizeLandScap();
        }
        myGSYPlayer.onConfigurationChanged(newConfig);

        super.onConfigurationChanged(newConfig);
    }
}
