package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_bottom_tab.model.bean.Course;

import java.util.List;

import io.reactivex.Observable;

/**
 * 工具-科目列表 通用
 * Created by Beanu on 2017/4/24.
 */

public interface ToolsCourceListContract {
    public interface View extends BaseView {
        void refreshCourseList(List<Course> list);

        void refreshNextChild(List<Course> list);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestCourseList(int type, String parentId);
    }

    public interface Model extends BaseModel {
        Observable<List<Course>> wrong_course_list(String parentId);

        Observable<List<Course>> collect_course_list(String parentId);

        Observable<List<Course>> note_course_list(String parentId);
    }


}