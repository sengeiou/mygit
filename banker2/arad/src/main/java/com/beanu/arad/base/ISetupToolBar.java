package com.beanu.arad.base;

import android.view.View;
import android.widget.TextView;

/**
 * 设置Toolbar接口
 * Created by beanu on 14/12/24.
 */
public interface ISetupToolBar {

    String setupToolBarTitle();

    boolean setupToolBarLeftButton(View leftButton);

    boolean setupToolBarRightButton2(View rightButton2);

    boolean setupToolBarRightButton3(View rightButton3);

    boolean setupToolBarRightButton4(View rightButton4);

    boolean setupToolBarRightButton5(View rightButton5);

    boolean setupToolBarRightButton1(View rightButton1);

    // 动态改变
    TextView getToolBarTitle();

    View getToolBarLeftButton();

    View getToolBarRightButton2();

    //全选
    View getToolBarRightButton3();

    //取消
    View getToolBarRightButton4();

    //下载管理
    View getToolBarRightButton5();

    View getToolBarRightButton1();

}
