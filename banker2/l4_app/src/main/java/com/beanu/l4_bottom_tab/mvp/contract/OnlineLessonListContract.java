package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.adapter.provider.Online_HotLesson;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;

import java.util.List;

import io.reactivex.Observable;

/**
 * 高清网课
 * Created by Beanu on 2017/3/8.
 */

public interface OnlineLessonListContract {

    public interface View extends ILoadMoreView<OnlineLesson> {
        void refreshHeaderBanner(List<BannerItem> list);

        void refreshHotLesson(boolean show, Online_HotLesson hotLesson);
    }

    public abstract class Presenter extends LoadMorePresenterImpl<OnlineLesson, View, Model> {
        public abstract void requestHttpData(int position);
    }

    public interface Model extends ILoadMoreModel<OnlineLesson> {
        Observable<List<BannerItem>> banner_list(int position);

        Observable<Online_HotLesson> requestHotLesson();
    }


}