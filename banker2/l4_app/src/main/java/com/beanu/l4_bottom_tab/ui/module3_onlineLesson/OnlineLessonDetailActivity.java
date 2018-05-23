package com.beanu.l4_bottom_tab.ui.module3_onlineLesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_shareutil.ShareDialogUtil;
import com.beanu.l2_shareutil.share.ShareListener;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.OnlineLessonFragmentAdapter;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.Period;
import com.beanu.l4_bottom_tab.mvp.contract.OnlineLessonDetailContract;
import com.beanu.l4_bottom_tab.mvp.model.OnlineLessonDetailModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.OnlineLessonDetailPresenterImpl;
import com.beanu.l4_bottom_tab.ui.common.LessonPayActivity;
import com.beanu.l4_bottom_tab.ui.download.DownloadListActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.ServicesActivity;
import com.bokecc.sdk.mobile.demo.drm.play.MediaPlayActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 高清网课 详情页
 */
@Route(path = "/app/onlineLesson/detail")
public class OnlineLessonDetailActivity extends MediaPlayActivity<OnlineLessonDetailPresenterImpl, OnlineLessonDetailModelImpl> implements OnlineLessonDetailContract.View {

    @BindView(R.id.txt_online_lesson_title) TextView mTxtTitle;
    @BindView(R.id.txt_online_lesson_time) TextView mTxtTime;
    @BindView(R.id.txt_online_lesson_share) TextView mTxtShare;
    @BindView(R.id.txt_online_lesson_comment) TextView mTxtComment;
    //    @BindView(R.id.recycle_view_online_lesson_period) RecyclerView mRecycleView;
    @BindView(R.id.tab_layout_online_lesson_detail) TabLayout mTabLayout;
    @BindView(R.id.viewPager_online_lesson_detail) ViewPager mViewPager;
    @BindView(R.id.txt_online_lesson_detail_price) TextView mTxtPrice;
    @BindView(R.id.txt_online_lesson_detail_buyer) TextView mTxtBuyer;
    @BindView(R.id.txt_expiry_date) TextView mTxtExpiryDate;
    @BindView(R.id.ll_online_lesson_detail_header) LinearLayout mLlOnlineLessonDetailHeader;
    @BindView(R.id.rl_bottom_price) RelativeLayout mRlBottomPrice;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_to_buy) Button mBtnToBuy;
    @BindView(R.id.btn_myonlinelesson_download) Button mBtnDownload;



    OnlineLessonFragmentAdapter mLessonFragmentAdapter;

    //    MultiTypeAdapter periodAdapter;
//    Items mPeriodItems;
    String lessonId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_lesson_detail);
        ButterKnife.bind(this);

        onCreate();

        lessonId = getIntent().getStringExtra("lessonId");


        //请求网络数据
        mPresenter.requestHttpData(lessonId);
        Arad.bus.register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);
    }

