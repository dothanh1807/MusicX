package com.example.exercise2.view.customizeview;


import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.exercise2.view.ViewUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BitmapManager {
    private static BitmapManager mBackground;

    public Bitmap mWallPagerBlur;
    //private List<Bitmap> mListBitmap;

    //private List<Bitmap> mListBitmapBlur;

    public static final String DEFAULT = "default";

    public BitmapManager() {
        //mListBitmap = new ArrayList<Bitmap>();
        // mListBitmapBlur = new ArrayList<Bitmap>();
    }

    public static BitmapManager getInstance() {
        if (mBackground == null) {
            mBackground = new BitmapManager();
        }
        return mBackground;
    }


    public Bitmap getBitmapBlur(String key, Context context) {
        Bitmap mCurrentBimap = null;

        mCurrentBimap = getBitmapWallpaperBlur(context);

        if (mCurrentBimap == null) {
            mCurrentBimap = getBitmapBlur(context);
        }
        return mCurrentBimap;
    }

    // Blur ảnh wallpager cho máy không có ảnh blur từ ROM( của method getBitmapWallPager() )
    public Bitmap getBitmapBlur(Context context) {
        BlurManager blur = new BlurManager();
        Bitmap bitmapBlur = null;
        blur.bitmapScale(ViewUtils.SCALE_BITMAP).build(context, getBitmapWallPager(context));
        bitmapBlur = blur.blur(ViewUtils.RADIUS);

        return bitmapBlur;
    }

    // Trả về bitmap wallpaper
    public Bitmap getBitmapWallPager(Context context) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        return bitmap;
    }

    // Trả về bitmap wallpaper đã đc blur
    public Bitmap getBitmapWallpaperBlur(Context context) {
        Bitmap wallpaperBlur = null;
        WallpaperManager mWallpaperManager = WallpaperManager.getInstance(context);
        try {
            Method method = mWallpaperManager.getClass().getMethod("getBitmapWallpaperBlur");
            method.setAccessible(true);
            wallpaperBlur = (Bitmap) method.invoke(mWallpaperManager);
        } catch (NoSuchMethodException e) {
            return getBitmapBlur(context);
        } catch (InvocationTargetException e) {
            return getBitmapBlur(context);
        } catch (IllegalAccessException e) {
            return getBitmapBlur(context);
        }
        return wallpaperBlur;
    }
}
