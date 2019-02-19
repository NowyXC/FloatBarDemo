package com.example.firstapp.widget.detailView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.firstapp.utils.AppUtil;
import com.example.firstapp.utils.DeviceUtil;
import com.example.firstapp.utils.ViewUtil;
import com.example.firstapp.R;

/**
 * Created by Nowy on 2018/1/9.
 * 填充一页高度的ViewPager
 * 高度：MatchFrameLayout高度 = 屏幕高度 - 标记控件的高度 - mRemoveHeight - StatusBarHeight
 * 高度偏移量:mHeightOffset = 标记控件的高度 + mRemoveHeight + StatusBarHeight
 * app:mfl_targetId，设置统一屏幕显示的ViewID。
 * app:mfl_removeHeight 扣除的偏移量
 */

public class MatchFrameLayout extends FrameLayout {
    public static final String TAG = MatchFrameLayout.class.getSimpleName();
    private int mTargetId;//标记ID,ViewPager高度 = 屏幕高度 - 标记控件的高度
    private int mRemoveHeight;//其他偏移量，可能需要减去标题栏的高度
    private int mTargetViewHeight;//标记控件的高度
    private int mStatusBarHeight;//状态栏高度
    public MatchFrameLayout(Context context) {
        super(context);
        initHeight();
    }

    public MatchFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
        initHeight();
    }


    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MatchFrameLayout);

        mTargetId = a.getResourceId(R.styleable.MatchFrameLayout_mfl_targetId,0);

        mRemoveHeight = a.getDimensionPixelOffset(R.styleable.MatchFrameLayout_mfl_removeHeight,0);
        mStatusBarHeight = AppUtil.getStatusBarHeight(getContext());
        a.recycle();
    }

    private void initHeight(){
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = calcHeight()+1;
                setLayoutParams(params);
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private int calcHeight(){
        ViewGroup parent = (ViewGroup) getParent();
        int height = DeviceUtil.getDeviceHeight(getContext()) - mStatusBarHeight;
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
        return  mRemoveHeight+ mTargetViewHeight+mStatusBarHeight;
    }
}
