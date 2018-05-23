package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.mvp.contract.LiveLessonListContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


/**
 * Created by Beanu on 2017/03/07
 */

public class LiveLessonListModelImpl implements LiveLessonListContract.Model {

    @Override
    public Observable<? extends IPageModel<LiveLesson>> loadData(Map<String, Object> params, int pageIndex) {

        return API.getInstance(ApiService.class).live_lesson_list(API.createHeader(), params, pageIndex, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<LiveLesson>>handleResult());
//        final int page = pageIndex;
//        return Observable.create(new Observable.OnSubscribe<IPageModel<LiveLesson>>() {
//            @Override
//            public void call(Subscriber<? super IPageModel<LiveLesson>> subscriber) {
//
//                final List<LiveLesson> list = new ArrayList<>();
//
//                for (int i = 0; i < 10; i++) {
//
//                    LiveLesson newsItem = new LiveLesson();
//                    list.add(newsItem);
//                }
//
//                PageModel<LiveLesson> pageModel = new PageModel<>();
//                pageModel.dataList = list;
//                pageModel.pageNumber = page;
//                pageModel.totalPage = 5;
//
//                subscriber.onNext(pageModel);
//                subscriber.onCompleted();
//
//            }
//        })
//                .delay(1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<BannerItem>> banner_list(int position) {
        return API.getInstance(ApiService.class).banner_list(API.createHeader(), position)
                .compose(RxHelper.<List<BannerItem>>handleResult());
    }
}