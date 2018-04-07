package com.administrator.seawindow;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.administrator.seawindow.fragment.MasterFragment;
import com.administrator.seawindow.fragment.MeFragment;
import com.administrator.seawindow.fragment.VideoFragment;
import com.administrator.seawindow.view.NoScrollViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TabLayout tabLayout;
    private NoScrollViewPager mPager;
    private TabLayout.Tab mTabVideo, mTabMaster, mTabMe;
    private TabsPageAdapter mAdapter;
    private ArrayList<Fragment> mFragments;
    private MasterFragment masterFragment;
    private MeFragment meFragment;
    private VideoFragment videoFragment;
    private TextView tv_title_video;
    private TextView tablayout_master;
    private TextView tv_title_me;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        setListener();
    }

    private void initFragment(){
        mFragments = new ArrayList<>();
        masterFragment = new MasterFragment();
        meFragment = new MeFragment();
        videoFragment = new VideoFragment();
        mFragments.add(masterFragment);
        mFragments.add(videoFragment);
        mFragments.add(meFragment);
    }

    private void initView(){
        tabLayout = findViewById(R.id.tab_findfragment_title);
        tv_title_video = findViewById(R.id.tv_title_video);
        tablayout_master = findViewById(R.id.tablayout_master);
        tv_title_me = findViewById(R.id.tv_title_me);
        mPager = findViewById(R.id.pager);
        toolbar = findViewById(R.id.toolbar);
        mAdapter = new TabsPageAdapter(this.getSupportFragmentManager());
        mPager.setNoScroll(true);
        mPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);
        mTabMaster = tabLayout.getTabAt(0).setText(getString(R.string.app_master)).setIcon(R.drawable.nav_menu_2_pre);
        mTabVideo = tabLayout.getTabAt(1).setText(getString(R.string.app_video)).setIcon(R.drawable.nav_menu_3);
        mTabMe = tabLayout.getTabAt(2).setText(getString(R.string.app_me)).setIcon(R.drawable.nav_menu_1);
        toolbar.setVisibility(View.GONE);
        mPager.setCurrentItem(0);
    }

    private void setListener(){
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mPager.setCurrentItem(0);
                        mTabMaster = tabLayout.getTabAt(0).setText(getString(R.string.app_master)).setIcon(R.drawable.nav_menu_2_pre);
                        mTabVideo = tabLayout.getTabAt(1).setText(getString(R.string.app_video)).setIcon(R.drawable.nav_menu_3);
                        mTabMe = tabLayout.getTabAt(2).setText(getString(R.string.app_me)).setIcon(R.drawable.nav_menu_1);
                        toolbar.setVisibility(View.GONE);
                        tv_title_video.setVisibility(View.GONE);
                        tablayout_master.setVisibility(View.VISIBLE);
                        tv_title_me.setVisibility(View.GONE);
                        break;
                    case 1:
                        mPager.setCurrentItem(1);
                        mTabMaster = tabLayout.getTabAt(0).setText(getString(R.string.app_master)).setIcon(R.drawable.nav_menu_2);
                        mTabVideo = tabLayout.getTabAt(1).setText(getString(R.string.app_video)).setIcon(R.drawable.nav_menu_3_pre);
                        mTabMe = tabLayout.getTabAt(2).setText(getString(R.string.app_me)).setIcon(R.drawable.nav_menu_1);
                        toolbar.setVisibility(View.VISIBLE);
                        tablayout_master.setVisibility(View.GONE);
                        tv_title_me.setVisibility(View.GONE);
                        tv_title_video.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mPager.setCurrentItem(2);
                        mTabMaster = tabLayout.getTabAt(0).setText(getString(R.string.app_master)).setIcon(R.drawable.nav_menu_2);
                        mTabVideo = tabLayout.getTabAt(1).setText(getString(R.string.app_video)).setIcon(R.drawable.nav_menu_3);
                        mTabMe = tabLayout.getTabAt(2).setText(getString(R.string.app_me)).setIcon(R.drawable.nav_menu_1_pre);
                        toolbar.setVisibility(View.VISIBLE);
                        tv_title_video.setVisibility(View.GONE);
                        tablayout_master.setVisibility(View.GONE);
                        tv_title_me.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class TabsPageAdapter extends FragmentPagerAdapter {
        public TabsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
