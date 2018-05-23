package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.ExamHistory;
import com.beanu.l4_bottom_tab.mvp.contract.ToolsTestPaperContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/03/29
 */

public class ToolsTestPaperModelImpl implements ToolsTestPaperContract.Model {

    @Override
    public Observable<? extends IPageModel<ExamHistory>> loadData(Map<String, Object> params, int pageIndex) {

        String id = AppHolder.getInstance().user.getSubjectId();

        return API.getInstance(ApiService.class)
                .test_paper_list(API.createHeader(), id, pageIndex, 20)
                .compose(RxHelper.<PageModel<ExamHistory>>handleResult());
    }
}