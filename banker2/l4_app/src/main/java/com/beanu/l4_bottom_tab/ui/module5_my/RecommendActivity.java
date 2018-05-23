package com.beanu.l4_bottom_tab.ui.module5_my;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的 推荐页面
 */
public class RecommendActivity extends BaseSDActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;

    RecommentFragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomment);
        ButterKnife.bind(this);

        mFragmentAdapter = new RecommentFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public String setupToolBarTitle() {
        return "推荐";
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


    private class RecommentFragmentAdapter extends FragmentStatePagerAdapter {

        String[] mTitleList;

        RecommentFragmentAdapter(FragmentManager fm) {
            super(fm);
            mTitleList = new String[]{"直播", "网课", "图书"};
        }

        @Override
        public Fragment getItem(int position) {

            return RecommendFragment.newInstance(position, "");
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
