package com.beanu.l3_login.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_login.model.APILoginService;
import com.beanu.l3_login.mvp.contract.LoginContract;

import io.reactivex.Observable;


/**
 * Created by Beanu on 2017/02/13
 */

public class LoginModelImpl implements LoginContract.Model {


    @Override
    public Observable<User> httpLogin(String account, String password, String loginType, String token, int sex, String icon, String nickName) {

        return API.getInstance(APILoginService.class).user_login(account, password, loginType, token, sex, icon, nickName)
                .compose(RxHelper.<User>handleResult());

    }

    @Override
    public Observable<String> sendSMSCode(String phoneNum) {
        return API.getInstance(APILoginService.class).smsVCode(phoneNum, "0").compose(RxHelper.<String>handleResult());
    }

}