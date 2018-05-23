package com.beanu.l3_login.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_login.model.ApiLoginService;
import com.beanu.l3_login.mvp.contract.RegisterSMSContract;

import io.reactivex.Observable;


/**
 * Created by Beanu on 2017/02/13
 */

public class RegisterSMSModelImpl implements RegisterSMSContract.Model {

    @Override
    public Observable<String> sendSMSCode(String phoneNum) {

        return API.getInstance(ApiLoginService.class).smsVCode(phoneNum, "0")
                .compose(RxHelper.<String>handleResult());
    }
}