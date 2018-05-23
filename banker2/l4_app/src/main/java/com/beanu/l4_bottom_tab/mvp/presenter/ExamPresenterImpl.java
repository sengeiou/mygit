package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.AnswerRecordDetailJson;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordJson;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.mvp.contract.ExamContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/04/12
 */

public class ExamPresenterImpl extends ExamContract.Presenter {

    private List<ExamQuestion> mExamQuestionList = new ArrayList<>();

    AnswerRecordJson mAnswerRecordJson = new AnswerRecordJson(); //答题记录
    private int seconds;                //当前做题时间



    @Override
    public void collectQuestion(String questionId, int type, int questionType, String subjectId, String courseId) {
        mModel.collect_question(questionId, type, questionType, subjectId, courseId)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.collectQuestionFailed();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Integer s) {
                        mView.collectQuestion(s == 1);
                    }
                });
    }

    @Override
    public void deleteWrongQuestion(final String ids, final boolean single) {
        mModel.deleteWrongQuestion(ids)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        if (single) {
                            //如果是手动删除一个
                            for (ExamQuestion question : mExamQuestionList) {
                                if (question.getWid().equals(ids)) {
                                    mExamQuestionList.remove(question);
                                    break;
                                }
                            }
                        }

                        mView.deleteWrongQuestion(single);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }

    @Override
    public void requestRandomExercisesList(final String subjectId, final String courseId) {
        mModel.random_exercises_list(subjectId, courseId).subscribe(new Observer<List<ExamQuestion>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(List<ExamQuestion> examQuestionList) {
                mExamQuestionList.clear();
                mExamQuestionList.addAll(examQuestionList);

                //同时初始化答案记录
                List<AnswerRecordDetailJson> answerList = new ArrayList<>();
                for (ExamQuestion question : mExamQuestionList) {
                    AnswerRecordDetailJson answer = new AnswerRecordDetailJson();
                    answer.setQuestionId(question.getId());
                    answer.setCourseId(question.getCourseId());
                    answer.setIsRealy(2);
                    answer.setType(question.getType());
                    answerList.add(answer);
                }
                mAnswerRecordJson.setArdj(answerList);
                mAnswerRecordJson.setCourseId(courseId);
                mAnswerRecordJson.setType(0);

                mView.refreshUIList(examQuestionList);
            }
        });
    }

    @Override
    public void requestRandomAIList(String subjectId) {
        mModel.random_ai_list(subjectId)
                .subscribe(new Observer<List<ExamQuestion>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<ExamQuestion> examQuestionList) {
                        mExamQuestionList.clear();
                        mExamQuestionList.addAll(examQuestionList);

                        //同时初始化答案记录
                        List<AnswerRecordDetailJson> answerList = new ArrayList<>();
                        for (ExamQuestion question : mExamQuestionList) {
                            AnswerRecordDetailJson answer = new AnswerRecordDetailJson();
                            answer.setQuestionId(question.getId());
                            answer.setCourseId(question.getCourseId());
                            answer.setIsRealy(2);
                            answer.setType(question.getType());
                            answerList.add(answer);
                        }
                        mAnswerRecordJson.setArdj(answerList);
                        //暂时没有作用了（题目有可能都是随机的，用里面的courseId）
                        //mAnswerRecordJson.setCourseId(courseId);
                        mAnswerRecordJson.setType(1);

                        mView.refreshUIList(examQuestionList);
                    }
                });
    }

    @Override
    public void requestExamAnalysisList(String examId, int type, int source) {
        mModel.exam_analysis_list(examId, type, source)
                .subscribe(new Observer<List<ExamQuestion>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ExamQuestion> examQuestionList) {
                        mExamQuestionList.clear();
                        mExamQuestionList.addAll(examQuestionList);
                        mView.refreshUIList(examQuestionList);
                    }
                });
    }

    @Override
    public void postExamResult() {

        preprocessorJSON();
        Gson gson = new Gson();
        String resultJson = gson.toJson(mAnswerRecordJson);

        mModel.postExamResult(resultJson)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                        mView.postExamResultSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }

    @Override
    public void requestTestPaperQuestions(final String paperId) {
        mModel.test_paper_questions(paperId).subscribe(new Observer<List<ExamQuestion>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(List<ExamQuestion> examQuestionList) {
                mExamQuestionList.clear();
                mExamQuestionList.addAll(examQuestionList);

                //同时初始化答案记录
                List<AnswerRecordDetailJson> answerList = new ArrayList<>();
                for (ExamQuestion question : mExamQuestionList) {
                    AnswerRecordDetailJson answer = new AnswerRecordDetailJson();
                    answer.setQuestionId(question.getId());
                    answer.setCourseId(question.getCourseId());
                    answer.setIsRealy(2);
                    answer.setType(question.getType());
                    answer.setqNo(question.getqNo());
                    answerList.add(answer);
                }
                mAnswerRecordJson.setArdj(answerList);
                mAnswerRecordJson.setPaperId(paperId);

                mView.refreshUIList(examQuestionList);
            }
        });
    }

    @Override
    public void postTestPaperResult() {
        preprocessorJSON();
        Gson gson = new Gson();
        String resultJson = gson.toJson(mAnswerRecordJson);

        mModel.postTestPaperResult(resultJson)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                        mView.postExamResultSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }


                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }

    @Override
    public void requestTestPaperAnalysisList(String id, int source) {
        mModel.test_paper_analysis_list(id, source)
                .subscribe(new Observer<List<ExamQuestion>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<ExamQuestion> examQuestionList) {
                        mExamQuestionList.clear();
                        mExamQuestionList.addAll(examQuestionList);
                        mView.refreshUIList(examQuestionList);
                    }
                });
    }

    @Override
    public void requestWrongQuestionList(String subjectId, String courseId) {
        mModel.tools_wrong_question(subjectId, courseId)
                .subscribe(new Observer<List<ExamQuestion>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }


                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<ExamQuestion> examQuestionList) {
                        mExamQuestionList.clear();
                        mExamQuestionList.addAll(examQuestionList);
                        mView.refreshUIList(examQuestionList);
                    }
                });
    }

    @Override
    public void requestCollectQuestionList(String subjectId, String courseId) {
        mModel.tools_collect_question(subjectId, courseId)
                .subscribe(new Observer<List<ExamQuestion>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<ExamQuestion> examQuestionList) {
                        mExamQuestionList.clear();
                        mExamQuestionList.addAll(examQuestionList);
                        mView.refreshUIList(examQuestionList);
                    }
                });
    }

    @Override
    public void requestNoteQuestionList(String subjectId, String courseId) {
        mModel.tools_note_question(subjectId, courseId)
                .subscribe(new Observer<List<ExamQuestion>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<ExamQuestion> examQuestionList) {
                        mExamQuestionList.clear();
                        mExamQuestionList.addAll(examQuestionList);
                        mView.refreshUIList(examQuestionList);
                    }
                });
    }

    public List<ExamQuestion> getExamQuestionList() {
        return mExamQuestionList;
    }

    public AnswerRecordJson getAnswerRecordJson() {
        return mAnswerRecordJson;
    }

    public int getSeconds() {
        return seconds;
    }

    public void secondsUp() {
        seconds++;
    }

    //预处理结果
    private void preprocessorJSON() {
        boolean isFinish = true;
        int answerRight = 0;
        if (mAnswerRecordJson.getArdj() != null) {
            for (AnswerRecordDetailJson recordDetailJson : mAnswerRecordJson.getArdj()) {
                if (recordDetailJson.getIsRealy() == 2) {
                    isFinish = false;
                }
                if (recordDetailJson.getIsRealy() == 1) {
                    answerRight++;
                }
            }
            mAnswerRecordJson.setIsFinish(isFinish ? 1 : 0);
            mAnswerRecordJson.setAnswerTotal(mAnswerRecordJson.getArdj().size());
            mAnswerRecordJson.setAnswerRealy(answerRight);
            mAnswerRecordJson.setAnswerTime(String.valueOf(seconds));
        }

    }
}