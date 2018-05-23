package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;

import java.util.List;

import io.reactivex.Observable;


/**
 * 直播课 列表
 * Created by Beanu on 2017/3/7.
 */

public interface LiveLessonListContract {

    public interface View extends ILoadMoreView<LiveLesson> {

        void refreshHeaderBanner(List<BannerItem> list);

    }

    public abstract class Presenter extends LoadMorePresenterImpl<LiveLesson, View, Model> {

        public abstract void requestHeaderBanner(int position);

    }

    public interface Model extends ILoadMoreModel<LiveLesson> {

        Observable<List<BannerItem>> banner_list(int position);


    }


}