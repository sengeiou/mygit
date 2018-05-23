package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.mvp.contract.MyOnlineLessonContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/06/02
 */

public class MyOnlineLessonPresenterImpl extends MyOnlineLessonContract.Presenter {

    @Override
    public void myOnlineLesson() {
        mModel.myOnlineLesson().subscribe(new Observer<List<OnlineLesson>>() {
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
            public void onNext(List<OnlineLesson> onlineLessons) {
                mView.refreshUI(onlineLessons);
            }
        });
    }

}