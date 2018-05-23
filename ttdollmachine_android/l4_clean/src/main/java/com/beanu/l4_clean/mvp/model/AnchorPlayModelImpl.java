package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.LiveRoom2Audience;
import com.beanu.l4_clean.mvp.contract.AnchorPlayContract;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/12/04
 */

public class AnchorPlayModelImpl implements AnchorPlayContract.Model {

    @Override
    public Observable<LiveRoom2Audience> audienceLiveRoom(String roomId, String logId) {
        return API.getInstance(APIService.class).audienceLiveRoom(roomId, logId).compose(RxHelper.<LiveRoom2Audience>handleResult());
    }

    @Override
    public Observable<Object> anchorHelpMe(String logId) {
        return API.getInstance(APIService.class).anchorHelpMeDraw(logId).compose(RxHelper.handleResult());
    }
}