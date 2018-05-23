package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.mvp.contract.TeacherLessonListContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/05/22
 */

public class TeacherLessonListModelImpl implements TeacherLessonListContract.Model {

    @Override
    public Observable<? extends IPageModel<LiveLesson>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(ApiService.class).teacher_sale_lesson_list(API.createHeader(), params, pageIndex, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<LiveLesson>>handleResult());
    }
}