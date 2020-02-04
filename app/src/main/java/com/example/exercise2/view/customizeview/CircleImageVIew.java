package com.example.exercise2.view.customizeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


public class CircleImageVIew extends android.support.v7.widget.AppCompatImageView {

    public CircleImageVIew(Context context) {
        super(context);
    }

    public CircleImageVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Lấy ảnh được set trong src
        Drawable drawable = getDrawable();
        ColorFilter colorFilter = getColorFilter();
        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int sizeView = Math.min(getWidth(), getHeight());

        Bitmap roundBitmap = getRoundBitmap(bitmap, sizeView, colorFilter);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    public static Bitmap getRoundBitmap(Bitmap bmp, int sizeView, ColorFilter colorFilter) {
        Bitmap sBmp;

        if (bmp.getWidth() != sizeView || bmp.getHeight() != sizeView) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / sizeView;
            sBmp = Bitmap.createScaledBitmap(bmp, (int)(bmp.getWidth() / factor), (int)(bmp.getHeight() / factor), false);
        } else {
            sBmp = bmp;
        }

        Bitmap bitmap = Bitmap.createBitmap(sizeView, sizeView,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sizeView, sizeView);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColorFilter(colorFilter);
        canvas.drawARGB(0, 0, 0, 0);

        float centerX = sizeView / 2;
        float centerY = sizeView / 2;
        float radius = sizeView / 2;
        canvas.drawCircle(centerX, centerY, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sBmp, rect, rect, paint);

        return bitmap;
    }

}
