package com.beanu.l4_clean.ui.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l4_clean.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 排行榜
 */
public class RankActivity extends BaseTTActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);

        RankFragmentAdapter adapter = new RankFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public String setupToolBarTitle() {
        return "排行榜";
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

    public static class RankFragmentAdapter extends FragmentStatePagerAdapter {

        String[] mTitleList;

        public RankFragmentAdapter(FragmentManager fm) {
            super(fm);
            mTitleList = new String[]{"日榜", "周榜", "月榜"};
        }

        @Override
        public Fragment getItem(int position) {
            return RankFragment.newInstance(position);
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