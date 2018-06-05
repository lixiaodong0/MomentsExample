package com.lixd.moments.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

import com.lixd.moments.App;

import java.util.List;

public class CommonUtils {

    public static final boolean isEmpty(List data) {
        if (data == null || data.size() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * dp转换成px
     */
    public static final int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    private static final int px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) App.sContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return App.sContext.getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) App.sContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return App.sContext.getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }
}
