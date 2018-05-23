package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.mvp.contract.RecommendContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/07
 */

public class RecommendModelImpl implements RecommendContract.Model {

    @Override
    public Observable<? extends IPageModel<Object>> loadData(Map<String, Object> params, int pageIndex) {

        int position = 0;
        if (params.get("position") != null) {
            position = (int) params.get("position");
        }
        String subjectId = (String) params.get("subjectId");

        Observable result = null;
        switch (position) {
            case 0:
                result = API.getInstance(ApiService.class).recommend_live_lesson_list(API.createHeader(), subjectId, pageIndex, Constants.PAGE_SIZE)
                        .compose(RxHelper.<PageModel<LiveLesson>>handleResult());
                break;

            case 1:

                result = API.getInstance(ApiService.class).recommend_online_lesson_list(API.createHeader(), subjectId, pageIndex, Constants.PAGE_SIZE)
                        .compose(RxHelper.<PageModel<OnlineLesson>>handleResult());
                break;

            case 2:

                result = API.getInstance(ApiService.class).recommend_book_list(API.createHeader(), subjectId, pageIndex, Constants.PAGE_SIZE)
                        .compose(RxHelper.<PageModel<BookItem>>handleResult());
                break;
        }

        return result;
    }
}