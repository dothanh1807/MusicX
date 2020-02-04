package com.example.exercise2.view.customizeview;

import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class BlurManager {
    private static final float DEFAULT_BITMAP_SCALE = 0.4f;

    public static final float DEFAULT_MIN_RADIUS = 1f;

    public static final float DEFAULT_MAX_RADIUS = 25f;

    private static final long DERAUL_DURATION = 200;

    private TimeAnimator mTimeAnimator;

    private BlurTimeListener mAnimationCallback;

    private View mBluredView;

    private Bitmap mInputBitmap;

    private RenderScript mRenderScript;

    private ScriptIntrinsicBlur mScriptIntrinsicBlur;

    private float mMinRadius;

    private float mMaxRadius;

    private boolean mUseSetImageBitmap;

    private float mBitmapScale;

    private float mCurveFactor;

    private OnBlurAnimationEndListener mOnBlurAnimationEndListener;

    private boolean mIsBuilt;

    private Allocation mInputAllocation, mOutputAllocation;

    public BlurManager() {

    }

    public BlurManager(View bluredView, OnBlurAnimationEndListener onBlurAnimationEndListener) {
        mBluredView = bluredView;
        mOnBlurAnimationEndListener = onBlurAnimationEndListener;

        mUseSetImageBitmap = false;
        mMinRadius = DEFAULT_MIN_RADIUS;
        mMaxRadius = DEFAULT_MAX_RADIUS;
        mBitmapScale = DEFAULT_BITMAP_SCALE;

        mIsBuilt = false;
    }

    public BlurManager build(Context context, Bitmap imageBitmap) {
        int width = Math.round(imageBitmap.getWidth() * mBitmapScale);
        int height = Math.round(imageBitmap.getHeight() * mBitmapScale);

        mInputBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);

        mRenderScript = RenderScript.create(context);
        mScriptIntrinsicBlur = ScriptIntrinsicBlur.create(mRenderScript,
                Element.U8_4(mRenderScript));
        destroyAllocation();
        mInputAllocation = Allocation.createFromBitmap(mRenderScript, mInputBitmap);
        mScriptIntrinsicBlur.setInput(mInputAllocation);

        mAnimationCallback = new BlurTimeListener(this, mMinRadius, mMaxRadius, 0);
        mAnimationCallback.setDuration(DERAUL_DURATION);

        mTimeAnimator = new TimeAnimator();
        mTimeAnimator.setInterpolator(new DecelerateInterpolator(mCurveFactor));

        mIsBuilt = true;

        return this;
    }

    public BlurManager build(Activity context, View bitmapSourceView) {
        Bitmap imageBitmap = getScreenshot(bitmapSourceView);

        return build(context, imageBitmap);
    }

    public BlurManager useSetImageBitmap(boolean useSetImageBitmap) {
        mUseSetImageBitmap = useSetImageBitmap;

        return this;
    }

    public BlurManager minRadius(float minRadius) {
        mMinRadius = minRadius;

        return this;
    }

    public BlurManager maxRadius(float maxRadius) {
        mMaxRadius = maxRadius;

        return this;
    }

    public BlurManager bitmapScale(float bitmapScale) {
        mBitmapScale = bitmapScale;

        return this;
    }

    public BlurManager curveFactor(float curveFactor) {
        mCurveFactor = curveFactor;

        return this;
    }

    public void setDuration(long duration) {
        mAnimationCallback.setDuration(duration);
    }

    public void start() {
        if (!mIsBuilt) {
            throw new RuntimeException("Must call build() before calling start().");
        }

        //mBluredView.setAlpha(0f);
        mAnimationCallback.setReversing(false);
        doAnimation();
    }

    public void reverse() {
        if (!mIsBuilt) {
            throw new RuntimeException("Must call build() before calling start().");
        }

        mAnimationCallback.setReversing(true);
        doAnimation();
    }

    private void doAnimation() {
        if (!mTimeAnimator.isStarted()) {
            mTimeAnimator.setTimeListener(mAnimationCallback);
            mTimeAnimator.setRepeatMode(ObjectAnimator.RESTART);
            mTimeAnimator.start();
        }
    }

    public Bitmap blur(float radius) {
        mScriptIntrinsicBlur.setRadius(radius);

        Bitmap outputBitmap = Bitmap.createBitmap(mInputBitmap);
        mOutputAllocation = Allocation.createFromBitmap(mRenderScript, outputBitmap);
        mScriptIntrinsicBlur.forEach(mOutputAllocation);
        mOutputAllocation.copyTo(outputBitmap);
        destroyAllocation();
        return outputBitmap;
    }

    public Bitmap getScreenshot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint paint = new Paint();
        paint.setAlpha(5);
        c.drawBitmap(b, 0, 0, paint);
        v.draw(c);
        return b;
    }

    public void destroyAllocation() {
        if (mInputAllocation != null) {
            mInputAllocation.destroy();
            mInputAllocation = null;
        }

        if (mOutputAllocation != null) {
            mOutputAllocation.destroy();
            mOutputAllocation = null;
        }
    }

    public void onBlurImageChanged(float alpha, Bitmap newBluredBitmap) {
        mOnBlurAnimationEndListener.onBluredViewAlphaChanged(alpha, newBluredBitmap);
        /* mBluredView.setAlpha(alpha);
         if (mUseSetImageBitmap && mBluredView instanceof ImageView) {
             ((ImageView) mBluredView).setImageBitmap(newBluredBitmap);

         } else {
             mBluredView.setBackground(new BitmapDrawable(newBluredBitmap));
         }*/
    }

    public void onBlurAnimationEnd() {
        mTimeAnimator.cancel();

        if (mOnBlurAnimationEndListener != null) {
            mOnBlurAnimationEndListener.onBlurAnimationEnd(mAnimationCallback.isReversing());
        }
    }

    public interface OnBlurAnimationEndListener {
        void onBlurAnimationEnd(boolean reversing);

        void onBluredViewAlphaChanged(float alpha, Bitmap newBitmap);
    }

    private static class BlurTimeListener implements TimeAnimator.TimeListener {
        private static final float MIN_RADIUS = 0.1f;

        private static final float MAX_RADIUS = 25f;

        private float mMinRadius;

        private float mMaxRadius;

        private long mDuration;

        private BlurManager mBlurManager;

        private boolean mReversing;

        public BlurTimeListener(BlurManager blurManager, float minRadius, float maxRadius,
                                long duration) {
            mDuration = duration;
            mBlurManager = blurManager;
            mMaxRadius = maxRadius;
            mMinRadius = minRadius;

            mReversing = false;
        }

        public void setDuration(long duration) {
            mDuration = duration;
        }

        public void setReversing(boolean reversing) {
            mReversing = reversing;
        }

        public boolean isReversing() {
            return mReversing;
        }

        @Override
        public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
            if (totalTime < mDuration) {
                if (totalTime == 0) {
                    totalTime += 1;
                }
                float fraction = (float) totalTime / mDuration;
                //float alpha = mReversing ? 1.0f - fraction : fraction;
                Bitmap blurBitmap = mBlurManager.blur(getCurrentRadius(totalTime));
                mBlurManager.onBlurImageChanged(0.4f, blurBitmap);

            } else if (totalTime > mDuration) {
                mBlurManager.onBlurAnimationEnd();
            }
        }

        private float getCurrentRadius(long totalTime) {
            if (!mReversing) {
                return mMinRadius + (mMaxRadius - mMinRadius) / mDuration * totalTime;
            } else {
                return mMaxRadius - (mMaxRadius - mMinRadius) / mDuration * totalTime;
            }
        }
    }
}
