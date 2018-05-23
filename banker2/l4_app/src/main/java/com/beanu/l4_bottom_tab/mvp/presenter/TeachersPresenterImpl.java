package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.mvp.contract.TeachersContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/04/06
 */

public class TeachersPresenterImpl extends TeachersContract.Presenter {

    @Override
    public void requestHttpData(String lessonId, int type) {
        switch (type) {
            case 0://直播课
                mModel.live_lesson_teacher_list(lessonId).subscribe(new Observer<List<TeacherIntro>>() {
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
                    public void onNext(List<TeacherIntro> list) {
                        mView.refreshListView(list);
                    }
                });
                break;
            case 1://高清网课
                mModel.online_lesson_teacher_list(lessonId).subscribe(new Observer<List<TeacherIntro>>() {
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
                    public void onNext(List<TeacherIntro> list) {
                        mView.refreshListView(list);
                    }
                });
                break;
        }

    }
}