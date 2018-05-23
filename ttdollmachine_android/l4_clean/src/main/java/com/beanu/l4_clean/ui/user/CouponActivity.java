package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.APIService;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 优惠券
 */
public class CouponActivity extends BaseTTActivity {


    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.txt_card) TextView mTxtCard;

    CouponFragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);

        mFragmentAdapter = new CouponFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        initData();
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
        return "免费卡";
    }

    private void initData() {
        API.getInstance(APIService.class).freeCardNum().compose(RxHelper.<Map<String, Integer>>handleResult())
                .subscribe(new Observer<Map<String, Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Map<String, Integer> map) {
                        mTxtCard.setText(map.get("total") + "");
                        mTabLayout.getTabAt(0).setText("登录奖励（" + map.get("loginNum") + "）");
                        mTabLayout.getTabAt(1).setText("充值奖励（" + map.get("loginNum") + "）");

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private class CouponFragmentAdapter extends FragmentStatePagerAdapter {

        String[] mTitleList;

        CouponFragmentAdapter(FragmentManager fm) {
            super(fm);
            mTitleList = new String[]{"登录奖励", "充值奖励"};
        }

        @Override
        public Fragment getItem(int position) {

            return CouponFragment.newInstance(position == 0 ? 1 : 0);
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