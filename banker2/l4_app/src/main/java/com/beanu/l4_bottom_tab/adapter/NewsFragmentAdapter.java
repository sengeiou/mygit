package com.beanu.l4_bottom_tab.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beanu.l4_bottom_tab.model.bean.NewsTitle;
import com.beanu.l4_bottom_tab.ui.module_news.NewsListFragment;

import java.util.List;

/**
 * 资讯  页面
 * Created by Beanu on 2017/3/7.
 */

public class NewsFragmentAdapter extends FragmentStatePagerAdapter {

    private List<NewsTitle> mTitleList;
    private String provinceId;

    public NewsFragmentAdapter(FragmentManager fm, List<NewsTitle> list) {
        super(fm);
        mTitleList = list;
    }

    @Override
    public Fragment getItem(int position) {
        NewsTitle title = mTitleList.get(position);
        return NewsListFragment.newInstance(title.getId(), provinceId);
    }

    @Override
    public int getCount() {
        return mTitleList == null ? 0 : mTitleList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position).getName();
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
}
