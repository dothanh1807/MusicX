package com.example.exercise2.view.customizeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.exercise2.R;

/**
 * Created by CTC_TRAINING on 2/22/2019.
 */

public class Circle extends View {

    private static final int START_ANGLE_POINT = 90;

    private Paint mPaint;

    private RectF mRect;

    private float mSweepAngle;

    private int mStrokeWidth;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSweepAngle = 0;
        mStrokeWidth = 5;
        mPaint = new Paint();
        mRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRect.set(0, 0, getWidth(), getHeight());

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(getContext().getResources().getColor(R.color.hightLight));

        canvas.drawArc(mRect, START_ANGLE_POINT, mSweepAngle, false, mPaint);
    }

    public float getAngle() {
        return mSweepAngle;
    }

    public void setAngle(float angle) {
        mSweepAngle = angle;
    }

}

