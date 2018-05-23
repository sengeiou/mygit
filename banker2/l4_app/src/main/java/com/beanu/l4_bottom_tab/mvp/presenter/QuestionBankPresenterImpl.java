package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.Course;
import com.beanu.l4_bottom_tab.mvp.contract.QuestionBankContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/03/23
 */

public class QuestionBankPresenterImpl extends QuestionBankContract.Presenter {

    private List<BannerItem> mBannerList = new ArrayList<>();
    private List<Course> mCourseList = new ArrayList<>();



    @Override
    public void requestHttpData(int position, String subjectId) {
        mView.showProgress();

        //获取banner
        mModel.banner_list(position)
                .subscribe(new Observer<List<BannerItem>>() {
                    @Override
                    public void onComplete() {
                        mView.refreshHeaderBanner(mBannerList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<BannerItem> list) {
                        mBannerList.clear();
                        mBannerList.addAll(list);

                    }
                });

        //获取科目列表
        mModel.course_list(subjectId)
                .subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onComplete() {
                        mView.refreshCourseList(mCourseList);
                        mView.hideProgress();

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();
                        e.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<Course> list) {
                        mCourseList.clear();
                        mCourseList.addAll(list);
                    }
                });

    }


    public List<BannerItem> getBannerList() {
        return mBannerList;
    }

    public List<Course> getCourseList() {
        return mCourseList;
    }
}