package com.beanu.l4_bottom_tab.ui.module1_exam;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.model.bean.QuestionEntry;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 材料题
 */
public class ExamMaterialFragment extends ToolBarFragment implements View.OnTouchListener {

    private static final String ARG_QUESTION = "arg_question";

    @BindView(R.id.slide_plan) LinearLayout mSlidePlan;
    @BindView(R.id.img_drag) ImageButton mImgDrag;
    @BindView(R.id.txt_exam_content) TextView mTxtContent;
    @BindView(R.id.under_area) ScrollView mUnderArea;
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.rl_root) RelativeLayout mRlRoot;

    //处理拖动的效果
    float oldX, oldY, dX, dY, distanceY;// distanceY用来解决onclick和onTouch的冲突
    int screenHeight;//去掉statusBar toolbar 之后的屏幕高度
    int mDragButtonHeight;//拖动按钮的高度

    //业务逻辑变量
    ViewPagerAdapter mPagerAdapter;
    ExamQuestion mExamQuestion;
    QuestionEntry mQuestionEntry;

    int screen_width;

    public ExamMaterialFragment() {
        // Required empty public constructor
    }

    public static ExamMaterialFragment newInstance(QuestionEntry question) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, question);

        ExamMaterialFragment fragment = new ExamMaterialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestionEntry = getArguments().getParcelable(ARG_QUESTION);

            mExamQuestion = mQuestionEntry.getExamQuestion();
        }
        screen_width = Arad.app.deviceInfo.getScreenWidth() - getResources().getDimensionPixelSize(R.dimen.fab_margin) * 2;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exam_material, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImgDrag.setOnTouchListener(this);
        mImgDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //drag plan的原始高度
                int originalHeight = getResources().getDimensionPixelSize(R.dimen.exam_material_height);

                ViewGroup.LayoutParams params = mSlidePlan.getLayoutParams();
                if (params.height < mDragButtonHeight * 3 && params.height > mDragButtonHeight) {
                    //当小于3个button的高度 大于一个button高度的时候   缩到底部
                    params.height = mDragButtonHeight;
                } else if (params.height == originalHeight) {
                    //原位置点击 缩到底部
                    params.height = mDragButtonHeight;
                } else {
                    //在底部 或 高于原始位置的时候，恢复到原始位置
                    params.height = originalHeight;
                }
                mSlidePlan.setLayoutParams(params);

                //底层的区域高度
                ViewGroup.LayoutParams params_under = mUnderArea.getLayoutParams();
                params_under.height = screenHeight - params.height + mDragButtonHeight;
                mUnderArea.setLayoutParams(params_under);

            }
        });
        mImgDrag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImgDrag.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mDragButtonHeight = mImgDrag.getHeight();
            }
        });
        mRlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRlRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                screenHeight = mRlRoot.getHeight();
                //初始化自己的高度
                ViewGroup.LayoutParams params_under = mUnderArea.getLayoutParams();
                params_under.height = screenHeight - getResources().getDimensionPixelSize(R.dimen.exam_material_height) + mDragButtonHeight;
                mUnderArea.setLayoutParams(params_under);

            }
        });


        //业务逻辑
        String questionType;
        switch (mExamQuestion.getType()) {
            case 4:
                questionType = "【主观题】";
                break;
            case 5:
                questionType = "【完形填空】";
                break;
            case 6:
                questionType = "【材料】";
                break;
            default:
                questionType = "【材料】";
                break;
        }
        RichText.from(questionType + mExamQuestion.getContent())
                .autoFix(false)
                .scaleType(ImageHolder.ScaleType.fit_xy)
                .resetSize(true)
                .size(ImageHolder.WRAP_CONTENT, ImageHolder.WRAP_CONTENT)
                .fix(new ImageFixCallback() {
                    @Override
                    public void onInit(ImageHolder holder) {

                    }

                    @Override
                    public void onLoading(ImageHolder holder) {

                    }

                    @Override
                    public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {

                    }

                    @Override
                    public void onImageReady(ImageHolder holder, int width, int height) {

                        if (width * 2 > screen_width) {
                            holder.setWidth(screen_width);
                            holder.setHeight(height * 2 * screen_width / (width * 2));
                        } else {
                            holder.setWidth(width * 2);
                            holder.setHeight(height * 2);
                        }

                    }

                    @Override
                    public void onFailure(ImageHolder holder, Exception e) {

                    }
                }).into(mTxtContent);

        //为了适应材料题的滑动方案
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), mQuestionEntry);
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldX = event.getRawX();
            oldY = event.getRawY();
            distanceY = event.getRawY();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            dX = event.getRawX() - oldX;
            dY = event.getRawY() - oldY;

            oldX = event.getRawX();
            oldY = event.getRawY();

            int _height = (int) (mSlidePlan.getHeight() - dY);

            if (_height < mDragButtonHeight) {
                _height = mDragButtonHeight;
            }

            //slide plan的区域
            ViewGroup.LayoutParams params = mSlidePlan.getLayoutParams();
            params.height = _height;
            mSlidePlan.setLayoutParams(params);

            //底层的区域高度
            ViewGroup.LayoutParams params_under = mUnderArea.getLayoutParams();
            params_under.height = screenHeight - _height + mDragButtonHeight;
            mUnderArea.setLayoutParams(params_under);


        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            distanceY = event.getRawY() - distanceY;
            if (Math.abs(distanceY) > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 材料题 选项
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        QuestionEntry mQuestionEntry;

        ViewPagerAdapter(FragmentManager fm, QuestionEntry questionEntry) {
            super(fm);
            mQuestionEntry = questionEntry;
        }

        @Override
        public Fragment getItem(int position) {


//            switch (question.getType()) {
//                case 0:
//                    return ExamRadioFragment.newInstance(entry, false);
//                case 1:
//                    return ExamMultiSelectFragment.newInstance(entry);
//                case 2:
//                    return ExamJudgeFragment.newInstance(entry);
//                case 4:
//                case 5:
//                case 6:
//                    return ExamMaterialFragment.newInstance(entry);
//            }

            //现阶段材料题只有单选，如果需要其他题型 打开上面的注释
            return ExamRadioFragment.newInstance(mQuestionEntry, true);

        }

        @Override
        public int getCount() {
//            return mQuestionList == null ? 0 : mQuestionList.size();
            return 1;
        }
    }
}