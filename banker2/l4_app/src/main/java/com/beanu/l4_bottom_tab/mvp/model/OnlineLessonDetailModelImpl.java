package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.Period;
import com.beanu.l4_bottom_tab.mvp.contract.OnlineLessonDetailContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/05
 */

public class OnlineLessonDetailModelImpl implements OnlineLessonDetailContract.Model {

    @Override
    public Observable<OnlineLesson> online_lesson_detail(String lessonId) {
        return API.getInstance(ApiService.class).online_lesson_detail(API.createHeader(), lessonId)
                .compose(RxHelper.<OnlineLesson>handleResult());
    }

    @Override
    public Observable<List<Period>> online_lesson_period(String lessonId) {
        return API.getInstance(ApiService.class).online_lesson_period_list(API.createHeader(), lessonId)
                .compose(RxHelper.<List<Period>>handleResult());
    }
}