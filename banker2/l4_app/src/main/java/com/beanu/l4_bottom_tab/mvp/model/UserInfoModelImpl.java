package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.mvp.contract.UserInfoContract;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/05/22
 */

public class UserInfoModelImpl implements UserInfoContract.Model {

    @Override
    public Observable<String> getQNToken() {
        return API.getInstance(ApiService.class).qn_token(API.createHeader()).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> updateUserInfo(String icon, String nickName, String motto, String sex, String school) {
        return API.getInstance(ApiService.class).update_user_info(API.createHeader(), icon, nickName, motto, sex, school)
                .compose(RxHelper.<String>handleResult());
    }
}