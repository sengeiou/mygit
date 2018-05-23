package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.PKMatch;
import com.beanu.l4_clean.mvp.contract.ChallengeContract;

import java.util.Map;

import io.reactivex.Observable;


/**
 * Created by Beanu on 2017/11/25
 */

public class ChallengeModelImpl implements ChallengeContract.Model {

    @Override
    public Observable<PKMatch> initMatch() {
        return API.getInstance(APIService.class).isExistenceMath().compose(RxHelper.<PKMatch>handleResult());
    }

    @Override
    public Observable<Map<String, String>> acceptInvitation(String code) {
        return API.getInstance(APIService.class).invite_accept(code).compose(RxHelper.<Map<String, String>>handleResult());
    }

    @Override
    public Observable<Map<String, String>> invite2PK() {
        return API.getInstance(APIService.class).invite2PK().compose(RxHelper.<Map<String, String>>handleResult());
    }
}