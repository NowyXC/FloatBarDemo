package com.example.firstapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.firstapp.fragment.ListFragment;
import com.example.firstapp.fragment.ScrollerViewFragment;
import com.example.firstapp.fragment.TextViewFragment;
import com.example.firstapp.widget.detailView.DetailScrollView;
import com.example.firstapp.widget.detailView.MatchFrameLayout;
import com.example.firstapp.widget.detailView.MatchViewPager;
import com.example.firstapp.widget.detailView.fragment.BaseDetailFrag;
import com.example.firstapp.widget.detailView.listener.DetailScrollStateListener;
import com.example.firstapp.widget.scrollview.ObservableScrollView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MetTuanActivity extends AppCompatActivity implements OnRefreshListener, ObservableScrollView.ScrollViewListener, DetailScrollStateListener {

    private DetailScrollView mSvFirst;
    private View mHeaderView;
    private TabLayout mTabLayout;
    private MatchFrameLayout mFlMain;
    private List<Fragment> mFragments;
    private RecyclerView mRecyclerView;

    private boolean canTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meituan);

        initView();
    }

    private void initView(){
        mSvFirst = findViewById(R.id.main_SvFirst);
        mHeaderView = findViewById(R.id.tab_viewPager_HeaderView);
        mTabLayout = findViewById(R.id.main_TabLayout);
        mFlMain = findViewById(R.id.main_FlMain);
        mRecyclerView = findViewById(R.id.list_RecyclerView);
        initScrollView();
        initTabLayout();
        initRecyclerView();
    }


    private void initScrollView(){
        mSvFirst.setInterceptTouchEvent(true);
        mSvFirst.setDetailScrollStateListener(this);
        mSvFirst.setScrollViewListener(this);
    }

    private void initTabLayout(){
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 3"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 4"));

    }

    private void initRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        List<String> data = new ArrayList<>();
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        mRecyclerView.setAdapter(new TestListAdapter(data));
        mRecyclerView.addOnScrollListener(getOnScrollListener());
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

    protected void updateCanTouch(boolean canTouch){
        this.canTouch = canTouch;
    }



    @Override
    public boolean isChildTouchEvent() {
        return canTouch;
    }


    @Override
    public void updateTouchEvent(boolean isInterceptTouchEvent) {
        if(mSvFirst != null)
            mSvFirst.setInterceptTouchEvent(isInterceptTouchEvent);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        mFlMain.getLocationOnScreen(location);
        int yPosition = location[1];
        if (yPosition < mFlMain.getHeightOffset()) {//移动到顶部，小于顶部偏移量(ViewPager完全显示)
            updateTouchEvent(false);
        } else {
            updateTouchEvent(true);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh(2000);
    }
}
