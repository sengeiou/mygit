package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.mvp.contract.UserInfoContract;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/05/22
 */

public class UserInfoModelImpl implements UserInfoContract.Model {

    @Override
    public Observable<String> getQNToken() {
        return API.getInstance(APIService.class).getQNToken().compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> updateUserInfo(String icon, String nickName, String motto, String sex) {
        return API.getInstance(APIService.class).update_user_info(icon, nickName, motto, sex)
                .compose(RxHelper.<String>handleResult());
    }
}