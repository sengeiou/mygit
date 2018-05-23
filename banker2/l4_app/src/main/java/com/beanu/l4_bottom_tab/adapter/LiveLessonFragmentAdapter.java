package com.beanu.l4_bottom_tab.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.ui.common.CommentFragment;
import com.beanu.l4_bottom_tab.ui.common.IntroWithWebFragment;
import com.beanu.l4_bottom_tab.ui.common.TeachersFragment;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonTimeTableFragment;

/**
 * 直播课详情  页面
 * Created by Beanu on 2017/3/7.
 */

public class LiveLessonFragmentAdapter extends FragmentStatePagerAdapter {

    String[] mTitleList;
    LiveLesson mLiveLesson;

    public LiveLessonFragmentAdapter(FragmentManager fm, LiveLesson lesson) {
        super(fm);
        mTitleList = new String[]{"课程介绍", "课程表", "师资介绍", "评论"};
        mLiveLesson = lesson;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1) {
            return LiveLessonTimeTableFragment.newInstance(mLiveLesson.getId(), 0, null);
        } else if (position == 2) {
            return TeachersFragment.newInstance(mLiveLesson.getId(), 0);
        } else if (position == 3) {
            return CommentFragment.newInstance(mLiveLesson.getId(), 0, mLiveLesson.getEvaluate_num(), mLiveLesson.getStar_rating());
        }

        return IntroWithWebFragment.newInstance(mLiveLesson.getIntroUrl());
    }

    @Override
    public int getCount() {
        return mTitleList.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList[position];
    }


}
