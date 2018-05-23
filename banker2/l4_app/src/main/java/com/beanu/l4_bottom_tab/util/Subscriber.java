package com.beanu.l4_bottom_tab.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by jrl on 2018/3/8.
 */

public abstract class Subscriber<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {
        onCompleted();
    }

    public void onCompleted() {

    }

}
