package com.example.camille.loding;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by camille on 2020/2/14.
 */

public class LodingView extends View {

    private Paint mPaint;

    private RectF mRectf;

    private int redius = 70;

    private int count = 0;

    public LodingView(Context context) {
        super(context);
        init();
    }

    public LodingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LodingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mRectf = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(redius * 2, redius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(count * 36, redius, redius);
        for (int i = 0; i < 10; i++){
            mPaint.setAlpha(255 - (10 - i) * 20);
            if (i >= 0 && i <= 3) {
                mRectf.set(redius - 5, 30, redius + 5, 40);
            } else if (i >= 4 && i < 6) {
                mRectf.set(redius - 6, 30, redius + 6, 42);
            } else if (i >= 6 && i < 8){
                mRectf.set(redius - 7, 30, redius + 7, 44);
            } else {
                mRectf.set(redius - 8, 30, redius + 8, 46);
            }
            canvas.drawRoundRect(mRectf, 16, 16, mPaint);
            canvas.rotate(36, redius, redius);
        }
        count ++;
        if (count > 9) {
            count = 0;
        }

        postInvalidateDelayed(100);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("zt", "onDetachedFromWindow");
    }
}
