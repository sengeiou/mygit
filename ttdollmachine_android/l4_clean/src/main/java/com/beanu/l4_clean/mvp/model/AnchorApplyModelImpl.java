package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.mvp.contract.AnchorApplyContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2018/01/25
 */

public class AnchorApplyModelImpl implements AnchorApplyContract.Model {

    @Override
    public Observable<String> refreshQNToken() {
        return API.getInstance(APIService.class).getQNToken().compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<Object> applyAnchor(Map<String, String> params) {
        return API.getInstance(APIService.class).anchorApply(params).compose(RxHelper.handleResult());
    }
}