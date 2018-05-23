package com.beanu.l4_bottom_tab.ui.module2_liveLesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.LiveLessonFragmentAdapter;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.ui.common.LessonPayActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.ServicesActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.live_lesson.MyTimeTableActivity;
import com.beanu.l4_bottom_tab.util.Subscriber;
import com.beanu.l4_bottom_tab.util.TimeUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 直播课详情界面
 */
@Route(path = "/app/liveLesson/detail")
public class LiveLessonDetailActivity extends BaseSDActivity {

    @BindView(R.id.txt_live_lesson_detail_title) TextView mTxtTitle;
    @BindView(R.id.txt_live_lesson_detail_date) TextView mTxtDate;
    @BindView(R.id.txt_live_lesson_detail_days) TextView mTxtDays;
    @BindView(R.id.tab_layout_live_lesson_detail) TabLayout mTabLayout;
    @BindView(R.id.viewPager_live_lesson_detail) ViewPager mViewPager;
    @BindView(R.id.txt_live_lesson_detail_price) TextView mTxtPrice;
    @BindView(R.id.txt_live_lesson_detail_buyer) TextView mTxtBuyer;
    @BindView(R.id.btn_to_buy) Button mBtnToBuy;


    String mLiveLessonId;
    LiveLesson mLiveLesson;
    boolean stopSale = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_lesson_detail);
        ButterKnife.bind(this);

        mLiveLessonId = getIntent().getStringExtra("lessonId");

        //请求数据
        API.getInstance(ApiService.class).live_lesson_detail(API.createHeader(), mLiveLessonId)
                .compose(RxHelper.<LiveLesson>handleResult())
                .subscribe(new Subscriber<LiveLesson>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LiveLesson lesson) {
                        mLiveLesson = lesson;
                        refreshUI(lesson);
                    }
                });

    }

    private void refreshUI(LiveLesson lesson) {
        //更新UI
        if (lesson != null) {
            mTxtTitle.setText(lesson.getName());

            if (lesson.getStart_time() != null && lesson.getStart_time().length() >= 10) {
                mTxtDate.setText(lesson.getStart_time().replaceAll("-", ".").substring(0, 10));
            }
            if (lesson.getEnd_time() != null && lesson.getStart_time().length() >= 10) {
                mTxtDate.append(" - " + lesson.getEnd_time().replaceAll("-", ".").substring(0, 10));
            }

//            mTxtDate.setText(lesson.getStart_time().replaceAll("-", ".") + " - " + lesson.getEnd_time().replaceAll("-", "."));
            if (!TextUtils.isEmpty(lesson.getStop_sale())) {

                String stopTime = TimeUtils.countDownDays(lesson.getStop_sale());
                if (!TextUtils.isEmpty(stopTime)) {
                    stopSale = false;
                    mTxtDays.setText(String.format("距离停售%s", stopTime));
                } else {
                    stopSale = true;
                    mTxtDays.setText(String.format("已停售", stopTime));
                }
            } else {
                stopSale = false;
                mTxtDays.setText("");
            }

            if (lesson.getPrice() == 0) {
                mTxtPrice.setText("免费");
                mTxtPrice.setTextColor(getResources().getColor(R.color.color_green));
                mTxtBuyer.setText(String.format("已有%s人领取", lesson.getSale_num()));
            } else {
//                mTxtPrice.setText("¥" + lesson.getPrice());
                mTxtPrice.setText(Html.fromHtml("<small>¥</small><font>" + lesson.getPrice() + "</font>"));
                mTxtPrice.setTextColor(getResources().getColor(R.color.base_font_red));
                mTxtBuyer.setText(String.format("已有%s人购买", lesson.getSale_num()));
            }
            LiveLessonFragmentAdapter mFragmentAdapter = new LiveLessonFragmentAdapter(getSupportFragmentManager(), lesson);
            mViewPager.setAdapter(mFragmentAdapter);
            mViewPager.setOffscreenPageLimit(4);
            mTabLayout.setupWithViewPager(mViewPager);

            if (lesson.getIsBuy() == 0) {
                //未购买
                if (lesson.getPrice() == 0) {
                    mBtnToBuy.setEnabled(true);
                    mBtnToBuy.setText("立即领取");
                    mBtnToBuy.setBackgroundColor(getResources().getColor(R.color.color_green));

                } else {
                    mBtnToBuy.setEnabled(true);
                    mBtnToBuy.setText("立即购买");
                    mBtnToBuy.setBackgroundColor(getResources().getColor(R.color.base_color_orange));
                }

                if (stopSale) {
                    mBtnToBuy.setText("已停售");
                    mBtnToBuy.setEnabled(false);
                    mBtnToBuy.setBackgroundColor(getResources().getColor(R.color.base_font_gray));
                }

            } else {
                //已购买
                mBtnToBuy.setText("进入课程");
                mBtnToBuy.setEnabled(true);
                if (lesson.getPrice() == 0) {
                    mBtnToBuy.setBackgroundColor(getResources().getColor(R.color.color_green));
                } else {
                    mBtnToBuy.setBackgroundColor(getResources().getColor(R.color.base_color_orange));
                }
            }
        }
    }

    @OnClick({R.id.btn_to_buy, R.id.btn_server})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_to_buy) {
            //跳转到 支付页面
            if (mLiveLesson != null && mLiveLesson.getIsBuy() == 0) {

                //价格为0 直接领取
                if (mLiveLesson.getPrice() == 0) {
                    showProgress();
                    API.getInstance(ApiService.class).createLessonOrder(API.createHeader(), 0, mLiveLessonId, null, null)
                            .compose(RxHelper.<Map<String, String>>handleResult())
                            .subscribe(new Subscriber<Map<String, String>>() {
                                @Override
                                public void onCompleted() {
                                    hideProgress();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    hideProgress();
                                }

                                @Override
                                public void onNext(Map<String, String> map) {
                                    mLiveLesson.setIsBuy(1);
                                    mBtnToBuy.setText("进入课程");

                                    ToastUtils.showShort( "领取成功");
                                }
                            });
                } else {
                    Intent intent = new Intent(this, LessonPayActivity.class);
                    intent.putExtra("orderType", 0);
                    intent.putExtra("lessonId", mLiveLessonId);
                    intent.putExtra("title", mLiveLesson.getName());
                    intent.putExtra("time", mLiveLesson.getStart_time().replaceAll("-", ".") + " - " + mLiveLesson.getEnd_time().replaceAll("-", "."));
                    intent.putExtra("price", mLiveLesson.getPrice());
                    startActivity(intent);
                }


            } else {
                startActivity(MyTimeTableActivity.class);
            }
        } else if (id == R.id.btn_server) {
            startActivity(ServicesActivity.class);
        }

    }

    @Override
    public String setupToolBarTitle() {
        return "课程详情";
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
}