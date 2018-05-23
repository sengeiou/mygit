package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.mvp.contract.ExamContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/12
 */

public class ExamModelImpl implements ExamContract.Model {

    @Override
    public Observable<Integer> collect_question(String questionId, int type, int questionType, String subjectId, String courseId) {
        return API.getInstance(ApiService.class).collect_question(API.createHeader(), questionId, type, questionType, subjectId, courseId)
                .compose(RxHelper.<Integer>handleResult());
    }

    @Override
    public Observable<String> deleteWrongQuestion(String ids) {
        return API.getInstance(ApiService.class).tools_delete_wrong_question(API.createHeader(), ids)
                .compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> postExamResult(String resultJson) {
        return API.getInstance(ApiService.class)
                .postExamResult(API.createHeader(), resultJson)
                .compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> random_exercises_list(String subjectId, String courseId) {

        return API.getInstance(ApiService.class)
                .random_exercises_list(API.createHeader(), subjectId, courseId)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> random_ai_list(String subjectId) {
        return API.getInstance(ApiService.class)
                .random_ai_list(API.createHeader(), subjectId)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> exam_analysis_list(String examId, int type, int source) {
        return API.getInstance(ApiService.class)
                .exam_analysis_list(API.createHeader(), examId, type, source)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> test_paper_questions(String pagerId) {
        return API.getInstance(ApiService.class).test_paper_questions(API.createHeader(), pagerId)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }

    @Override
    public Observable<String> postTestPaperResult(String resultJson) {
        return API.getInstance(ApiService.class).post_test_paper_result(API.createHeader(), resultJson)
                .compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> test_paper_analysis_list(String examId, int source) {
        return API.getInstance(ApiService.class).test_paper_analysis_list(API.createHeader(), examId, source)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> tools_wrong_question(String subjectId, String courseId) {
        return API.getInstance(ApiService.class).tools_wrong_question_list(API.createHeader(), subjectId, courseId)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> tools_collect_question(String subjectId, String courseId) {
        return API.getInstance(ApiService.class).tools_collect_question_list(API.createHeader(), subjectId, courseId)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }

    @Override
    public Observable<List<ExamQuestion>> tools_note_question(String subjectId, String courseId) {
        return API.getInstance(ApiService.class).tools_note_question_list(API.createHeader(), subjectId, courseId)
                .compose(RxHelper.<List<ExamQuestion>>handleResult());
    }
}