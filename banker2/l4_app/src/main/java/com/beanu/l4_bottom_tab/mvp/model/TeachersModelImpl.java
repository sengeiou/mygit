package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.mvp.contract.TeachersContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/06
 */

public class TeachersModelImpl implements TeachersContract.Model {

    @Override
    public Observable<List<TeacherIntro>> online_lesson_teacher_list(String lessonId) {
        return API.getInstance(ApiService.class).online_lesson_teacher_list(API.createHeader(), lessonId)
                .compose(RxHelper.<List<TeacherIntro>>handleResult());
    }

    @Override
    public Observable<List<TeacherIntro>> live_lesson_teacher_list(String lessonId) {
        return API.getInstance(ApiService.class).live_lesson_teacher_list(API.createHeader(), lessonId)
                .compose(RxHelper.<List<TeacherIntro>>handleResult());
    }
}