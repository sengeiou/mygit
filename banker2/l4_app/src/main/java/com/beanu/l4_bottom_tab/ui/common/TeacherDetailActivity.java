package com.beanu.l4_bottom_tab.ui.common;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.util.Subscriber;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 老师详情介绍
 */
public class TeacherDetailActivity extends BaseSDActivity {

    @BindView(R.id.img_avatar) ImageView mImgAvatar;
    @BindView(R.id.txt_teacher_name) TextView mTxtTeacherName;
    @BindView(R.id.ratingBar) RatingBar mRatingBar;
    @BindView(R.id.txt_duration) TextView mTxtDuration;
    @BindView(R.id.txt_intro) TextView mTxtIntro;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;

    TeacherDetailAdapter mAdapter;
    String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        ButterKnife.bind(this);

        teacherId = getIntent().getStringExtra("id");

        mAdapter = new TeacherDetailAdapter(getSupportFragmentManager(), teacherId);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //获取老师信息
        API.getInstance(ApiService.class).teacher_detail(API.createHeader(), teacherId)
                .compose(RxHelper.<TeacherIntro>handleResult())
                .subscribe(new Subscriber<TeacherIntro>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(TeacherIntro teacherIntro) {
                        refreshUI(teacherIntro);
                    }
                });
    }


    private void refreshUI(TeacherIntro teacher) {
        if (teacher != null) {
            if (!TextUtils.isEmpty(teacher.getHead_portrait())) {
                Glide.with(this).load(teacher.getHead_portrait()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
            }
            mTxtTeacherName.setText(teacher.getName());
            mRatingBar.setRating(teacher.getStar_rating());
            mTxtDuration.setText(String.format("共授课%s小时", teacher.getTeaching_time()));
            if(!TextUtils.isEmpty(teacher.getIntro())){

                RichText.fromHtml(teacher.getIntro()).into(mTxtIntro);
            }

            mAdapter.setCommentCount(teacher.getEvaluate_num());
            mAdapter.setCommentRate(teacher.getStar_rating());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "老师介绍";
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

    //适配器
    private class TeacherDetailAdapter extends FragmentStatePagerAdapter {

        String[] mTitleList;
        String teacherId;

        int commentCount;
        int commentRate;

        TeacherDetailAdapter(FragmentManager fm, String teacherId) {
            super(fm);
            mTitleList = new String[]{"在售课程", "学生评价"};
            this.teacherId = teacherId;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 1) {
                return CommentFragment.newInstance(teacherId, 3, commentCount, commentRate);
            }

            return TeacherDetailLessonListFragment.newInstance(teacherId);
        }

        @Override
        public int getCount() {
            return mTitleList.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList[position];
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public void setCommentRate(int commentRate) {
            this.commentRate = commentRate;
        }
    }
}