//    @Override
//    protected void setStatusBar() {
////        StatusBarUtil.setTransparentForImageView(this, null);
//
//        //设置toolbar的低版本的高度
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            ViewGroup.LayoutParams layoutParams = getToolbar().getLayoutParams();
//            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.toolbar);
//            getToolbar().setLayoutParams(layoutParams);
//        }
//    }


    @Override
    public void refreshView(OnlineLesson lesson) {
        mTxtTitle.setText(lesson.getName());
        if (!TextUtils.isEmpty(lesson.getLong_time())) {
            mTxtTime.setText(lesson.getLong_time() + "    " + lesson.getSale_num() + "次观看");
        } else {
            mTxtTime.setText(lesson.getSale_num() + "次观看");
        }

        mTxtExpiryDate.setText(Html.fromHtml("有效期：<font color='red'>" + lesson.getEndDays() + "</font>"));
        mTxtComment.setText("评论（" + lesson.getEvaluate_num() + "）");
        if (lesson.getIs_charges() == 1) {
//            mViewPager.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.tab_height));
            mTxtPrice.setText(Html.fromHtml("<small>¥</small><strong color='red'>" + lesson.getPrice() + "</strong>"));
            if (lesson.getIsBuy() == 0) {
                //未购买
                mBtnToBuy.setText("立即购买");
                mBtnToBuy.setBackgroundColor(getResources().getColor(R.color.base_color_orange));
            } else {
                //已购买
                //我添加 如果已经购买，就显示下载按钮

                mRlBottomPrice.setVisibility(View.INVISIBLE);
                mBtnDownload.setVisibility(View.VISIBLE);

                mBtnToBuy.setText("已购买");
                mBtnToBuy.setBackgroundColor(getResources().getColor(R.color.base_font_gray));
            }

            ((CoordinatorLayout.LayoutParams) (mViewPager.getLayoutParams())).setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.tab_height));

        } else {
            ((CoordinatorLayout.LayoutParams) (mViewPager.getLayoutParams())).setMargins(0, 0, 0, 0);
            mRlBottomPrice.setVisibility(View.GONE);
        }

        mTxtBuyer.setText("已有" + lesson.getSale_num() + "人购买");

        mLessonFragmentAdapter = new OnlineLessonFragmentAdapter(getSupportFragmentManager(), lesson);
        mViewPager.setAdapter(mLessonFragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void refreshPeriod(List<Period> periods) {
//        mPeriodItems.addAll(periods);
//        periodAdapter.notifyDataSetChanged();

//        mTxtPeriodNum.setText("1/" + periods.size());

//        ArrayList<String> _list = new ArrayList<>();
//        if (isUserCharged()) {
//            //用户已购买
//            for (Period period : periods) {
//                _list.add(period.getRecordingUrl());
//            }
//
//        } else {
//            //未购买
//            for (Period period : periods) {
//                if (period.getIsTry() == 1) {
//                    _list.add(period.getRecordingUrl());
//                }
//            }
//        }
//
//
//        refreshPlayList(_list);
    }

    @Override
    public void showBottomLayout() {
        mLlOnlineLessonDetailHeader.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        mRlBottomPrice.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomLayout() {
        mLlOnlineLessonDetailHeader.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        mRlBottomPrice.setVisibility(View.GONE);
    }

    @Override
    protected void toBuyLesson() {
        if (mPresenter.getOnlineLessonDetail() != null && mPresenter.getOnlineLessonDetail().getIsTime() == 1) {

            //购买课程
            OnlineLesson onlineLesson = mPresenter.getOnlineLessonDetail();
            //跳转到 支付页面
            if (onlineLesson != null) {
                Intent intent = new Intent(this, LessonPayActivity.class);
                intent.putExtra("orderType", 1);
                intent.putExtra("lessonId", onlineLesson.getId());
                intent.putExtra("title", onlineLesson.getName());
                //TODO
//            intent.putExtra("time", String.format("精品录播课（%s节）", mPeriodItems.size()));
                intent.putExtra("price", onlineLesson.getPrice());
                startActivity(intent);
            }
        } else {
            ToastUtils.showShort( "网课已到期");

        }

    }

    @Override
    protected boolean isUserCharged() {
        if (mPresenter.getOnlineLessonDetail() != null) {
            if (mPresenter.getOnlineLessonDetail().getIs_charges() == 0 || mPresenter.getOnlineLessonDetail().getIsBuy() == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void orientationHorizon() {
        mToolbar.getLayoutParams().height = Arad.app.deviceInfo.getScreenWidth();
    }

    @Override
    protected void orientationProtrait() {
        mToolbar.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.toolbar_video_height);
    }

    @OnClick({R.id.btn_to_buy, R.id.btn_server})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_to_buy) {
            toBuyLesson();
        } else if (id == R.id.btn_server) {
            startActivity(ServicesActivity.class);
        }
    }

    @OnClick(R.id.txt_online_lesson_share)
    public void onShareClicked() {
        ShareDialogUtil.showShareDialog(this, "时代教育", mTxtTitle.getText().toString(), "http://yinhangren.cn/gqwk-content.html?ID=" + lessonId, R.mipmap.ic_launcher, new ShareListener() {
            @Override
            public void shareSuccess() {

            }

            @Override
            public void shareFailure(Exception e) {
                e.printStackTrace();
                ToastUtils.showShort( e.getLocalizedMessage());
            }

            @Override
            public void shareCancel() {

            }
        });
    }

    @OnClick(R.id.txt_online_lesson_comment)
    public void onCommentBtnClick() {
        if (mViewPager.getChildCount() == 4) {
            mViewPager.setCurrentItem(3);
        }
    }

    //我添加，下载的点击事件
    @OnClick(R.id.btn_myonlinelesson_download)
    public void onDownloadBtnClick(){
        // TODO: 2018/5/11 下载的点击事件

        Intent intent = new Intent(this, DownloadListActivity.class);
        intent.putExtra("lessonId",lessonId);
        startActivity(intent);


    }

    //fragment通过 eventBus 传递过来的课时 position

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventModel.OnlineLessonChangePeriod event) {

        if (event.position >= 0) {
            changeVideo(event.position);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFire(EventModel.OnlineLessonRefreshPeriod event) {

        //把所有课时的URL以list的形式作为参数，

        refreshPlayList(event.periodUrl);
        KLog.d("url" + event.periodUrl.size());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFire(EventModel.OnlineLessonRefreshPeriodInfo event) {
        refreshPlayListUI(event.period);
    }


}