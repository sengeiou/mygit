package com.beanu.l3_login.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_login.model.ApiLoginService;
import com.beanu.l3_login.model.ApiQQService;
import com.beanu.l3_login.mvp.contract.LoginContract;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Beanu on 2017/02/13
 */

public class LoginModelImpl implements LoginContract.Model {

    @Override
    public Observable<User> httpLogin(String account, String password, String loginType, String token, int sex, String icon, String nickName) {

        return API.getInstance(ApiLoginService.class).user_login(account, password, loginType, token, sex, icon, nickName)
                .compose(RxHelper.<User>handleResult());
    }

    @Override
    public Observable<String> getUnionId(String token) {
        return API.getInstanceWithBaseUrl2(ApiQQService.class, "https://graph.qq.com/oauth2.0/").getUnionid(token, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}