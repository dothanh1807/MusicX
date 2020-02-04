package com.example.exercise2.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.exercise2.R;

public class ViewUtils {

    public static final int TIME_VIBRATE = 20;

    public static final float RADIUS = 5f;// ThanhNgD: 1f - 25f

    public static final float SCALE_BITMAP = 0.06f;

    public static final String COLOR_HIGHTLIGHT = "#FF41EB";

    public static final int IMAGE_DEFAULT = R.drawable.icon_notification;

    public static final int IMAGE_DEFAULT_LARGE = R.drawable.image_default;

    public static final int IMAGE_DELETE = R.drawable.icon_recycle_bin;

    public static final int TIME_POST_DELAY = 500;

    public static final int TIME_HANDLER_SEEKBAR = 1000;

    public static final int TIME_ANIMATION_FLIP_IMG = 1500;

    public static final String TIME_START_DEFAULT = "00:00";

    public static boolean setUIFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= 6) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        return true;
    }

    public static CharSequence highlightText(String searchString, String string) {
        if ( searchString != null && !searchString.equals("")) {
            String normalizedText = string.toLowerCase();
            int start = normalizedText.indexOf(searchString);
            if (start < 0) {
                return string;
            } else {
                Spannable highlighted = new SpannableString(string);
                while (start >= 0) {
                    int spanStart = Math.min(start, string.length());
                    int spanEnd = Math.min(start + searchString.length(), string.length());
                    highlighted.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_HIGHTLIGHT)), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(searchString, spanEnd);
                }
                return highlighted;
            }
        }

        return string;
    }

    public static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

}
