package com.example.firstapp.widget.detailView.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.firstapp.widget.detailView.listener.DetailScrollStateListener;

/**
 * Created by Nowy on 2018/3/6.
 */

public class BaseDetailFrag extends Fragment {

    protected DetailScrollStateListener mScrollStateListener;
    protected boolean canTouch = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof  DetailScrollStateListener){
            mScrollStateListener = (DetailScrollStateListener) getActivity();
        }
    }


    protected void updateCanTouch(boolean canTouch){
        this.canTouch = canTouch;
    }
    public boolean canChildTouch() {
        return canTouch;
    }
    protected RecyclerView.OnScrollListener getOnScrollListener(){
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(-1)){//在顶部，且当前View已经允许可滑动
                    if(dy < 0){//向上滑，应该直接滑动父容器
                        updateCanTouch(false);
                    }else{//向下滑，直接滑动
                        updateCanTouch(true);
                    }
                }else{
                    updateCanTouch(true);
                }
            }
        };
    }

    protected NestedScrollView.OnScrollChangeListener getNestedScrollListener(){
        return new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!nestedScrollView.canScrollVertically(-1)){//在顶部，且当前View已经允许可滑动
                    if(scrollY < 0){//向上滑，应该直接滑动父容器
                        updateCanTouch(false);
                    }else{//向下滑，直接滑动
                        updateCanTouch(true);
                    }
                }else{
                    updateCanTouch(true);
                }
            }
        };
    }



}
