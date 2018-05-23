package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.mvp.contract.MyOnlineLessonContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/06/02
 */

public class MyOnlineLessonModelImpl implements MyOnlineLessonContract.Model {

    @Override
    public Observable<List<OnlineLesson>> myOnlineLesson() {

        return API.getInstance(ApiService.class)
                .my_online_lesson_list(API.createHeader())
                .compose(RxHelper.<List<OnlineLesson>>handleResult());
    }
}