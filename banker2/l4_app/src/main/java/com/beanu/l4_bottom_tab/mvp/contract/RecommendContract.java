package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;

/**
 * 推荐
 * Created by Beanu on 2017/4/7.
 */

public interface RecommendContract {

    public interface View extends ILoadMoreView<Object> {
    }

    public abstract class Presenter extends LoadMorePresenterImpl<Object, View, Model> {
    }

    public interface Model extends ILoadMoreModel<Object> {
    }

}