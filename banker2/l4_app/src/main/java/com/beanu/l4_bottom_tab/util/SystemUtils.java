package com.beanu.l4_bottom_tab.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

/**
 * 键盘相关
 * //TODO 后期整合到smart framer中
 * Created by Beanu on 2017/4/27.
 */

public class SystemUtils {

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    public static void hideSoftInput(Context context, View paramEditText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    public static void showKeyBoard(final Context context, final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            public void run() {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramEditText, 0);
            }
        });
    }

    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity) - getAppHeight(paramActivity);
        return height != 0;
    }

    public static int getAppContentHeight(Activity paramActivity) {
        return getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity) - getActionBarHeight(paramActivity) - getKeyboardHeight(paramActivity);
    }

    @TargetApi(14)
    public static int getActionBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= 14) {
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        return result;
    }

    //TODO 保存到sp中，下次先读取 没有值  再去计算
    // http://www.jianshu.com/p/328784b363b6?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io
    public static int getKeyboardHeight(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity) - getAppHeight(paramActivity);
        if (height == 0) {
            height = 400;
        }
        return height;
    }

    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        return getInternalDimensionSize(context.getResources(), "status_bar_height");
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }
}
