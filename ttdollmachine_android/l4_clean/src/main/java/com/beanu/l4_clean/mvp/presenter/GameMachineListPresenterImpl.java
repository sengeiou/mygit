package com.beanu.l4_clean.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.beanu.l3_common.model.PageModel;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.mvp.contract.GameMachineListContract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/12/05
 */

public class GameMachineListPresenterImpl extends GameMachineListContract.Presenter {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

//        bannerList();
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
    public void topDolls() {
        mModel.topDolls().subscribe(new Observer<PageModel<Dolls>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(PageModel<Dolls> pageModel) {
                for (Dolls dolls : pageModel.dataList) {
                    dolls.setTop(true);
                }

                mView.uiRefreshTOP(pageModel.dataList);
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