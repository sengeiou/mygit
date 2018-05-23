package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.Course;
import com.beanu.l4_bottom_tab.mvp.contract.QuestionBankContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/03/23
 */

public class QuestionBankModelImpl implements QuestionBankContract.Model {

    @Override
    public Observable<List<Course>> course_list(String subjectId) {

        return API.getInstance(ApiService.class).course_list(API.createHeader(), subjectId)
                .compose(RxHelper.<List<Course>>handleResult());
    }

    @Override
    public Observable<List<BannerItem>> banner_list(int position) {

        return API.getInstance(ApiService.class).banner_list(API.createHeader(), position)
                .compose(RxHelper.<List<BannerItem>>handleResult());
    }
}