package com.administrator.seawindow.listener;

import com.administrator.seawindow.view.ObservableScrollView;

public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
