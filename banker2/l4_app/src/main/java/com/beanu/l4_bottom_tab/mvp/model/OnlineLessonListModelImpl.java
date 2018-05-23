package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.adapter.provider.Online_HotLesson;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.mvp.contract.OnlineLessonListContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/03/08
 */

public class OnlineLessonListModelImpl implements OnlineLessonListContract.Model {

    @Override
    public Observable<? extends IPageModel<OnlineLesson>> loadData(Map<String, Object> params, int pageIndex) {

        String subjectId = AppHolder.getInstance().user.getSubjectId();
//        if (params.get("subjectId") != null) {
//            subjectId = (String) params.get("subjectId");
//        }
        return API.getInstance(ApiService.class)
                .online_lesson_list(API.createHeader(), subjectId, pageIndex, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<OnlineLesson>>handleResult());
    }

    @Override
    public Observable<List<BannerItem>> banner_list(int position) {
        return API.getInstance(ApiService.class).banner_list(API.createHeader(), position)
                .compose(RxHelper.<List<BannerItem>>handleResult());
    }

    @Override
    public Observable<Online_HotLesson> requestHotLesson() {
        String subjectId = AppHolder.getInstance().user.getSubjectId();

        return API.getInstance(ApiService.class).online_lesson_hot(subjectId).compose(RxHelper.<Online_HotLesson>handleResult());
    }
}