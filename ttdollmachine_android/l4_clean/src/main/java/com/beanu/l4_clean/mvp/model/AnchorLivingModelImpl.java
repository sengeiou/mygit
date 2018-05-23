package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.HelpMeCraw;
import com.beanu.l4_clean.model.bean.HelpUser;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.beanu.l4_clean.mvp.contract.AnchorLivingContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/12/01
 */

public class AnchorLivingModelImpl implements AnchorLivingContract.Model {

    @Override
    public Observable<SeizeResult> startSeize(String dollMaId) {
        return API.getInstance(APIService.class).anchorBeginGame(dollMaId).compose(RxHelper.<SeizeResult>handleResult());
    }

    @Override
    public Observable<Object> exitLive(String roomId, String logId) {
        return API.getInstance(APIService.class).anchorEndGame(roomId, logId).compose(RxHelper.handleResult());
    }

    @Override
    public Observable<List<HelpMeCraw>> helpMeList(String logId) {
        return API.getInstance(APIService.class).anchorHelpMeList(logId).compose(RxHelper.<List<HelpMeCraw>>handleResult());
    }

    @Override
    public Observable<HelpUser> anchorAcceptCraw(String id) {
        return API.getInstance(APIService.class).anchorAccepted(id, "1").compose(RxHelper.<HelpUser>handleResult());
    }

}