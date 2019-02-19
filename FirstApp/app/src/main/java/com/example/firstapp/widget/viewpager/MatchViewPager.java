package com.example.firstapp.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.firstapp.R;
import com.example.firstapp.utils.AppUtil;
import com.example.firstapp.utils.DeviceUtil;
import com.example.firstapp.utils.ViewUtil;

import java.util.logging.Logger;


/**
 * Created by Nowy on 2018/1/9.
 * 填充一页高度的ViewPager
 * 高度：ViewPager高度 = 屏幕高度 - 标记控件的高度 - mRemoveHeight - StatusBarHeight
 * 高度偏移量:mHeightOffset = 标记控件的高度 + mRemoveHeight + StatusBarHeight
 * app:targetId，设置统一屏幕显示的ViewID。
 * app:removeHeight 扣除的偏移量
 */

public class MatchViewPager extends ViewPager {
    public static final String TAG = MatchViewPager.class.getSimpleName();
    private int mTargetId;//标记ID,ViewPager高度 = 屏幕高度 - 标记控件的高度
    private int mRemoveHeight;//其他偏移量，可能需要减去标题栏的高度
    private int mHeightOffset = 0;//
    private int mTargetViewHeight;

    public MatchViewPager(Context context) {
        super(context);
        initHeight();
    }

    public MatchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
        initHeight();
    }


    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MatchViewPager);

        mTargetId = a.getResourceId(R.styleable.MatchViewPager_targetId,0);

        mRemoveHeight = a.getDimensionPixelOffset(R.styleable.MatchViewPager_removeHeight,0);
        a.recycle();
    }

    private void initHeight(){
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = calcHeight() +1;
                setLayoutParams(params);
            }
        });

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int height = calcHeight() + 1;
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }




    private int calcHeight(){
        ViewGroup parent = (ViewGroup) getParent();
        int height = DeviceUtil.getDeviceHeight(getContext()) - AppUtil.getStatusBarHeight(getContext());
        if(parent != null && mTargetId  > 0){
            View targetView = parent.findViewById(mTargetId);
            if(targetView != null){
                mTargetViewHeight = targetView.getHeight();
                if(mTargetViewHeight <= 0){
                    mTargetViewHeight =  ViewUtil.getViewHeight(targetView);
                }

                height = height - mTargetViewHeight;
            }
        }

        height = height - mRemoveHeight;//扣除偏移量
        return height;
    }




    public void setTargetId(int mTargetId) {
        this.mTargetId = mTargetId;
    }

    public void setRemoveHeight(int removeHeight) {
        this.mRemoveHeight = removeHeight;
    }

    public int getHeightOffset() {
        return mHeightOffset = (mRemoveHeight+ mTargetViewHeight+AppUtil.getStatusBarHeight(getContext()));
    }
}
