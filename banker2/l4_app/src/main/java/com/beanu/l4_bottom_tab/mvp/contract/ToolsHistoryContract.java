package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.adapter.provider.ToolsHistory;

/**
 * 练习历史
 * Created by Beanu on 2017/4/25.
 */

public interface ToolsHistoryContract {

    public interface View extends ILoadMoreView<ToolsHistory> {
    }

    public abstract class Presenter extends LoadMorePresenterImpl<ToolsHistory, View, Model> {
    }

    public interface Model extends ILoadMoreModel<ToolsHistory> {
    }


}