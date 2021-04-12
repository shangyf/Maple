package com.seven.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by SeVen on 2021/3/10 10:34
 */
public class DensityUtil {

    // 根据手机的分辨率将dp的单位转成px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 根据手机的分辨率将px(像素)的单位转成dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Context context) {
        Resources res = context.getResources();
        DisplayMetrics metric = res.getDisplayMetrics();
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Context context) {
        Resources res = context.getResources();
        DisplayMetrics metric = res.getDisplayMetrics();
        return metric.heightPixels;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return 屏幕分辨率幕高度
     */
    public static int getScreenDpi(Context context) {
        Resources res = context.getResources();
        DisplayMetrics metric = res.getDisplayMetrics();
        return metric.densityDpi;
    }

    /**
     * 获取真实屏幕密度
     *
     * @param context 上下文【注意，Application和Activity的屏幕密度是不一样的】
     * @return
     */
    public static int getRealDpi(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        float xdpi = metric.xdpi;
        float ydpi = metric.ydpi;

        return (int) (((xdpi + ydpi) / 2.0F) + 0.5F);
    }

    /**
     * 获取应用窗口的尺寸
     *
     * @param activity 应用窗口
     * @param isReal   是否是真实的尺寸
     * @return 应用窗口的尺寸
     */
    public static Point getAppSize(Activity activity, boolean isReal) {
        return getDisplaySize(activity, isReal);
    }

    /**
     * 获取屏幕的尺寸
     *
     * @param isReal 是否是真实的尺寸
     * @return 屏幕的尺寸
     */
    public static Point getScreenSize(Context context, boolean isReal) {
        return getDisplaySize(context, isReal);
    }

    /**
     * 获取上下文所在的尺寸
     *
     * @param context 上下文
     * @param isReal  是否是真实的尺寸
     * @return 上下文所在的尺寸
     */
    public static Point getDisplaySize(Context context, boolean isReal) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return null;
        }
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (isReal) {
            display.getRealSize(point);
        } else {
            display.getSize(point);
        }
        return point;
    }

    /**
     * 获取应用窗口的度量信息
     *
     * @param activity 应用窗口
     * @param isReal   是否是真实的度量信息
     * @return 应用窗口的度量信息
     */
    public static DisplayMetrics getAppMetrics(Activity activity, boolean isReal) {
        return getDisplayMetrics(activity, isReal);
    }

    /**
     * 获取屏幕的度量信息
     *
     * @param isReal 是否是真实的度量信息
     * @return 屏幕的度量信息
     */
    public static DisplayMetrics getScreenMetrics(Context context, boolean isReal) {
        return getDisplayMetrics(context, isReal);
    }

    /**
     * 获取上下文所在的度量信息
     *
     * @param context 上下文
     * @param isReal  是否是真实的度量信息
     * @return 上下文所在的度量信息
     */
    public static DisplayMetrics getDisplayMetrics(Context context, boolean isReal) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return null;
        }
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        if (isReal) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }
        return metrics;
    }


    /**
     * 底部导航条是否开启
     *
     * @param context 上下文
     * @return 底部导航条是否显示
     */
    public static boolean isNavigationBarExist(Context context) {
        return getNavigationBarHeight(context) > 0;
    }

    /**
     * 获取系统底部导航栏的高度
     *
     * @param context 上下文
     * @return 系统状态栏的高度
     */
    public static int getNavigationBarHeight(Context context) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return 0;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealMetrics(realDisplayMetrics);
        }
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        if (realHeight - displayHeight > 0) {
            return realHeight - displayHeight;
        }
        return Math.max(realWidth - displayWidth, 0);
    }

}
