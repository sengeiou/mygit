package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.model.bean.Comment;

/**
 * 评论列表
 * Created by Beanu on 2017/4/6.
 */

public interface CommentContract {
    public interface View extends ILoadMoreView<Comment> {

    }

    public abstract class Presenter extends LoadMorePresenterImpl<Comment, View, Model> {

    }

    public interface Model extends ILoadMoreModel<Comment> {
    }


}