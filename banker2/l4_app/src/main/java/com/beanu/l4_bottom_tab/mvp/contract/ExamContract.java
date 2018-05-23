package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;

import java.util.List;

import io.reactivex.Observable;

/**
 * 答题页面
 * Created by Beanu on 2017/4/12.
 */

public interface ExamContract {

    public interface View extends BaseView {
        public void refreshUIList(List<ExamQuestion> examQuestionList);

        public void postExamResultSuccess();

        public void collectQuestion(boolean fill);

        public void collectQuestionFailed();

        public void deleteWrongQuestion(boolean single);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        //=================标题栏功能===================
        //收藏题目
        public abstract void collectQuestion(String questionId, int type, int questionType, String subjectId, String courseId);

        public abstract void deleteWrongQuestion(String ids, boolean single);

        //=================专题练习 & 智能练习 及错题分析===================
        //练习题
        public abstract void requestRandomExercisesList(String subjectId, String courseId);

        //智能练习
        public abstract void requestRandomAIList(String subjectId);

        //上面两个练习  提交答案
        public abstract void postExamResult();

        //上面两个练习的错误分析
        public abstract void requestExamAnalysisList(String examId, int type, int source);


        //=================真题练习 及错题分析===================
        //获取真题试卷的题目
        public abstract void requestTestPaperQuestions(String pagerId);

        //提交真题答案
        public abstract void postTestPaperResult();

        //获取试卷的错题分析
        public abstract void requestTestPaperAnalysisList(String id, int source);

        //=================错题本 练习===================
        public abstract void requestWrongQuestionList(String subjectId, String courseId);

        //=================收藏的题目 分析===================
        public abstract void requestCollectQuestionList(String subjectId, String courseId);

        //=================笔记题目 分析===================
        public abstract void requestNoteQuestionList(String subjectId, String courseId);

    }

    public interface Model extends BaseModel {

        Observable<Integer> collect_question(String questionId, int type, int questionType, String subjectId, String courseId);

        Observable<String> deleteWrongQuestion(String ids);

        Observable<String> postExamResult(String resultJson);

        Observable<List<ExamQuestion>> random_exercises_list(String subjectId, String courseId);

        Observable<List<ExamQuestion>> random_ai_list(String subjectId);

        Observable<List<ExamQuestion>> exam_analysis_list(String examId, int type, int source);

        Observable<List<ExamQuestion>> test_paper_questions(String pagerId);

        Observable<String> postTestPaperResult(String resultJson);

        Observable<List<ExamQuestion>> test_paper_analysis_list(String examId, int source);

        Observable<List<ExamQuestion>> tools_wrong_question(String subjectId, String courseId);

        Observable<List<ExamQuestion>> tools_collect_question(String subjectId, String courseId);

        Observable<List<ExamQuestion>> tools_note_question(String subjectId, String courseId);

    }


}