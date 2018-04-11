package com.administrator.seawindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.seawindow.adapter.CommentListAdapter;
import com.administrator.seawindow.bean.CommentBean;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.listener.MyTextWatcher;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.EventBusEvent;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.utils.PreferenceUtil;
import com.administrator.seawindow.view.CommonTitleBar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static com.administrator.seawindow.utils.EventBusEvent.RFRESH_SEA_HOTNEWS;

public class SeaHotSpotInfoActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "SeaHotSpotInfoActivity";
    private CommonTitleBar title_bar;
    private TextView tv_title, tv_source, tv_time, tv_text;
    private RecyclerView commentRecycler;
    private String title, source, time, text;
    private int id;
    private RelativeLayout comment;
    private ImageView icon_comment;
    private ImageView image_hot;
    private TextView comment_count;
    private EditText comment_input;
    private RelativeLayout rl_comment_input;
    private Button comment_send;
    private MyTextWatcher myTextWatcher;
    private CommentListAdapter mAdapter;
    private List<CommentBean> mList;
    private String imageNews;
    private int commenCount;
    private boolean isInputShow;
    private boolean isCommentListShow = false;

    private final int SEND_COMMENT_SUCCESS = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_COMMENT_SUCCESS:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rl_comment_input.getWindowToken(), 0);
                    rl_comment_input.setVisibility(View.GONE);
                    comment_input.setText("");

                    CommentBean bean = new CommentBean();
                    String comments = (String) msg.obj;
                    bean.setText(comments);
                    String nickName = PreferenceUtil.getUserInfo(SeaHotSpotInfoActivity.this).get("nickName");
                    String headPhotoAdr = PreferenceUtil.getUserHeadphoto(SeaHotSpotInfoActivity.this);
                    bean.setNickName(nickName);
                    bean.setImage(headPhotoAdr);

                    commenCount++;
                    comment_count.setText(commenCount + "");
                    EventBus.getDefault().post(new EventBusEvent(RFRESH_SEA_HOTNEWS));
                    List<CommentBean> datas = mAdapter.getData();
                    datas.add(bean);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sea_hot_spot);
        initView();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        SeaHotSpotBean bean = (SeaHotSpotBean) bundle.getSerializable("bean");
        title = bean.getTitle();
        source = bean.getSource();
        time = bean.getPublishTime();
        text = bean.getText();
        id = bean.getId();
        imageNews = bean.getImage();

        if (!TextUtils.isEmpty(imageNews)) {
            Picasso.with(this).load(imageNews).into(image_hot);
        }

        List<CommentBean> commentBeans = bean.getComments();
        commenCount = commentBeans.size();
        comment_count.setText(commenCount + "");
        for(int i = 0;i<commentBeans.size();i++){
            mList.add(commentBeans.get(i));
        }
        setData();
        setListener();
    }

    private void initView(){
        mList = new ArrayList<>();
        title_bar = findViewById(R.id.title_bar);
        image_hot = findViewById(R.id.image_hot);
        tv_title = findViewById(R.id.title);
        tv_source = findViewById(R.id.source);
        tv_time = findViewById(R.id.time);
        tv_text = findViewById(R.id.text);
        icon_comment = findViewById(R.id.icon_comment);
        comment_count = findViewById(R.id.comment_count);
        commentRecycler = findViewById(R.id.commentList);
        comment = findViewById(R.id.comment);
        comment_input = findViewById(R.id.comment_content);
        rl_comment_input = findViewById(R.id.rl_comment_input);
        comment_send = findViewById(R.id.comment_send);
        myTextWatcher = new MyTextWatcher(comment_input, 0);
        mAdapter = new CommentListAdapter(mList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        commentRecycler.setLayoutManager(linearLayoutManager);
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setAdapter(mAdapter);
    }

    private void setData(){
        tv_title.setText(title);
        tv_source.setText(source);
        tv_time.setText(time);
        tv_text.setText(text);
    }

    private void setListener(){
        title_bar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeaHotSpotInfoActivity.this.finish();
            }
        });
        comment.setOnClickListener(this);
        comment_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment:
                if (isCommentListShow) {
                    commentRecycler.setVisibility(View.GONE);
                    isCommentListShow = false;
                } else {
                    commentRecycler.setVisibility(View.VISIBLE);
                    isCommentListShow = true;
                }
                rl_comment_input.setVisibility(View.VISIBLE);
                break;
            case R.id.comment_send:
                rl_comment_input.setVisibility(View.GONE);
                if (myTextWatcher.judgeText() >= 0) {
                    String comments = comment_input.getText().toString();
                    sendComment(comments);
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void sendComment(final String comments){
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                String nickName = PreferenceUtil.getUserInfo(SeaHotSpotInfoActivity.this).get("nickName");
                String headPhoto = PreferenceUtil.getUserHeadphoto(SeaHotSpotInfoActivity.this);
                String url = ConstantPool.COMMENT_SEND + "?id=" + id + "&comments=" + comments + "&userImage=" + headPhoto + "&nickName=" + nickName;
                Response response = HttpUtils.getInstance().request(url);
                try {
                    json = response.body().string();
                    Log.e(TAG, "doInBackground:sendComment json = " + json);
                    Message msg = mHandler.obtainMessage(SEND_COMMENT_SUCCESS);
                    msg.obj = comments;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:sendComment e = " + e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }
}
