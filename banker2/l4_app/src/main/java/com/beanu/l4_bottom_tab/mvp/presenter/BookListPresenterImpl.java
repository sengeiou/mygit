package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.Subject;
import com.beanu.l4_bottom_tab.mvp.contract.BookListContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/03/09
 */

public class BookListPresenterImpl extends BookListContract.Presenter {

    @Override
    public void requestSubjectList() {
        mModel.requestSubjectList().subscribe(new Observer<List<Subject>>() {

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(List<Subject> subjectList) {
                mView.refreshSubjectList(subjectList);
            }
        });
    }

    @Override
    public void requestCartNum() {
        mModel.requestCartNum().subscribe(new Observer<Integer>() {


            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Integer integer) {
                mView.refreshCartNum(integer);
            }
        });
    }
}