package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.support.viewpager.tricks.ViewPagerUtils;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordDetailJson;
import com.beanu.l4_bottom_tab.model.bean.ExamOption;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.model.bean.QuestionEntry;
import com.beanu.l4_bottom_tab.mvp.contract.ExamContract;
import com.beanu.l4_bottom_tab.mvp.model.ExamModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.ExamPresenterImpl;
import com.beanu.l4_bottom_tab.support.dialog.AlertDialogCustom;
import com.beanu.l4_bottom_tab.support.dialog.AlertDialogRest;
import com.beanu.l4_bottom_tab.support.draw.DrawFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 答题页面
 */
public class ExamActivity extends BaseSDActivity<ExamPresenterImpl, ExamModelImpl> implements ExamContract.View, IExamResponseListener {

    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.txt_timer) TextView mTxtTimer;
    @BindView(R.id.txt_collect) TextView mTxtCollect;

    public static final String TITLE = "title"; //标题
    public static final String EXAM_TYPE = "type"; //0 练习 1智能组卷 2真题
    public static final String EXAM_COURSE_ID = "courseId"; //科目ID
    public static final String EXAM_PAGER_ID = "paperId"; //试卷ID

    ViewPagerAdapter mViewPagerAdapter;
    private String mTitle;
    private int type;//0练习题 1智能练习 2真题
    private String courseId;
    private String subjectId;
    private ExamQuestion currentQuestion;//当前选中的题目

    //计时器
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mPresenter.secondsUp();

            int minute = mPresenter.getSeconds() / 60;
            int second = mPresenter.getSeconds() % 60;

            mTxtTimer.setText((minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second);
            handler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);

        mTitle = getIntent().getStringExtra(TITLE);
        type = getIntent().getIntExtra(EXAM_TYPE, 0);

        courseId = getIntent().getStringExtra(EXAM_COURSE_ID);
        subjectId = AppHolder.getInstance().user.getSubjectId();
        String pagerId = getIntent().getStringExtra(EXAM_PAGER_ID);//试题ID

        switch (type) {
            case 0://练习
                mPresenter.requestRandomExercisesList(subjectId, courseId);
                break;
            case 1://智能组卷
                mPresenter.requestRandomAIList(subjectId);
                break;
            case 2://真题
                mPresenter.requestTestPaperQuestions(pagerId);
                break;
        }

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mPresenter.getExamQuestionList());
        mViewPager.setAdapter(mViewPagerAdapter);
        ViewPagerUtils.setSliderTransformDuration(mViewPager, 300, new DecelerateInterpolator());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mPresenter.getExamQuestionList().size()) {
                    mViewPagerAdapter.notifyDataSetChanged();
                } else {
                    //更新收藏状态
                    collectQuestion(mPresenter.getExamQuestionList().get(position).getCollection() == 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //关掉 右划 返回
        disableSlideBack();

        Arad.bus.register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mTxtTimer.isSelected())
            timerFire(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
        timerFire(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    public void refreshUIList(List<ExamQuestion> examQuestionList) {
//        mViewPagerAdapter.notifyDataSetChanged();
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mPresenter.getExamQuestionList());
        mViewPager.setAdapter(mViewPagerAdapter);
        if (examQuestionList.size() > 0) {
            collectQuestion(examQuestionList.get(0).getCollection() == 1);
        }
    }

    @Override
    public void postExamResultSuccess() {
        finish();
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
        //do nothing
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExamPreviewEvent(EventModel.ExamPreviewQuesionSelectEvent event) {

        if (mViewPager != null) {
            mViewPager.setCurrentItem(event.position, false);
        }
    }

    @Override
    public void onRadio(String selectedId, int whichOne) {
        KLog.d(selectedId + ":我是第" + whichOne + "个题");
        ExamQuestion question = mPresenter.getExamQuestionList().get(whichOne);
        boolean isCorrect = false;
        for (ExamOption option : question.getDataList()) {
            if (option.getIs_correct() == 1) {
                if (option.getId().equals(selectedId)) {
                    isCorrect = true;
                    break;
                }
            }
        }

        mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setAnswer(selectedId);
        mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setIsRealy(isCorrect ? 1 : 0);

        gotoNextPage(whichOne);
    }

    @Override
    public void onMutil(List<String> selectedID, int whichOne) {

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

            ExamQuestion question = mPresenter.getExamQuestionList().get(whichOne);
            for (ExamOption option : question.getDataList()) {
                if (option.getIs_correct() == 0) {
                    if (_ids.contains(option.getId())) {
                        isCorrect = false;
                        break;
                    }
                }
            }
            mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setAnswer(_ids);
            mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setIsRealy(isCorrect ? 1 : 0);
        } else {
            mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setAnswer(null);
            mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setIsRealy(2);
        }
    }

    @Override
    public void onJudge(String selectedId, int whichOne) {
        boolean isCorrect = false;
        ExamQuestion question = mPresenter.getExamQuestionList().get(whichOne);

        for (ExamOption option : question.getDataList()) {
            if (option.getIs_correct() == 1) {
                if (option.getId().equals(selectedId)) {
                    isCorrect = true;
                    break;
                }
            }
        }

        mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setAnswer(selectedId);
        mPresenter.getAnswerRecordJson().getArdj().get(whichOne).setIsRealy(isCorrect ? 1 : 0);

        gotoNextPage(whichOne);
    }

    @OnClick({R.id.img_back, R.id.txt_note, R.id.txt_answer_sheet, R.id.txt_collect, R.id.txt_timer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                showExitDialog();
                break;
            case R.id.txt_note:
                showDrawFragment();
                break;
            case R.id.txt_answer_sheet:
                if (mViewPagerAdapter.getCount() > 0) {
                    mViewPager.setCurrentItem(mViewPagerAdapter.getCount() - 1);
                }
                break;
            case R.id.txt_collect:
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
                        mPresenter.collectQuestion(currentQuestion.getId(), type, currentQuestion.getType(), subjectId, currentQuestion.getCourseId());
                    }
                }
                break;
            case R.id.txt_timer:
                if (mTxtTimer.isSelected()) {
                    timerFire(true);
                    mTxtTimer.setSelected(false);

                } else {
                    timerFire(false);
                    mTxtTimer.setSelected(true);
                    showRestDialog();
                }
                break;
        }
    }


    /**
     * 显示画图面板
     */
    private void showDrawFragment() {

        int position = mViewPager.getCurrentItem();
        if (mPresenter.getExamQuestionList().size() > position) {
            ExamQuestion question = mPresenter.getExamQuestionList().get(position);

            DrawFragment mDrawFragment = DrawFragment.newInstance(question.getId());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("draw");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_draw, mDrawFragment, "draw")
                    .addToBackStack(null)
                    .commit();
        }

    }

    /**
     * 执行下一个页面
     *
     * @param currentPosition 当前页
     */
    private void gotoNextPage(int currentPosition) {
        int nextPage = currentPosition + 1;
        if (nextPage < mPresenter.getExamQuestionList().size() + 1) {
            mViewPager.setCurrentItem(nextPage, true);
        }
        if (nextPage == mPresenter.getExamQuestionList().size()) {
            mViewPagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 是否停止计时
     *
     * @param begin 是否
     */
    private void timerFire(boolean begin) {
        if (begin) {
            handler.postDelayed(runnable, 1000);
        } else {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 显示休息dialog
     */
    private void showRestDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();

        int totalCount = mPresenter.getExamQuestionList().size();

        int answerRight = 0;
        if (mPresenter.getAnswerRecordJson() != null && mPresenter.getAnswerRecordJson().getArdj() != null) {
            for (AnswerRecordDetailJson recordDetailJson : mPresenter.getAnswerRecordJson().getArdj()) {
                if (recordDetailJson.getIsRealy() != 2) {
                    answerRight++;
                }
            }
        }

        AlertDialogRest dialog = AlertDialogRest.newInstance(String.format("共有%d道题，还有%d道未做！", totalCount, totalCount - answerRight));
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerFire(true);
                mTxtTimer.setSelected(false);

            }
        });
        dialog.show(getSupportFragmentManager(), "dialog");

    }

    /**
     * 提交答案页面
     */
    private void showExitDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();

        AlertDialogCustom dialog = AlertDialogCustom.newInstance("确定要退出练习？退出后未完成的练习会保存在练习记录中");
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgress();
                switch (type) {
                    case 0:
                    case 1:
                        mPresenter.postExamResult();
                        break;
                    case 2:
                        mPresenter.postTestPaperResult();
                        break;
                }

            }
        });
        dialog.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    /**
     * Viewpager 内容
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<ExamQuestion> mQuestionList;

        ViewPagerAdapter(FragmentManager fm, List<ExamQuestion> questionList) {
            super(fm);
            mQuestionList = questionList;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == mQuestionList.size()) {
                return ExamPreviewFragment.newInstance(mTitle, mPresenter.getAnswerRecordJson(), mPresenter.getSeconds(), type);
            }
            ExamQuestion question = mQuestionList.get(position);

            QuestionEntry entry = new QuestionEntry();
            entry.setExamQuestion(question);
            entry.setPosition(position);
            entry.setTitle(mTitle);
            entry.setCount(mQuestionList.size());
            entry.setExam_or_analysis(0);

            switch (question.getType()) {
                case 0:
                    return ExamRadioFragment.newInstance(entry, false);
                case 1:
                    return ExamMultiSelectFragment.newInstance(entry);
                case 2:
                    return ExamJudgeFragment.newInstance(entry);
                //TODO 3
                case 4:
                case 5:
                case 6:
                    return ExamMaterialFragment.newInstance(entry);
            }

            return ExamRadioFragment.newInstance(entry, false);
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
            return (mQuestionList == null || mQuestionList.size() == 0) ? 0 : mQuestionList.size() + 1;
        }
    }
}