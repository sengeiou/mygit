package com.beanu.l4_bottom_tab.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.ui.common.CommentFragment;
import com.beanu.l4_bottom_tab.ui.common.IntroWithWebFragment;
import com.beanu.l4_bottom_tab.ui.common.TeachersFragment;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.OnlineLessonPeriodListFragment;

/**
 * 高清网课 adapter
 * Created by Beanu on 2017/3/7.
 */

public class OnlineLessonFragmentAdapter extends FragmentStatePagerAdapter {

    String[] mTitleList;
    OnlineLesson mOnlineLesson;

    public OnlineLessonFragmentAdapter(FragmentManager fm, OnlineLesson lesson) {
        super(fm);
        mTitleList = new String[]{"课程介绍", "课时列表", "师资介绍", "评论"};
        mOnlineLesson = lesson;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1) {
            return OnlineLessonPeriodListFragment.newInstance(mOnlineLesson.getId(), mOnlineLesson);
        } else if (position == 2) {
            return TeachersFragment.newInstance(mOnlineLesson.getId(), 1);
        } else if (position == 3) {
            return CommentFragment.newInstance(mOnlineLesson.getId(), 1, mOnlineLesson.getEvaluate_num(), mOnlineLesson.getStar_rating());
        }

        return IntroWithWebFragment.newInstance(Constants.URL + "recordingView/?id=" + mOnlineLesson.getId());
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
