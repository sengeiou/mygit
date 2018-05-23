package com.beanu.l4_bottom_tab.mvp.contract;


import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.model.bean.ExamHistory;

/**
 * 历年真题
 * Created by Beanu on 2017/3/29.
 */

public interface ToolsTestPaperContract {
    public interface View extends ILoadMoreView<ExamHistory> {
    }

    public abstract class Presenter extends LoadMorePresenterImpl<ExamHistory, View, Model> {
    }

    public interface Model extends ILoadMoreModel<ExamHistory> {
    }

}