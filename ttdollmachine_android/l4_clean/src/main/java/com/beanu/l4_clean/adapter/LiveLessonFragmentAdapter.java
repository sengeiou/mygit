package com.beanu.l4_clean.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.ui.home.DollListFragment;

/**
 * 直播课详情  页面
 * Created by Beanu on 2017/3/7.
 */

public class LiveLessonFragmentAdapter extends FragmentStatePagerAdapter {

    String[] mTitleList;

    public LiveLessonFragmentAdapter(FragmentManager fm) {
        super(fm);

        mTitleList = new String[AppHolder.getInstance().mSiteClassList.size() + 1];
        mTitleList[0] = "全部";

        for (int i = 1; i < AppHolder.getInstance().mSiteClassList.size() + 1; i++) {
            mTitleList[i] = AppHolder.getInstance().mSiteClassList.get(i - 1).getName();
        }

    }

    @Override
    public Fragment getItem(int position) {

        String id = "FFFFFF";

        if (position != 0 && position < AppHolder.getInstance().mSiteClassList.size() + 1) {
            id = AppHolder.getInstance().mSiteClassList.get(position - 1).getId();
        }

        return DollListFragment.newInstance(id);
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
