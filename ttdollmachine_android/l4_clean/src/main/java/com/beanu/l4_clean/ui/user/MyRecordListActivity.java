package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * 我的抓取记录
 */
public class MyRecordListActivity extends BaseTTActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;
    MyRecordListAdapter mMyRecordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_record_list);
        ButterKnife.bind(this);

        mMyRecordListAdapter = new MyRecordListAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMyRecordListAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public String setupToolBarTitle() {
        return "抓取记录";
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


    public static class MyRecordListAdapter extends FragmentStatePagerAdapter {

        private String[] mTitls;

        public MyRecordListAdapter(FragmentManager fm) {
            super(fm);
            mTitls = new String[]{"全部", "成功"};
        }

        @Override
        public Fragment getItem(int position) {
            return MyRecordListFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mTitls.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitls[position];
        }
    }

}