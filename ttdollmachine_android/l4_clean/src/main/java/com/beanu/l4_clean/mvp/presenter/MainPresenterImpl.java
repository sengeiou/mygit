package com.beanu.l4_clean.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.mvp.contract.MainContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.rong.imlib.RongIMClient;

/**
 * Created by Beanu on 2017/11/09
 */

public class MainPresenterImpl extends MainContract.Presenter {


    private List<BannerItem> mBannerList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //如果已经登录,初始化融云等
        if (AppHolder.getInstance().user.isLogin()) {
            loginSuccessEvent(AppHolder.getInstance().user);
        }

        bannerList();

    }


    @Override
    public void userBalance() {
        mModel.userBalance().subscribe(new Observer<Map<String, Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, Integer> map) {
                int coin = map.get("coins");
                if (coin >= 0) {
                    AppHolder.getInstance().user.setCoins(coin);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                mView.uiUserBalance();
            }
        });
    }

    @Override
    public void loginSuccessEvent(User user) {
        //0.全局变量负值
        AppHolder.getInstance().setUser(user);

        //1.链接融云
        if (TextUtils.isEmpty(user.getRongyunToken())) {
            updateRongCloudToken(user.getId());
        } else {
            connectRongCloud(user.getId(), user.getRongyunToken());
        }

        rewardsInfo();

        //2.检测当前用户是否可以免费抓
//        boolean show = Arad.preferences.getBoolean(Constants.P_ISFIRST_LOGIN, true);
//
//        if (AppHolder.getInstance().user.getSuccess_num() == 0 && show) {
//            mView.uiShowNewUserFreeTips();
//
//            Arad.preferences.putBoolean(Constants.P_ISFIRST_LOGIN, false);
//            Arad.preferences.flush();
//        } else {
        //3.0检测当天是否提示领取积分了

//            String time = AppHolder.getInstance().user.getLastSignTime();
//            try {
//
//                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.HOUR_OF_DAY, 0);
//                cal.set(Calendar.SECOND, 0);
//                cal.set(Calendar.MINUTE, 0);
//                cal.set(Calendar.MILLISECOND, 0);
//                Date day = cal.getTime();
//
//                boolean before = TimeUtils.string2Date(time).before(day);
//                if (before) {
//                    rewardsInfo();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                rewardsInfo();
//            }

//        }

    }

    @Override
    public void updateRongCloudToken(final String userId) {

        mModel.requestRongCloudToken(userId).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(String s) {
                connectRongCloud(userId, s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void connectRongCloud(final String userId, String token) {


        //连接融云
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                KLog.i("rongconnect", "token 失效，需要重新获取token");
                updateRongCloudToken(userId);
            }

            @Override
            public void onSuccess(String s) {
                KLog.d("融云 连接成功");


            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                KLog.d("融云 连接失败" + errorCode.getMessage());


            }
        });

    }

    @Override
    public void bannerList() {
        mModel.bannerList().subscribe(new Observer<List<BannerItem>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(List<BannerItem> itemList) {
                mBannerList = itemList;
                mView.uiRefreshBanner(itemList);
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

    @Override
    public void rewardsInfo() {
        mModel.freeCard().subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, String> map) {
                String suc = map.get("suc");
                if ("1".equals(suc)) {
                    mView.uiShowUserFreeTips();
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


    public List<BannerItem> getBannerList() {
        return mBannerList;
    }
}