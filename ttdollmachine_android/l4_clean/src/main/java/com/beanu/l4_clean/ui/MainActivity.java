package com.beanu.l4_clean.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.Arad;
import com.beanu.arad.support.updateversion.UpdateChecker;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_login.SignIn2Activity;
import com.beanu.l3_search.SearchActivity;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.LiveLessonFragmentAdapter;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.mvp.contract.MainContract;
import com.beanu.l4_clean.mvp.model.MainModelImpl;
import com.beanu.l4_clean.mvp.presenter.MainPresenterImpl;
import com.beanu.l4_clean.support.GlideImageLoader;
import com.beanu.l4_clean.support.dialog.FreeCardTipsDialogFragment;
import com.beanu.l4_clean.support.push.PushManager;
import com.beanu.l4_clean.ui.anchor.AnchorListActivity;
import com.beanu.l4_clean.ui.home.RankActivity;
import com.beanu.l4_clean.ui.pk.ChallengeActivity;
import com.beanu.l4_clean.ui.user.MoreActivity;
import com.beanu.l4_clean.ui.user.RechargeActivity;
import com.beanu.l4_clean.ui.user.ShareActivity;
import com.beanu.l4_clean.ui.user.UserCenterActivity;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceManager;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
@Route(path = "/app/main")
public class MainActivity extends BaseTTActivity<MainPresenterImpl, MainModelImpl> implements MainContract.View, View.OnTouchListener {

    @BindView(R.id.banner) Banner mBanner;
    @BindView(R.id.btn_charge) ImageButton mBtnCharge;
    @BindView(R.id.btn_rank) ImageButton mBtnPractice;
    @BindView(R.id.tab_layout_live_lesson_detail) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.activity_main) CoordinatorLayout mActivityMain;
    @BindView(R.id.appBar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.img_title) ImageView mImgTitle;

    public static final int EXIT_CODE = 123;

    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //设置toolbar在4.4版本以下的高度
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            int height = getResources().getDimensionPixelSize(R.dimen.toolbar);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            mToolbar.setLayoutParams(layoutParams);
        }

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float alpha = Math.abs(verticalOffset / (float) appBarLayout.getTotalScrollRange());
                mImgTitle.setAlpha(alpha);
            }
        });


        mBtnCharge.setOnTouchListener(this);
        mBtnPractice.setOnTouchListener(this);

        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                BannerItem item = mPresenter.getBannerList().get(position);

                if ("0".equals(item.getType())) {
                    WebActivity.startActivity(MainActivity.this, item.getUrl(), item.getTitle());
                } else if ("1".equals(item.getType())) {
                    //分享
                    startActivity(ShareActivity.class);

                } else if ("2".equals(item.getType())) {

                } else if ("3".equals(item.getType())) {
                    WebActivity.startActivity(MainActivity.this, item.getUrl(), item.getTitle());
                } else if ("4".equals(item.getType())) {
                    Intent intent = new Intent(MainActivity.this, AnchorListActivity.class);
                    startActivity(intent);
                } else if ("5".equals(item.getType())) {
                    Intent intent = new Intent(MainActivity.this, ChallengeActivity.class);
                    startActivity(intent);
                }


            }
        });

        //版本更新提示
        String version = AppHolder.getInstance().mConfig.getApkversion();
        if (!TextUtils.isEmpty(version)) {
            if (Integer.parseInt(version) > 0) {
                UpdateChecker.checkForDialog(MainActivity.this, AppHolder.getInstance().mConfig.getDetail(), AppHolder.getInstance().mConfig.getApkurl(), Integer.parseInt(version));
            }
        }

        if (AppHolder.getInstance().user.isLogin()) {
            //设置极光别名＋tag
            setAlias(AppHolder.getInstance().user.getId());
        }


        //启动推送过来的详情
        Bundle bundle = getIntent().getBundleExtra(Constants.EXTRA_BUNDLE);
        if (bundle != null) {
            //如果bundle存在，取出其中的参数，启动DetailActivity
            String title = bundle.getString("title");
            String url = bundle.getString("url");
            url += AppHolder.getInstance().user.getId();

            if (!TextUtils.isEmpty(url)) {
                WebActivity.startActivity(this, url, title);
            }
        }

        //TODO TEST
        uiRefresh(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppHolder.getInstance().reset();

        //七牛直播释放资源
        RTCMediaStreamingManager.deinit();
        RTCConferenceManager.deinit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 两次返回键，退出程序
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MainActivity.EXIT_CODE) {
            startActivity(SignIn2Activity.class);
            finish();
        }
    }


    private void uiRefresh(User user) {

        LiveLessonFragmentAdapter mFragmentAdapter = new LiveLessonFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.btn_charge, R.id.btn_rank, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_charge:
                startActivity(RechargeActivity.class);
                break;
            case R.id.btn_rank:
                startActivity(RankActivity.class);
                break;
            case R.id.search:
                startActivity(SearchActivity.class);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImageButton touchedButton = (ImageButton) view;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchedButton.getDrawable().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                touchedButton.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchedButton.getDrawable().clearColorFilter();
                touchedButton.invalidate();
                break;
        }
        return false;
    }

    @Override
    public void uiRefreshBanner(List<BannerItem> itemList) {
        mBanner.setImages(itemList);
        mBanner.start();
    }

    @Override
    public void uiShowUserFreeTips() {
        boolean notice = Arad.preferences.getBoolean(Constants.P_NOTICE);
        if (!notice) {
            FreeCardTipsDialogFragment.show(getSupportFragmentManager());
        }
    }

//    @Override
//    public void uiShowNewUserFreeTips() {
//        NewUserDialogFragment.show(getSupportFragmentManager(), new NewUserDialogFragment.Listener() {
//            @Override
//            public void onClose() {
//                mPresenter.rewardsInfo();
//            }
//        });
//        KLog.d("show dialog");
//    }

//    @Override
//    public void uiShowRewardsDialog(Rewards rewards) {
//        RewardsDialogFragment.show(getSupportFragmentManager(), rewards, new RewardsDialogFragment.Listener() {
//            @Override
//            public void onClose() {
//                mPresenter.userBalance();
//            }
//        });
//    }

    @Override
    public void uiUserBalance() {
//        mTxtYue.setText("开心币:" + AppHolder.getInstance().user.getCoins());
    }


    private void setAlias(String alias) {
        PushManager.setAlias(getApplicationContext(), alias);
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoreActivity.class);
//                startActivityForResult(intent, 100);
                startActivity(intent);

            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton) {
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserCenterActivity.class);
                startActivity(intent);
            }
        });
        return true;
    }


}