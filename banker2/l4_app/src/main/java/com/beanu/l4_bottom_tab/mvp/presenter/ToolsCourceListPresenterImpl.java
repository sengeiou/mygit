package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.Course;
import com.beanu.l4_bottom_tab.mvp.contract.ToolsCourceListContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/04/24
 */

public class ToolsCourceListPresenterImpl extends ToolsCourceListContract.Presenter {

    private List<Course> mCourseList = new ArrayList<>();



    @Override
    public void requestCourseList(int type, final String parentId) {

//        mView.contentLoading();
        switch (type) {
            case 0:
                //错题本
                mModel.wrong_course_list(parentId).subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onComplete() {
                        mView.contentLoadingComplete();
                        if ("0".equals(parentId)) {
                            mView.refreshCourseList(mCourseList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.contentLoadingError();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<Course> list) {
                        if ("0".equals(parentId)) {
                            mCourseList.clear();
                            mCourseList.addAll(list);
                        } else {
                            mView.refreshNextChild(list);

                        }
                    }
                });
                break;
            case 1:
                //收藏列表

                mModel.collect_course_list(parentId).subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onComplete() {

                        mView.contentLoadingComplete();
                        if ("0".equals(parentId)) {
                            mView.refreshCourseList(mCourseList);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.contentLoadingError();
                    }

                    @Override
                    public void onNext(List<Course> list) {
                        if ("0".equals(parentId)) {
                            mCourseList.clear();
                            mCourseList.addAll(list);
                        } else {
                            mView.refreshNextChild(list);

                        }
                    }
                });
                break;
            case 2:
                mModel.note_course_list(parentId).subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onComplete() {

                        mView.contentLoadingComplete();
                        if ("0".equals(parentId)) {
                            mView.refreshCourseList(mCourseList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.contentLoadingError();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<Course> list) {
                        if ("0".equals(parentId)) {
                            mCourseList.clear();
                            mCourseList.addAll(list);
                        } else {
                            mView.refreshNextChild(list);
                        }
                    }
                });
                break;
        }

    }

    public List<Course> getCourseList() {
        return mCourseList;
    }
}