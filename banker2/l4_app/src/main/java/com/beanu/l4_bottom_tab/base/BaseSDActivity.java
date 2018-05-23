package com.beanu.l4_bottom_tab.base;

import android.view.View;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.ToolBarActivity;

/**
 * 全局     基础类
 * Created by Beanu on 2017/3/6.
 */

public abstract class BaseSDActivity<T extends BasePresenter, E extends BaseModel> extends ToolBarActivity<T, E> {


//    @Override
//    protected void setStatusBar() {
//        StatusBarUtil.setTransparentForImageView(this, null);
//
//        //设置toolbar的低版本的高度
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            if (getToolbar() != null) {
//                ViewGroup.LayoutParams layoutParams = getToolbar().getLayoutParams();
//                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.toolbar);
//                getToolbar().setLayoutParams(layoutParams);
//            }
//        }
//    }
}
