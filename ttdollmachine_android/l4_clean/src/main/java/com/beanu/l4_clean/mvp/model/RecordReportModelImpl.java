package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.mvp.contract.RecordReportContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2018/03/27
 */

public class RecordReportModelImpl implements RecordReportContract.Model {

    @Override
    public Observable<String> getQNToken() {
        return API.getInstance(APIService.class).getQNToken().compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<Object> report(Map<String, String> params) {
        return API.getInstance(APIService.class).recordReport(params).compose(RxHelper.handleResult());
    }
}