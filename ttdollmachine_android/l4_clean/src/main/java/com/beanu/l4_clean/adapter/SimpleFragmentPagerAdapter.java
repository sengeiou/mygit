package com.beanu.l4_clean.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lizhihua on 2017/5/3.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> fragments;
    private List<String> titles;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<? extends Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<? extends Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments == null ? null : (position < fragments.size() ? fragments.get(position) : null);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? null : (position < titles.size() ? titles.get(position) : null);
    }
}
