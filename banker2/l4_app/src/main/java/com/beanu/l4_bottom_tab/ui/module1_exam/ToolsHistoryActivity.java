package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 练习历史
 */
public class ToolsHistoryActivity extends BaseSDActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    HistoryFragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_history);
        ButterKnife.bind(this);

        mFragmentAdapter = new HistoryFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        onBackPressed();
    }

    private class HistoryFragmentAdapter extends FragmentStatePagerAdapter {

        String[] mTitleList;

        public HistoryFragmentAdapter(FragmentManager fm) {
            super(fm);
            mTitleList = new String[]{"练习", "试卷"};
        }

        @Override
        public Fragment getItem(int position) {
            return ToolsHistoryFragment.newInstance(position);
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
}
