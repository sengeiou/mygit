package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.mvp.contract.MainContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/11/09
 */

public class MainModelImpl implements MainContract.Model {


    @Override
    public Observable<String> requestRongCloudToken(String userId) {
        return API.getInstance(APIService.class).updateRongCloudToken(userId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<List<BannerItem>> bannerList() {
        return API.getInstance(APIService.class).bannerList(0).compose(RxHelper.<List<BannerItem>>handleResult());
    }

//    @Override
//    public Observable<Rewards> rewardsInfo() {
//        return API.getInstance(APIService.class).rewardsInfo().compose(RxHelper.<Rewards>handleResult());
//    }

    @Override
    public Observable<Map<String, Integer>> userBalance() {
        return API.getInstance(APIService.class).userBalance().compose(RxHelper.<Map<String, Integer>>handleResult());
    }

    @Override
    public Observable<Map<String, String>> freeCard() {
        return API.getInstance(APIService.class).receiveFreeCard().compose(RxHelper.<Map<String,String>>handleResult());
    }
}