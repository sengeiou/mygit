package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;

import java.util.List;

import io.reactivex.Observable;

/**
 * 老师列表
 * Created by Beanu on 2017/4/6.
 */

public interface TeachersContract {

    public interface View extends BaseView {
        public void refreshListView(List<TeacherIntro> list);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestHttpData(String lessonId, int type);
    }

    public interface Model extends BaseModel {

        Observable<List<TeacherIntro>> online_lesson_teacher_list(String lessonId);

        Observable<List<TeacherIntro>> live_lesson_teacher_list(String lessonId);

    }


}