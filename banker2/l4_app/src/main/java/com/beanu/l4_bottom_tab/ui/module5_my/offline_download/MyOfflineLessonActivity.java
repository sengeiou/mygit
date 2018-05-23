package com.beanu.l4_bottom_tab.ui.module5_my.offline_download;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 * 已下载课程  列表
 */

public class MyOfflineLessonActivity extends BaseSDActivity {


    @BindView(R.id.toolbar_left_btn)
    ImageView toolbarLeftBtn;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offline_lesson);
        ButterKnife.bind(this);
    }


    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        return "已下载课程";
    }
}
