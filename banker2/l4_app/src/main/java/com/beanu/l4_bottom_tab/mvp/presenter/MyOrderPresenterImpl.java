package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.mvp.contract.MyOrderContract;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/04/07
 */

public class MyOrderPresenterImpl extends MyOrderContract.Presenter {

    @Override
    public void deleteOrder(final int position, String orderId) {
        mModel.deleteOrder(orderId).subscribe(new Observer<String>() {
            @Override
            public void onComplete() {
                mView.deleteOrderSuccess(position);
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
            public void onNext(String s) {

            }
        });
    }
}