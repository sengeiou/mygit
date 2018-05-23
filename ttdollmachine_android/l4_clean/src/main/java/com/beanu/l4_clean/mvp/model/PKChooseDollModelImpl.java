package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.mvp.contract.PKChooseDollContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/11/25
 */

public class PKChooseDollModelImpl implements PKChooseDollContract.Model {

    @Override
    public Observable<List<Dolls>> httpDollsList() {
        return API.getInstance(APIService.class)
                .pkMachineList()
                .compose(RxHelper.<List<Dolls>>handleResult());
    }

    @Override
    public Observable<Map<String, String>> pkConfirmReady(String dollMaId, String matchId) {
        return API.getInstance(APIService.class).pkConfirmReady(dollMaId, matchId).compose(RxHelper.<Map<String, String>>handleResult());
    }
}