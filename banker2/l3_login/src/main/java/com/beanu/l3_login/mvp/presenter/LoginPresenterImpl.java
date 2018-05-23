package com.beanu.l3_login.mvp.presenter;

import com.beanu.arad.Arad;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Base64Util;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_login.mvp.contract.LoginContract;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created by Beanu on 2017/02/13
 */

public class LoginPresenterImpl extends LoginContract.Presenter {

    @Override
    public void login(final String account, final String password, final String loginType, String token, int sex, String icon, String nickName) {

        if (sex == 1) {
            sex = 0;//男
        } else if (sex == 2) {
            sex = 1;//女
        }

        mModel.httpLogin(account, password, loginType, token, sex, icon, nickName).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(@NonNull User user) {
                AppHolder.getInstance().setUser(user);

                //保存到本地
                if ("0".equals(loginType)) {
                    Arad.preferences.putString(Constants.P_ACCOUNT, account);
                    Arad.preferences.putString(Constants.P_PWD, Base64Util.makeStringToBase64(password));
                }
                Arad.preferences.putString(Constants.P_LOGIN_TYPE, loginType);
                Arad.preferences.putString(Constants.P_User_Id, user.getId());
                Arad.preferences.putBoolean(Constants.P_ISFIRSTLOAD, false);
                Arad.preferences.flush();


                //通知登录成功
                Arad.bus.post(new EventModel.LoginEvent(user));

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.loginFailed(e.getMessage());
            }

            @Override
            public void onComplete() {
                mView.loginSuccess();

            }
        });
    }

    @Override
    public void getUnionId(String token) {

        mModel.getUnionId(token).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(String map) {
                map = map.substring(map.indexOf("(") + 1, map.lastIndexOf(")"));

                try {
                    JSONObject jsonObject = new JSONObject(map);

                    mView.unionIdSuccess(jsonObject.get("unionid").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

            }
        });
    }
}