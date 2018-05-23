package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.l4_clean.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的消息
 */
public class MyMessageActivity extends ToolBarActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;

    MyMessageFragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        ButterKnife.bind(this);

        int showPageNum = getIntent().getIntExtra("page", 0);

        mFragmentAdapter = new MyMessageFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(showPageNum);
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
        return "通知中心";
    }

    private class MyMessageFragmentAdapter extends FragmentStatePagerAdapter {

        String[] mTitleList;

        MyMessageFragmentAdapter(FragmentManager fm) {
            super(fm);
            mTitleList = new String[]{"平台", "活动", "客服"};
        }

        @Override
        public Fragment getItem(int position) {
            return MyMessageFragment.newInstance(position);
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