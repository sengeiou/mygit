package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;

import io.reactivex.Observable;


/**
 * 用户信息管理
 * Created by Beanu on 2017/5/22.
 */

public interface UserInfoContract {

    public interface View extends BaseView {
        public void updateSuccess();
    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        //更新用户信息
        public abstract void updateUserInfo(String icon, String nickName, String motto, String sex);

        //上传头像图片
        public abstract void uploadPhoto(String imgPaths);
    }

    public interface Model extends BaseModel {
        Observable<String> getQNToken();

        Observable<String> updateUserInfo(String icon, String nickName, String motto, String sex);
    }

}