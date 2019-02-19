package com.example.firstapp.widget.detailView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.example.firstapp.widget.detailView.listener.DetailScrollStateListener;
import com.example.firstapp.widget.scrollview.ObservableScrollView;

import java.util.logging.Logger;


/**
 * Created by Nowy on 2018/1/9.
 */

public class DetailScrollView extends ObservableScrollView {

    private DetailScrollStateListener mScrollStateListener;
    private int mTouchSlop;

    public DetailScrollView(Context context) {
        super(context);
        init(context);
    }

    public DetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    boolean mIsInterceptTouchEvent = false;
    float downY ;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if(action == MotionEvent.ACTION_DOWN){//按下
            downY = ev.getRawY();
        }else if(action == MotionEvent.ACTION_MOVE){//移动
            float dy = ev.getRawY() - downY;
            //1.判断上下滑动
            //2.通过mIsInterceptTouchEvent将是否中断交给外部实现
            if(Math.abs(dy) > mTouchSlop &&  mIsInterceptTouchEvent){//是否中断事件向下分发
                return true;
            }else{
                if(mScrollStateListener != null){
                    //3.页面滑向顶部时，子页面不能处理则交给父控件
                    if(dy > 0 && !mScrollStateListener.isChildTouchEvent()){//手指向下滑（即页面滑向顶部）
                        return true;
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setInterceptTouchEvent(boolean isInterceptTouchEvent) {
        this.mIsInterceptTouchEvent = isInterceptTouchEvent;
    }


    public void setDetailScrollStateListener(DetailScrollStateListener scrollStateListener) {
        this.mScrollStateListener = scrollStateListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
