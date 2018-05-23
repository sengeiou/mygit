package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.DollsMachine;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.beanu.l4_clean.mvp.contract.GameContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/11/18
 */

public class GameModelImpl implements GameContract.Model {

    @Override
    public Observable<DollsMachine> requestMachineDetail(String id) {
        return API.getInstance(APIService.class)
                .dollMachineDetail(id).compose(RxHelper.<DollsMachine>handleResult());
    }

    @Override
    public Observable<SeizeResult> startSeize(String dollMaId) {
        return API.getInstance(APIService.class).seizeMachine(dollMaId).compose(RxHelper.<SeizeResult>handleResult());
    }

    @Override
    public Observable<Map<String, Integer>> userBalance() {
        return API.getInstance(APIService.class).userBalance().compose(RxHelper.<Map<String, Integer>>handleResult());
    }

    @Override
    public Observable<Map<String, String>> gameResult(String id) {
        return API.getInstance(APIService.class).gameResult(id).compose(RxHelper.<Map<String, String>>handleResult());
    }

    @Override
    public Observable<Map<String, String>> shareGame(String id) {
        return API.getInstance(APIService.class).shareGame(id).compose(RxHelper.<Map<String, String>>handleResult());
    }


}