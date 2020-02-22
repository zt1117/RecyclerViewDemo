package com.example.camille;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.camille.loding.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by camille on 2020/2/18.
 */

public class RefreshLayout extends LinearLayout implements NestedScrollingParent {
    private boolean isMeasured;
    //刷新的头部局
    private View mHeader;
    //RecycleView列表页
    private RecyclerView mContent;
    //头部局的高度
    private int mHeaderHeight;
    private Scroller mScroller;

    //刷新提示
    private TextView mTvTip;

    //上次刷新的时间
    private long mLastTime;
    private TextView mTvTime;
    private ValueAnimator mUpAnim;
    private ValueAnimator mDownAnim;

    private enum STATUS {
        //刷新中,下拉刷新，释放刷新
        REFRESHING, PULLREFRESH, RELEASEREFRESH
    }

    //当前刷新状态
    private STATUS mStatus = STATUS.PULLREFRESH;
    private STATUS mLastStatus;


    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        View view = LayoutInflater.from(context).inflate(R.layout.rv_header, this, false);
        mTvTip = view.findViewById(R.id.tv_tip);
        mTvTime = view.findViewById(R.id.tv_time);
        addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeader.layout(0, -mHeaderHeight, getWidth(), 0);
        mContent.layout(0, 0, getWidth(), getBottom());
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        Log.e("zty", "onStartNestedScroll");
        // 刷新中不接受内部控件参数
        return mStatus != STATUS.REFRESHING;
    }

    /**
     * onStartNestedScroll方法返回true才执行该方法
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.e("zty", "onNestedPreScroll");
        //上滑头部局逐渐隐藏
        boolean hiddenRefresh = dy > 0 && getScrollY() <= 0;
        //下滑头部局逐渐出现
        boolean showRefresh = dy < 0 && getScrollY() >= -mHeaderHeight && !ViewCompat.canScrollVertically(target, -1);

        if (getScrollY() >= -mHeaderHeight / 2) {
            mStatus = STATUS.PULLREFRESH;
        } else {
            mStatus = STATUS.RELEASEREFRESH;
        }
        refreshByStatus(mStatus);

        if (hiddenRefresh || showRefresh) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("zty", "onMeasure");
        if (!isMeasured) {
            mHeader = getChildAt(0);
            mContent = (RecyclerView) getChildAt(1);
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //获取头部局的高度
            mHeaderHeight = mHeader.getMeasuredHeight();
            isMeasured = true;
            //recycleView手抬起时的监听
            mContent.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        if (getScrollY() <= -mHeaderHeight / 2) {
                            mStatus = STATUS.REFRESHING;
                            refreshByStatus(mStatus);
                        } else {
                            scrollTo(0, 0);
                        }
                    }
                    return false;
                }
            });

            mDownAnim = ValueAnimator.ofFloat(180, 360);
            mDownAnim.setDuration(500);


            mUpAnim = ValueAnimator.ofFloat(0, 180);
            mUpAnim.setDuration(500);

        }
    }

    /**
     * 根据状态去改变
     */
    private void refreshByStatus(STATUS status) {
        if (mLastStatus != status) {
            switch (status) {
                case PULLREFRESH:
                    if (mLastStatus != null) {
                        mTvTip.setText("下拉刷新");
                        mDownAnim.start();
                    }
                    break;
                case REFRESHING:
                    mTvTip.setText("正在刷新");
                    mLastTime = System.currentTimeMillis();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollTo(0, 0);
                            mStatus = STATUS.PULLREFRESH;
                            mLastStatus = null;
                            mTvTime.setText("上次刷新时间:" + formartTime(mLastTime));
                            //刷新完毕
                            if (mRefreshCompleteListener != null)
                                mRefreshCompleteListener.refreshed();
                        }
                    }, 1500);
                    break;
                case RELEASEREFRESH:
                    mTvTip.setText("释放刷新");
                    mUpAnim.start();
                    break;
            }
            mLastStatus = status;
        }
    }

    private String formartTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y <= -mHeaderHeight) {
            y = -mHeaderHeight;
        } else if (y >= 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public interface RefreshCompleteListener {
        void refreshed();
    }

    private RefreshCompleteListener mRefreshCompleteListener;

    public void setRefreshCompleteListener(RefreshCompleteListener refreshCompleteListener) {
        mRefreshCompleteListener = refreshCompleteListener;
    }
}