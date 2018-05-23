package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.NewsItem;
import com.beanu.l4_bottom_tab.mvp.contract.NewsListContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/03/07
 */

public class NewsListModelImpl implements NewsListContract.Model {

    @Override
    public Observable<? extends IPageModel<NewsItem>> loadData(Map<String, Object> params, int pageIndex) {
        String id = null;
        if (params.get("id") != null) {
            id = (String) params.get("id");
        }

        String provinceId = null;
        if (params.get("provinceId") != null) {
            provinceId = (String) params.get("provinceId");
        }

        String subjectId = AppHolder.getInstance().user.getSubjectId();

        return API.getInstance(ApiService.class).news_content_list(API.createHeader(), id, provinceId, subjectId, pageIndex, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<NewsItem>>handleResult());

//        return Observable.create(new Observable.OnSubscribe<IPageModel<NewsItem>>() {
//            @Override
//            public void call(Subscriber<? super IPageModel<NewsItem>> subscriber) {
//
//                final List<NewsItem> list = new ArrayList<NewsItem>();
//
//                for (int i = 0; i < 10; i++) {
//
//                    NewsItem newsItem = new NewsItem();
//                    newsItem.setTitle("新闻标题");
//                    newsItem.setImgUrl("http://i3.itc.cn/20170306/2fcf_aa00602f_2235_a6d2_ad46_a1abe2c38d79_2.jpg");
//                    newsItem.setShowTime("2016-06-06");
//                    list.add(newsItem);
//                }
//
//                PageModel<NewsItem> pageModel = new PageModel<>();
//                pageModel.dataList = list;
//                pageModel.pageNumber = 1;
//                pageModel.totalPage = 5;
//
//                subscriber.onNext(pageModel);
//                subscriber.onCompleted();
//
//            }
//        }).delay(2, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                ;

    }
}