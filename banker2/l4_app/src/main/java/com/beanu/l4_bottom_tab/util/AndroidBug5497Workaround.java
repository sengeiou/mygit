package com.beanu.l4_bottom_tab.util;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * //TODO 整合到smart framer中
 * 用来解决键盘弹出 底部弹上去 场景
 * Created by Beanu on 2017/4/27.
 */

public class AndroidBug5497Workaround {
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static AndroidBug5497Workaround assistActivity(@NonNull Activity activity) {
        return new AndroidBug5497Workaround(activity);
    }

    public void release() {
        mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private int contentHeight;
    private boolean isfirst = true;
    private int statusBarHeight;

    private ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            if (isfirst) {
                contentHeight = mChildOfContent.getHeight();//兼容华为等机型
                isfirst = false;
            }
            possiblyResizeChildOfContent();
        }
    };

    private AndroidBug5497Workaround(Activity activity) {
        //获取状态栏的高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);

        //界面出现变动都会调用这个监听事件
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        frameLayoutParams = (FrameLayout.LayoutParams)
                mChildOfContent.getLayoutParams();
    }

    //重新调整跟布局的高度
    private void possiblyResizeChildOfContent() {

        int usableHeightNow = computeUsableHeight();

        //当前可见高度和上一次可见高度不一致 布局变动
        if (usableHeightNow != usableHeightPrevious) {
            //int usableHeightSansKeyboard2 = mChildOfContent.getHeight();//兼容华为等机型
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight;
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
            } else {
                frameLayoutParams.height = contentHeight;
            }

            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算mChildOfContent可见高度     ** @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

}
