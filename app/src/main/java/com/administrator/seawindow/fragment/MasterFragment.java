package com.administrator.seawindow.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.administrator.seawindow.R;
import com.administrator.seawindow.bean.SeaHotSpotBean;
import com.administrator.seawindow.utils.ConstantPool;
import com.administrator.seawindow.utils.HttpUtils;
import com.administrator.seawindow.view.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MasterFragment extends Fragment implements OnBannerListener {
    private static final String TAG = "MasterFragment";
    private SeaHotSpotFragment seaHotSpotFragment;
    private SeaKnowledgeFragment seaKnowledgeFragment;
    private SeaModelFragment seaModelFragment;
    private RequestionFragment requestionFragment;
    private SeaActivityFragment seaActivityFragment;

    private ViewPager mPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private DisplayMetrics dm;

    private Banner banner;
    private ArrayList<String> titles;
    private List<String> circleUrls;
    private final int GET_CIRCLE_URL_SUCCESS = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_CIRCLE_URL_SUCCESS:
                    titles = new ArrayList<>();
                    titles.add("1");
                    titles.add("2");
                    titles.add("3");

                    banner.setImages(circleUrls)//设置图片资源
                            .setImageLoader(new ImageLoader() {
                                @Override
                                public void displayImage(Context context, Object path, ImageView imageView) {
                                    Picasso.with(context).load((String) path).into(imageView);
                                }
                            })//设置图片加载器
                            .setBannerTitles(titles)//设置文字
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)//设置样式
                            .setOnBannerListener(MasterFragment.this)//设置监听器
                            .setDelayTime(4000)
                            .start();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_layout, null);
        circleUrls = new ArrayList<>();
        getCircle();
        setOverflowShowingAlways();
        dm = getResources().getDisplayMetrics();
        mPager = view.findViewById(R.id.pager);
        banner = view.findViewById(R.id.banner);
        pagerSlidingTabStrip = view.findViewById(R.id.tabs);

        mPager.setOffscreenPageLimit(0);//设置ViewPager的缓存界面数,默认缓存为2
        mPager.setAdapter(new TabsPageAdapter(getChildFragmentManager()));
        pagerSlidingTabStrip.setViewPager(mPager);
        int screnHeight = dm.heightPixels;
        banner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screnHeight / 3));
        setTabsValue();
        setListener();
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void getCircle(){
        new AsyncTask<Void, Void, Void>() {
            String json = null;
            @Override
            protected Void doInBackground(Void... voids) {
                Response response = HttpUtils.getInstance().request(ConstantPool.MASTER_CIRCLE);
                try {
                    if(response != null){
                        json = response.body().string();
                        Log.e(TAG, "doInBackground:getCircle json = " + json);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground:getCircle e = " + e);
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONArray array = new JSONArray(json);
                        for (int i = 0;i < array.length();i++){
                            JSONObject obj = array.getJSONObject(i);
                            if (obj != null) {
                                String imageStr = ConstantPool.HOST + obj.getString("image");
                                circleUrls.add(imageStr);
                            }
                        }
                        Message msg = mHandler.obtainMessage(GET_CIRCLE_URL_SUCCESS);
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        Log.e(TAG, "onPostExecute:getCircle e = " + e.getMessage());
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        pagerSlidingTabStrip.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        pagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        pagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        pagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        pagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, dm));
        // 设置Tab Indicator的颜色
        pagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#ffffff"));//#d83737   #d83737(绿)
        // 设置选中Tab文字的颜色
        pagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#ffffff"));
        // 取消点击Tab时的背景色
        pagerSlidingTabStrip.setTabBackground(0);
    }

    private void setListener(){
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: position = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(getParentFragment().getActivity());
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }

    private class TabsPageAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TitleIconTabProvider{
        private final String[] titles = { getString(R.string.master_hotspot), getString(R.string.master_knowledge), getString(R.string.master_model), getString(R.string.master_question), getString(R.string.master_activity)};

        private final int[] titleIcon = {R.drawable.sea_hot, R.drawable.sea_knowledge, R.drawable.sea_model, R.drawable.sea_question, R.drawable.sea_activvity};

        public TabsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    seaHotSpotFragment = new SeaHotSpotFragment();
                    return seaHotSpotFragment;
                case 1:
                    seaKnowledgeFragment = new SeaKnowledgeFragment();
                    return seaKnowledgeFragment;
                case 2:
                    seaModelFragment = new SeaModelFragment();
                    return seaModelFragment;
                case 3:
                    requestionFragment = new RequestionFragment();
                    return requestionFragment;
                case 4:
                    seaActivityFragment = new SeaActivityFragment();
                    return seaActivityFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public int getPageIconResId(int position) {
            return titleIcon[position];
        }
    }
}
