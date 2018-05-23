package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.beanu.arad.support.viewpager.tricks.ViewPagerUtils;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.ExamOption;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.model.bean.QuestionEntry;
import com.beanu.l4_bottom_tab.mvp.contract.ExamContract;
import com.beanu.l4_bottom_tab.mvp.model.ExamModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.ExamPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 错误分析页面
 */
public class ErrorAnalysisActivity extends BaseSDActivity<ExamPresenterImpl, ExamModelImpl> implements ExamContract.View, IExamResponseListener {

    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.txt_collect) TextView mTxtCollect;
    @BindView(R.id.txt_answer_delete) TextView mTxtAnswerDelete;


    public static final String TITLE = "title"; //标题
    public static final String ANALYSIS_TYPE = "mPaperType"; //0 练习 1智能练习 2真题 90错题本 91收藏列表 92笔记题目
    public static final String ANALYSIS_ID = "id"; //做题记录ID
    public static final String ANALYSIS_SOURCE = "source";//不传为全部解析，1错误解析

    ViewPagerAdapter mViewPagerAdapter;
    private String mTitle;
    private String subjectId;
    private int mPaperType;//0 练习 1智能组卷 2真题 90错题本 91收藏列表 92笔记题目
    private ExamQuestion currentQuestion;//当前选中的题目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_analysis);
        ButterKnife.bind(this);

        mTitle = getIntent().getStringExtra(TITLE);
        mPaperType = getIntent().getIntExtra(ANALYSIS_TYPE, 0);

        String id = getIntent().getStringExtra(ANALYSIS_ID);
        int source = getIntent().getIntExtra(ANALYSIS_SOURCE, 0);
        subjectId = AppHolder.getInstance().user.getSubjectId();

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mPresenter.getExamQuestionList());
        mViewPager.setAdapter(mViewPagerAdapter);
        ViewPagerUtils.setSliderTransformDuration(mViewPager, 200, new DecelerateInterpolator());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //更新收藏状态
                collectQuestion(mPresenter.getExamQuestionList().get(position).getCollection() == 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        switch (mPaperType) {
            case 0:
            case 1:
                mPresenter.requestExamAnalysisList(id, mPaperType, source);
                mTxtAnswerDelete.setVisibility(View.GONE);
                break;
            case 2:
                mPresenter.requestTestPaperAnalysisList(id, source);
                mTxtAnswerDelete.setVisibility(View.GONE);

                break;
            case 90:
                mPresenter.requestWrongQuestionList(subjectId, id);
                break;
            case 91:
                mPresenter.requestCollectQuestionList(subjectId, id);
                mTxtAnswerDelete.setVisibility(View.GONE);

                break;
            case 92:
                mPresenter.requestNoteQuestionList(subjectId, id);
                mTxtAnswerDelete.setVisibility(View.GONE);

                break;
        }

        //关掉 右划 返回
        disableSlideBack();
    }

    @Override
    public void refreshUIList(List<ExamQuestion> examQuestionList) {
//        mViewPagerAdapter.notifyDataSetChanged();
        mViewPagerAdapter = new ErrorAnalysisActivity.ViewPagerAdapter(getSupportFragmentManager(), mPresenter.getExamQuestionList());
        mViewPager.setAdapter(mViewPagerAdapter);
        if (examQuestionList.size() > 0) {
            collectQuestion(examQuestionList.get(0).getCollection() == 1);
        }
    }

    @Override
    public void postExamResultSuccess() {
        // do nothing
    }

    @Override
    public void collectQuestion(boolean fill) {
        if (fill) {
            mTxtCollect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.note_collected, 0, 0, 0);
        } else {
            mTxtCollect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.note_collect, 0, 0, 0);
        }
    }

    @Override
    public void collectQuestionFailed() {
        if (currentQuestion != null) {
            if (currentQuestion.getCollection() == 0) {
                currentQuestion.setCollection(1);
            } else {
                currentQuestion.setCollection(0);
            }

            //如果收藏失败 再次更新收藏状态
            collectQuestion(currentQuestion.getCollection() == 1);
        }
    }

    @Override
    public void deleteWrongQuestion(boolean single) {
        //删除错题成功
        if (single) {
            ToastUtils.showShort("已标记为删除状态");
        }
    }


    @Override
    public void onRadio(String selectedId, int whichOne) {
        // 错题本模式
        Fragment fragment = mViewPagerAdapter.getCurrentFragment();
        if (fragment instanceof ExamRadioFragment) {
            //更新本地数据
            ExamQuestion mq = mPresenter.getExamQuestionList().get(whichOne);
            boolean isCorrect = false;
            for (ExamOption option : mq.getDataList()) {
                if (option.getIs_correct() == 1) {
                    if (option.getId().equals(selectedId)) {
                        isCorrect = true;
                        break;
                    }
                }
            }
            mq.setAnswer(selectedId);
            mq.setIsRealy(isCorrect ? 1 : 0);

            if (isCorrect) {
                mPresenter.deleteWrongQuestion(mq.getWid(), false);
            }

            //更新内存数据
            ExamRadioFragment radioFragment = ((ExamRadioFragment) fragment);
            ExamQuestion question = radioFragment.getQuestionEntry().getExamQuestion();
            question.setAnswer(selectedId);
            question.setIsRealy(isCorrect ? 1 : 0);

            radioFragment.getQuestionEntry().setExam_or_analysis(1);
            radioFragment.refreshView();
        }

    }

    @Override
    public void onMutil(List<String> selectedID, int whichOne) {
        // 错题本模式
        Fragment fragment = mViewPagerAdapter.getCurrentFragment();
        if (fragment instanceof ExamMultiSelectFragment) {

            ExamMultiSelectFragment multiSelectFragment = ((ExamMultiSelectFragment) fragment);
            ExamQuestion question = multiSelectFragment.getQuestionEntry().getExamQuestion();

            //更新本地数据
            if (selectedID.size() > 0) {
                boolean isCorrect = true;
                String _ids = "";
                for (int i = 0; i < selectedID.size(); i++) {
                    if (i != selectedID.size() - 1) {
                        _ids += selectedID.get(i) + ",";
                    } else {
                        _ids += selectedID.get(i);
                    }
                }

                ExamQuestion mq = mPresenter.getExamQuestionList().get(whichOne);
                for (ExamOption option : mq.getDataList()) {
                    if (option.getIs_correct() == 1) {
                        if (!_ids.contains(option.getId())) {
                            isCorrect = false;
                        }
                    }
                }
                mPresenter.getExamQuestionList().get(whichOne).setAnswer(_ids);
                mPresenter.getExamQuestionList().get(whichOne).setIsRealy(isCorrect ? 1 : 0);

                question.setAnswer(_ids);
                question.setIsRealy(isCorrect ? 1 : 0);

                if (isCorrect) {
                    mPresenter.deleteWrongQuestion(mq.getWid(), false);
                }

            } else {
                mPresenter.getExamQuestionList().get(whichOne).setAnswer(null);
                mPresenter.getExamQuestionList().get(whichOne).setIsRealy(2);

                question.setAnswer(null);
                question.setIsRealy(2);

            }

            multiSelectFragment.getQuestionEntry().setExam_or_analysis(1);
            multiSelectFragment.refreshView();
        }
    }

    @Override
    public void onJudge(String selectId, int whichOne) {
        // 错题本模式
        Fragment fragment = mViewPagerAdapter.getCurrentFragment();
        if (fragment instanceof ExamJudgeFragment) {
            //更新本地数据
            boolean isCorrect = false;
            ExamQuestion mq = mPresenter.getExamQuestionList().get(whichOne);

            for (ExamOption option : mq.getDataList()) {
                if (option.getIs_correct() == 1) {
                    if (option.getId().equals(selectId)) {
                        isCorrect = true;
                        break;
                    }
                }
            }

            mPresenter.getExamQuestionList().get(whichOne).setAnswer(selectId);
            mPresenter.getExamQuestionList().get(whichOne).setIsRealy(isCorrect ? 1 : 0);

            if (isCorrect) {
                mPresenter.deleteWrongQuestion(mq.getWid(), false);
            }

            //更新内存数据
            ExamJudgeFragment judgeFragment = ((ExamJudgeFragment) fragment);
            ExamQuestion question = judgeFragment.getQuestionEntry().getExamQuestion();
            question.setAnswer(selectId);
            question.setIsRealy(isCorrect ? 1 : 0);

            judgeFragment.getQuestionEntry().setExam_or_analysis(1);
            judgeFragment.refreshView();
        }


    }

    @OnClick({R.id.img_back, R.id.txt_answer_delete, R.id.txt_collect})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.img_back) {
            finish();

        } else if (i == R.id.txt_answer_delete) {
            int position = mViewPager.getCurrentItem();
            if (mPresenter.getExamQuestionList().size() > position) {
                currentQuestion = mPresenter.getExamQuestionList().get(position);
                if (currentQuestion != null) {
                    mPresenter.deleteWrongQuestion(currentQuestion.getWid(), true);
                }
            }


        } else if (i == R.id.txt_collect) {
            int position = mViewPager.getCurrentItem();
            if (mPresenter.getExamQuestionList().size() > position) {
                currentQuestion = mPresenter.getExamQuestionList().get(position);
                if (currentQuestion != null) {

                    if (currentQuestion.getCollection() == 0) {
                        currentQuestion.setCollection(1);
                    } else {
                        currentQuestion.setCollection(0);
                    }
                    //更新收藏状态
                    collectQuestion(currentQuestion.getCollection() == 1);
                    mPresenter.collectQuestion(currentQuestion.getId(), currentQuestion.getSource(), currentQuestion.getType(), subjectId, currentQuestion.getCourseId());
                }
            }

        }
    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<ExamQuestion> mQuestionList;
        Fragment mCurrentFragment;

        ViewPagerAdapter(FragmentManager fm, List<ExamQuestion> questionList) {
            super(fm);
            mQuestionList = questionList;
        }

        @Override
        public Fragment getItem(int position) {

            ExamQuestion question = mQuestionList.get(position);

            QuestionEntry entry = new QuestionEntry();
            entry.setExamQuestion(question);
            entry.setPosition(position);
            entry.setTitle(mTitle);
            entry.setCount(mQuestionList.size());
            if (mPaperType == 90) {//错题本
                if (TextUtils.isEmpty(question.getAnswer())) {
                    entry.setExam_or_analysis(2);
                } else {
                    entry.setExam_or_analysis(1);//错题本已答
                }
            } else {
                entry.setExam_or_analysis(1);
            }

            switch (question.getType()) {
                case 0:
                    return ExamRadioFragment.newInstance(entry, false);
                case 1:
                    return ExamMultiSelectFragment.newInstance(entry);
                case 2:
                    return ExamJudgeFragment.newInstance(entry);
                case 4:
                case 5:
                case 6:
                    return ExamMaterialFragment.newInstance(entry);
                default:
                    return ExamRadioFragment.newInstance(entry, false);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof ExamPreviewFragment) {
                return POSITION_NONE;
            }

            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return (mQuestionList == null || mQuestionList.size() == 0) ? 0 : mQuestionList.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentFragment = (Fragment) object;
            super.setPrimaryItem(container, position, object);
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
    }
}
