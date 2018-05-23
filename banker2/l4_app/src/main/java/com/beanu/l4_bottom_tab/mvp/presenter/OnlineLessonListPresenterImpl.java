package com.beanu.l4_bottom_tab.mvp.presenter;

import com.beanu.l4_bottom_tab.adapter.provider.Online_HotLesson;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.mvp.contract.OnlineLessonListContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/03/08
 */

public class OnlineLessonListPresenterImpl extends OnlineLessonListContract.Presenter {

    private List<BannerItem> mBannerList = new ArrayList<>();


    private void hotData() {
        mModel.requestHotLesson()
                .subscribe(new Observer<Online_HotLesson>() {
                    @Override
                    public void onComplete() {
                        loadDataFirst();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadDataFirst();
                        e.printStackTrace();
                        mView.refreshHotLesson(false, null);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Online_HotLesson hotLesson) {
                        if (hotLesson != null) {
                            mView.refreshHotLesson(true, hotLesson);
                        }
                    }
                });
    }

    @Override
    public void requestHttpData(int position) {
        //获取banner
        mModel.banner_list(position)
                .subscribe(new Observer<List<BannerItem>>() {
                    @Override
                    public void onComplete() {
                        mView.refreshHeaderBanner(mBannerList);
                        hotData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hotData();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
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