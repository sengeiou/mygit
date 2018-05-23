package com.beanu.l4_clean.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.utils.statusbar.ImmersionBar;
import com.beanu.l4_clean.R;

/**
 * Created by Beanu on 2018/2/22.
 */

public abstract class SimplestListActivity<T> extends com.beanu.l2_recyclerview.SimplestListActivity<T> {
    private View mStatusBar;//状态栏

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置背景
        View view = this.getWindow().getDecorView();   //getDecorView 获得window最顶层的View
        view.setBackgroundResource(R.drawable.bg);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mStatusBar = findViewById(R.id.arad_status_bar);


        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this);

            //在沉浸式状态栏的时候，保证内容视图与状态栏不重叠。两种方案。
            if (mStatusBar != null) {
                mImmersionBar.statusBarView(mStatusBar).init();
            } else {
                mImmersionBar.transparentStatusBar().init();
            }
        }

        //设置toolbar的低版本的高度
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (getToolbar() != null) {
                ViewGroup.LayoutParams layoutParams = getToolbar().getLayoutParams();
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.toolbar);
                getToolbar().setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    protected void setStatusBar() {

    }
}
