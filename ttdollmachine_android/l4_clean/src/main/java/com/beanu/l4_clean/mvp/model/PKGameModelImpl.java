package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.PKMatchDetail;
import com.beanu.l4_clean.mvp.contract.PKGameContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/11/28
 */

public class PKGameModelImpl implements PKGameContract.Model {

    @Override
    public Observable<PKMatchDetail> pkMatchDetail(String matchId) {
        return API.getInstance(APIService.class).pkMatchDetail(matchId).compose(RxHelper.<PKMatchDetail>handleResult());
    }

    @Override
    public Observable<String> pkMatchGoOn(String matchId, String id) {
        return API.getInstance(APIService.class).pkMatchGoOn(matchId, id).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> pkMatchOver(String matchId) {
        return API.getInstance(APIService.class).pkMatchOver(matchId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<Map<String, Integer>> userBalance() {
        return API.getInstance(APIService.class).userBalance().compose(RxHelper.<Map<String, Integer>>handleResult());
    }
}