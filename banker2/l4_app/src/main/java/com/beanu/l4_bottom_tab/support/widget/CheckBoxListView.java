package com.beanu.l4_bottom_tab.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * 多选列表
 * Created by Beanu on 2017/3/22.
 */

public class CheckBoxListView extends LinearLayout {

    private ListAdapter adapter;
    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = null;

    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(params);
            if (v instanceof CheckBox) {
                ((CheckBox) v).setOnCheckedChangeListener(mCheckedChangeListener);
            }
            v.setId(i);
            addView(v, i);
        }
        Log.v("countTAG", "" + count);
    }

    public CheckBoxListView(Context context) {
        super(context);
    }

    public CheckBoxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取Adapter
     *
     * @return adapter
     */
    public ListAdapter getAdpater() {
        return adapter;
    }

    /**
     * 设置数据
     *
     * @param adpater
     */
    public void setAdapter(ListAdapter adpater) {
        this.adapter = adpater;
        bindLinearLayout();
    }

    public CompoundButton.OnCheckedChangeListener getCheckedChangeListener() {
        return mCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        mCheckedChangeListener = checkedChangeListener;
    }
}
