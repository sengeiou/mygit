package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;

/**
 * 教师 在售课程列表
 * Created by Beanu on 2017/5/22.
 */

public interface TeacherLessonListContract {

    public interface View extends ILoadMoreView<LiveLesson> {
    }

    public abstract class Presenter extends LoadMorePresenterImpl<LiveLesson, View, Model> {
    }

    public interface Model extends ILoadMoreModel<LiveLesson> {
    }


}