package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.adapter.provider.ToolsHistory;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.mvp.contract.ToolsHistoryContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/25
 */

public class ToolsHistoryModelImpl implements ToolsHistoryContract.Model {

    @Override
    public Observable<? extends IPageModel<ToolsHistory>> loadData(Map<String, Object> params, int pageIndex) {

        int type = 0;
        if (params.get("type") != null) {
            type = (int) params.get("type");
        }

        return API.getInstance(ApiService.class).tools_history_list(API.createHeader(), type, pageIndex, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<ToolsHistory>>handleResult());
    }
}