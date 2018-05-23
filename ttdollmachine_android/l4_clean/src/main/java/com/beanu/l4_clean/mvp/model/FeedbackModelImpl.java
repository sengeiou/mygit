package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.mvp.contract.FeedbackContract;

import io.reactivex.Observable;

/**
 * Created by jrl on 2017/11/21
 */

public class FeedbackModelImpl implements FeedbackContract.Model {

    @Override
    public Observable<String> getQNToken() {
        return API.getInstance(APIService.class).getQNToken().compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> push(String content) {
        return API.getInstance(APIService.class).feedBack(content).compose(RxHelper.<String>handleResult());
    }
}