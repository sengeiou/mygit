package com.beanu.l4_bottom_tab.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.RadioGroup;

/**
 * 动态 单选
 * Created by Beanu on 2017/3/21.
 */

public class RadioGroupListView extends RadioGroup {

    private ListAdapter adapter;

    /**
     * 绑定布局
     */
    private void bindLinearLayout() {
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(params);
            v.setId(i);
            addView(v, i);
        }
        Log.v("countTAG", "" + count);
    }

    public RadioGroupListView(Context context) {
        super(context);
    }

    public RadioGroupListView(Context context, AttributeSet attrs) {
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

}
