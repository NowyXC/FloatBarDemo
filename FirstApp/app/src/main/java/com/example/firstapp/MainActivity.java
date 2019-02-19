package com.example.firstapp;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.firstapp.fragment.ListFragment;
import com.example.firstapp.fragment.ScrollerViewFragment;
import com.example.firstapp.fragment.TextViewFragment;
import com.example.firstapp.widget.detailView.DetailScrollView;
import com.example.firstapp.widget.detailView.MatchViewPager;
import com.example.firstapp.widget.detailView.fragment.BaseDetailFrag;
import com.example.firstapp.widget.detailView.listener.DetailScrollStateListener;
import com.example.firstapp.widget.scrollview.ObservableScrollView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
* 查看说明文档 https://nowyxc.github.io/2019/02/19/Android吸顶效果——Smartrefresh-ScrollView-ViewPager-RecylerView/
**/
public class MainActivity extends AppCompatActivity implements OnRefreshListener, ObservableScrollView.ScrollViewListener, DetailScrollStateListener {

    private SmartRefreshLayout mRefreshView;
    private DetailScrollView mSvFirst;
    private View mHeaderView;
    private TabLayout mTabLayout;
    private MatchViewPager mVpMain;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        mRefreshView = findViewById(R.id.main_refreshView);
        mSvFirst = findViewById(R.id.main_SvFirst);
        mHeaderView = findViewById(R.id.tab_viewPager_HeaderView);
        mTabLayout = findViewById(R.id.main_TabLayout);
        mVpMain = findViewById(R.id.main_VpMain);

        initRefreshView();
        initScrollView();
        initTabLayout();
        initViewPager();
    }

    private void initRefreshView(){
        mRefreshView.setEnableLoadMore(false);//禁止加载smartRefresh的加载更多
        mRefreshView.setOnRefreshListener(this);//下拉刷新监听
        mRefreshView.setEnableOverScrollBounce(false);//是否启用越界回弹
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
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mVpMain.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initViewPager(){
        mFragments = new ArrayList<>();
        mFragments.add(ListFragment.newInstance());
        mFragments.add(ScrollerViewFragment.newInstance());
        mFragments.add(TextViewFragment.newInstance());
        mFragments.add(ListFragment.newInstance());
        mVpMain.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments));
    }





    @Override
    public boolean isChildTouchEvent() {
        Fragment fragment =  mFragments.get(mVpMain.getCurrentItem());
        if(fragment instanceof BaseDetailFrag){
            return ((BaseDetailFrag)fragment).canChildTouch();
        }
        return false;
    }


    @Override
    public void updateTouchEvent(boolean isInterceptTouchEvent) {
        if(mSvFirst != null)
            mSvFirst.setInterceptTouchEvent(isInterceptTouchEvent);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        mVpMain.getLocationOnScreen(location);
        int yPosition = location[1];
//        int visibility = mTopTabLayout.getVisibility();
        if (yPosition < mVpMain.getHeightOffset()) {//移动到顶部，小于顶部偏移量(ViewPager完全显示)
            updateTouchEvent(false);
//            if(visibility != View.VISIBLE)
//                mTopTabLayout.setVisibility(View.VISIBLE);
        } else {
            updateTouchEvent(true);
//            if(visibility != View.GONE)
//                mTopTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh(2000);
    }
}
