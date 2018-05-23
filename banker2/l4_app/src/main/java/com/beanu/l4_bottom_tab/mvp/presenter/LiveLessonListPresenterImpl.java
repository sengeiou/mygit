package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.mvp.contract.LiveLessonListContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/03/07
 */

public class LiveLessonListPresenterImpl extends LiveLessonListContract.Presenter {
    private List<BannerItem> mBannerList = new ArrayList<>();



    @Override
    public void requestHeaderBanner(int position) {
        //获取banner
        mModel.banner_list(position)
                .subscribe(new Observer<List<BannerItem>>() {
                    @Override
                    public void onComplete() {
                        mView.refreshHeaderBanner(mBannerList);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<BannerItem> list) {
                        mBannerList.clear();
                        mBannerList.addAll(list);

                    }
                });

    }

    public List<BannerItem> getBannerList() {
        return mBannerList;
    }

}