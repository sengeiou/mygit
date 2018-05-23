package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.Period;

import java.util.List;

import io.reactivex.Observable;

/**
 * 高清网课详情
 * Created by Beanu on 2017/4/5.
 */

public interface OnlineLessonDetailContract {
    public interface View extends BaseView {
        public void refreshView(OnlineLesson lesson);

        public void refreshPeriod(List<Period> periods);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestHttpData(String lessonId);
    }

    public interface Model extends BaseModel {

        Observable<OnlineLesson> online_lesson_detail(String lessonId);

        Observable<List<Period>> online_lesson_period(String lessonId);

    }


}