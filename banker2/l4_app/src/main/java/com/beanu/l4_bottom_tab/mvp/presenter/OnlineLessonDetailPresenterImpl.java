package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.Period;
import com.beanu.l4_bottom_tab.mvp.contract.OnlineLessonDetailContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 高清网课 详情
 * Created by Beanu on 2017/04/05
 */

public class OnlineLessonDetailPresenterImpl extends OnlineLessonDetailContract.Presenter {

    private List<Period> mPeriodList = new ArrayList<>();
    private OnlineLesson mOnlineLessonDetail;



    @Override
    public void requestHttpData(final String lessonId) {
        //详情
        mView.showProgress();
        mModel.online_lesson_detail(lessonId)
                .subscribe(new Observer<OnlineLesson>() {
                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                        mView.refreshView(mOnlineLessonDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideProgress();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(OnlineLesson lesson) {
                        mOnlineLessonDetail = lesson;


                        //课时列表
//                mRxManage.add(mModel.online_lesson_period(lessonId).subscribe(new Subscriber<List<Period>>() {
//                    @Override
//                    public void onCompleted() {
//                        mView.refreshPeriod(mPeriodList);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<Period> periodPageModel) {
//                        mPeriodList.clear();
//                        mPeriodList.addAll(periodPageModel);
//                    }
//                }));


                    }
                });


    }

    public OnlineLesson getOnlineLessonDetail() {
        return mOnlineLessonDetail;
    }
}