package com.example.firstapp.widget.detailView.listener;

/**
 * Created by Nowy on 2018/3/6.
 */

public interface DetailScrollStateListener {
    void updateTouchEvent(boolean isInterceptTouchEvent);
    boolean isChildTouchEvent();
}
