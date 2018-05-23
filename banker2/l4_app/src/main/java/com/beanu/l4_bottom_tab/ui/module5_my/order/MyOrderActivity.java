package com.beanu.l4_bottom_tab.ui.module5_my.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的订单
 */
@Route(path = "/app/my/orderList")
public class MyOrderActivity extends BaseSDActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;

    MyOrderFragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);

        int showPageNum = getIntent().getIntExtra("page", 0);

        mFragmentAdapter = new MyOrderFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(showPageNum);
    }

    @Override
    public String setupToolBarTitle() {
        return "我的订单";
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


    private class MyOrderFragmentAdapter extends FragmentStatePagerAdapter {

        String[] mTitleList;

        MyOrderFragmentAdapter(FragmentManager fm) {
            super(fm);
            mTitleList = new String[]{"直播", "网课"};
        }

        @Override
        public Fragment getItem(int position) {
            return MyOrderFragment.newInstance(position);
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
