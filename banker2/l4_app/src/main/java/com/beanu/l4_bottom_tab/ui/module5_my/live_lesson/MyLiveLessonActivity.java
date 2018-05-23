package com.beanu.l4_bottom_tab.ui.module5_my.live_lesson;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的直播课程
 */
public class MyLiveLessonActivity extends BaseSDActivity {

    @BindView(android.R.id.tabhost) FragmentTabHost mTabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live_lesson);
        ButterKnife.bind(this);

        //初始化FragmentTabHost
        mTabhost.setup(this, getSupportFragmentManager(), R.id.content);
        mTabhost.getTabWidget().setBackgroundColor(getResources().getColor(R.color.white));
        mTabhost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);//TODO 目前米没起作用

        Bundle bundle0 = new Bundle();
        bundle0.putInt("type", 0);
        mTabhost.addTab(mTabhost.newTabSpec("one").setIndicator(getTabItemView(R.drawable.tab_my_live_lesson_1, "未开课课程")), MyLiveLessonFragment.class, bundle0);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("type", 1);
        mTabhost.addTab(mTabhost.newTabSpec("two").setIndicator(getTabItemView(R.drawable.tab_my_live_lesson_2, "正在开播")), MyLiveLessonFragment.class, bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("type", 2);
        mTabhost.addTab(mTabhost.newTabSpec("three").setIndicator(getTabItemView(R.drawable.tab_my_live_lesson_3, "回放")), MyLiveLessonFragment.class, bundle2);

        mTabhost.getTabWidget().getChildAt(0).getLayoutParams().height = (int) getResources().getDimension(R.dimen.tab_height_my_live_lesson);
    }

    private View getTabItemView(int id, String title) {
        View view = getLayoutInflater().inflate(R.layout.item_tab_my_live_lesson, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageResource(id);

        TextView textView = (TextView) view.findViewById(R.id.tab_title);
        textView.setText(title);

        return view;
    }


    @Override
    public String setupToolBarTitle() {
        return "我的课程";
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
    public boolean setupToolBarRightButton1(View rightButton1) {
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MyTimeTableActivity.class);
            }
        });
        return true;

    }

//    @Override
//    public boolean setupToolBarRightButton(View rightButton) {
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(MyTimeTableActivity.class);
//            }
//        });
//        return true;
//    }
}
