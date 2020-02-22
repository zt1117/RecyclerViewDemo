package com.example.camille;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by haha on 2017/9/12.
 */

public class ScrollDeleteLayout extends HorizontalScrollView {
    private int mScreenWidth;  //屏幕宽度
    private int mMenuWidth;//菜单宽度
    private boolean mIsOpen = false;

    private boolean once;

    public ScrollDeleteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = getScreenWidth(context);//由安卓内置类ScreenUtil获取屏幕宽度 ，注意要写一个ScreenUtil类
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
        * 自定义类是继承自HorizontalScrollView，那么意图就非常明显，就是要利用父类的水平滑动效果
        * 而HorizontalScrollView这个控件只允许放一个子控件，一般来说，一个控件是远远不够，那么就
        * 直接设置一个线性布局，然后再在线性布局里放置任意我们想放的控件，这样就解决了控件数量的
        * 限制，所以下面的getChildAt(0)让对应的就是设置的线性布局，获取对象，然后再由该对象获取布
        * 局中的控件对象，这样所有控件对象都能取到*/
        LinearLayout wrapper = (LinearLayout) getChildAt(0);
        ViewGroup menu = (ViewGroup) wrapper.getChildAt(1);//获取线性布局中的第二个控件对象
        mMenuWidth = mScreenWidth / 2;
        menu.getLayoutParams().width = mMenuWidth;//给获取的控件对象进行属性设置
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP://触碰抬起，判断手指滑动距离
                int scrollX = getScrollX();
                if (scrollX > mMenuWidth / 4 && !mIsOpen) {
                    mIsOpen = true;
                    this.smoothScrollTo(mMenuWidth, 0);
                }
                else {
                    mIsOpen = false;
                    this.smoothScrollTo(0, 0);
                }
                Log.e("zt", "ACTION_UP " + scrollX);
                return true;
        }

        return super.onTouchEvent(ev);
    }


    //获得屏幕高度
    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;

    }

    //获得屏幕宽度
    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;


    }


}