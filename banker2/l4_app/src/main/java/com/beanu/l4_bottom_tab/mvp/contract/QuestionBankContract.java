package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.Course;

import java.util.List;

import io.reactivex.Observable;

/**
 * 题库
 * Created by Beanu on 2017/3/23.
 */

public interface QuestionBankContract {

    public interface View extends BaseView {
        void refreshCourseList(List<Course> list);

        void refreshHeaderBanner(List<BannerItem> list);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestHttpData(int position, String subjectId);
    }

    public interface Model extends BaseModel {
        Observable<List<Course>> course_list(String subjectId);

        Observable<List<BannerItem>> banner_list(int position);
    }


}