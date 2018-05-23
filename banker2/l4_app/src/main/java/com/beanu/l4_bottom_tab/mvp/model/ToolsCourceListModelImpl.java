package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.Course;
import com.beanu.l4_bottom_tab.mvp.contract.ToolsCourceListContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/24
 */

public class ToolsCourceListModelImpl implements ToolsCourceListContract.Model {

    @Override
    public Observable<List<Course>> wrong_course_list(String parentId) {
        return API.getInstance(ApiService.class).tools_wrong_course_list(API.createHeader(), parentId)
                .compose(RxHelper.<List<Course>>handleResult());
    }

    @Override
    public Observable<List<Course>> collect_course_list(String parentId) {
        return API.getInstance(ApiService.class).tools_collect_course_list(API.createHeader(), parentId)
                .compose(RxHelper.<List<Course>>handleResult());
    }

    @Override
    public Observable<List<Course>> note_course_list(String parentId) {
        return API.getInstance(ApiService.class).tools_note_course_list(API.createHeader(), parentId)
                .compose(RxHelper.<List<Course>>handleResult());
    }
}