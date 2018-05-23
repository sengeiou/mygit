package com.beanu.l4_clean.util;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.HttpModel;
import com.beanu.l3_common.model.PageModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * 模拟请求数据
 * Created by Beanu on 2017/11/9.
 */

public class FakeLoader {
    public static <T> Observable<IPageModel<T>> loadList(final Class<T> tClass, final int page) {
        return Observable.create(new ObservableOnSubscribe<HttpModel<IPageModel<T>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<HttpModel<IPageModel<T>>> subscriber) throws Exception {
                HttpModel<IPageModel<T>> baseModel = new HttpModel<>();
                baseModel.succeed = "000";
                PageModel<T> pageModel = new PageModel<>();
                pageModel.pageNumber = page;
                pageModel.totalPage = 10;
                pageModel.dataList = new ArrayList<>();
                for (int i = 0; i < 10; ++i) {
                    T standard = tClass.newInstance();
//                    if (Message.class.isAssignableFrom(tClass)) {
//                        ((Message) standard).setIcon(R.drawable.message_tongzhi);
//                        ((Message) standard).setTitle("通知");
//                        ((Message) standard).setDescribe("这是一些重要的信息");
//                    }

                    pageModel.dataList.add(standard);
                }
                baseModel.dataInfo = pageModel;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    baseModel.succeed = "111";
                    baseModel.sucInfo = "超时";
                }

//                if (Math.random() > 0.9) {
//                    subscriber.onError(new Throwable());
//                } else {
                subscriber.onNext(baseModel);
//                }

                subscriber.onComplete();
            }
        }).compose(RxHelper.<IPageModel<T>>handleResult());
    }
}
