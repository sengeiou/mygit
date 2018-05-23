package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.model.bean.NewsItem;

/**
 * 新闻列表
 * Created by Beanu on 2017/3/7.
 */

public interface NewsListContract {


    public interface View extends ILoadMoreView<NewsItem> {
    }

    public abstract class Presenter extends LoadMorePresenterImpl<NewsItem, View, Model> {
    }

    public interface Model extends ILoadMoreModel<NewsItem> {
    }


}