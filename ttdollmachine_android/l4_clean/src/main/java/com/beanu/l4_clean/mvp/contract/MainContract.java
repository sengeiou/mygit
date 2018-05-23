package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l4_clean.model.bean.BannerItem;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * 首页MVP
 * Created by Beanu on 2017/11/9.
 */

public interface MainContract {

    public interface View extends BaseView {
        public void uiRefreshBanner(List<BannerItem> itemList);

        void uiShowUserFreeTips();

//        void uiShowRewardsDialog(Rewards rewards);

        void uiUserBalance();

    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        //更新当前用户余额
        public abstract void userBalance();

        public abstract void loginSuccessEvent(User user);

        public abstract void updateRongCloudToken(String userId);

        public abstract void connectRongCloud(String userId, String token);

        public abstract void bannerList();

        public abstract void rewardsInfo();

    }

    public interface Model extends BaseModel {
        Observable<String> requestRongCloudToken(String userId);

        Observable<List<BannerItem>> bannerList();

//        Observable<Rewards> rewardsInfo();

        Observable<Map<String, Integer>> userBalance();

        Observable<Map<String, String>> freeCard();

    }


}