package com.administrator.seawindow.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.administrator.seawindow.listener.ScrollViewListener;

public class ObservableScrollView extends ScrollView implements View.OnClickListener{
    private ScrollViewListener scrollViewListener = null;

    private ImageView goTopBtn;

    private int screenHeight=200;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (screenHeight != 0) {
            if (y > screenHeight) {
                goTopBtn.setVisibility(VISIBLE);
            } else {
                goTopBtn.setVisibility(GONE);
            }
        }

        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }


    public void setImgeViewOnClickGoToFirst(ImageView goTopBtn) {
        this.goTopBtn = goTopBtn;
        this.goTopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.smoothScrollTo(0, 0);
    }
}
