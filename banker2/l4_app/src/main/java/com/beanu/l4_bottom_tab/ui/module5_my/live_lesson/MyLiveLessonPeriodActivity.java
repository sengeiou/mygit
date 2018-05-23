package com.beanu.l4_bottom_tab.ui.module5_my.live_lesson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.MyLiveLesson;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonTimeTableFragment;

/**
 * 我的直播课 课时列表
 */
public class MyLiveLessonPeriodActivity extends BaseSDActivity {

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live_lesson_period);

        String liveLessonId = getIntent().getStringExtra("lessonId");
        title = getIntent().getStringExtra("title");
        MyLiveLesson myLiveLesson = getIntent().getParcelableExtra("lesson");

        Fragment fragment = LiveLessonTimeTableFragment.newInstance(liveLessonId, 1, myLiveLesson);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment, "fragment").commit();
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
        return title;
    }
}