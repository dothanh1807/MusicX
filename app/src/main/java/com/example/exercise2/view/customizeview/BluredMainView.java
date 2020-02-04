package com.example.exercise2.view.customizeview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.exercise2.R;

public class BluredMainView extends View implements BlurManager.OnBlurAnimationEndListener {
    private static final float SCALE_FACTOR = 0.1f;

    private static final int MIN_SCALED_PX = 240;

    private static final float BITMAP_SCALE = 0.2f;

    private BlurManager mBlurManager;

    private Bitmap mDefaultBitmap;

    protected Bitmap mBluredBitmap;

    private int mOverlayColor;

    protected int mScreenWidth;

    private int mScreenHeight;

    private float mScale = 1f;

    private int mTranslateX = 0;

    private int mTranslateY = 0;

    private Context mContext;

    public BluredMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mOverlayColor = getContext().getResources().getColor(R.color.colorBackGround);
        mBlurManager = new BlurManager(this, this).minRadius(BlurManager.DEFAULT_MIN_RADIUS)
                .maxRadius(BlurManager.DEFAULT_MAX_RADIUS);
        mBluredBitmap = BitmapManager.getInstance().getBitmapWallpaperBlur(mContext);

        //mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        setBlurBitmap();
    }

    public Bitmap getBitmapBlur() {
        return mBluredBitmap;
    }

    public void setBlurBitmap() {
        int bluredBitmapWidth = mBluredBitmap.getWidth();
        int bluredBitmapHeight = mBluredBitmap.getHeight();

        if ((float) bluredBitmapHeight / bluredBitmapWidth < (float) mScreenHeight / mScreenWidth) {
            mScale = (float) mScreenHeight / bluredBitmapHeight;
            mTranslateX = (int) (-(bluredBitmapWidth - mScreenWidth / mScale) / 2);
            mTranslateY = 0;

        } else {
            mScale = (float) mScreenWidth / bluredBitmapWidth;
            mTranslateX = 0;
            mTranslateY = (int) (-(bluredBitmapHeight - mScreenHeight / mScale) / 2);
        }

    }

    public void redraw() {
        mBluredBitmap = BitmapManager.getInstance().getBitmapWallpaperBlur(mContext);
        setBlurBitmap();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScale, mScale);
        canvas.translate(mTranslateX, mTranslateY);
        canvas.drawBitmap(mBluredBitmap, 0, 0, null);
        canvas.drawColor(mOverlayColor);

    }

    private int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Tra ve anh co kich thuoc phu hop
     *
     * @param inputBitmap : anh lam mo dan dan
     * @return
     */
    public float getScale(Bitmap inputBitmap, float scaleFactor) {
        int requestWidht = (int) (mScreenWidth / scaleFactor);
        int requestHeight = (int) (mScreenHeight / scaleFactor);
        int sampleSize = calculateInSampleSize(inputBitmap.getWidth(), inputBitmap.getHeight(),
                requestWidht, requestHeight);

        return 1f / sampleSize;
    }

    @Override
    public void onBlurAnimationEnd(boolean reversing) {
        invalidate();
    }

    @Override
    public void onBluredViewAlphaChanged(float alpha, Bitmap bitmap) {
        mBluredBitmap = bitmap;
        // TODO Auto-generated method stub
        int bluredBitmapWidth = mBluredBitmap.getWidth();
        int bluredBitmapHeight = mBluredBitmap.getHeight();

        if ((float) bluredBitmapHeight / bluredBitmapWidth < (float) mScreenHeight / mScreenWidth) {
            mScale = (float) mScreenHeight / bluredBitmapHeight;
            mTranslateX = (int) (-(bluredBitmapWidth - mScreenWidth / mScale) / 2);
            mTranslateY = 0;

        } else {
            mScale = (float) mScreenWidth / bluredBitmapWidth;
            mTranslateX = 0;
            mTranslateY = (int) (-(bluredBitmapHeight - mScreenHeight / mScale) / 2);
        }
        invalidate();
    }
}
