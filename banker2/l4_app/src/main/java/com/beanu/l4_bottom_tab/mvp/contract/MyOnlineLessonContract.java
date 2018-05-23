package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;

import java.util.List;

import io.reactivex.Observable;

/**
 * 我的网课
 * Created by Beanu on 2017/4/7.
 */

public interface MyOnlineLessonContract {


    public interface View extends BaseView {
        public void refreshUI(List<OnlineLesson> result);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void myOnlineLesson();
    }

    public interface Model extends BaseModel {
        Observable<List<OnlineLesson>> myOnlineLesson();
    }


}