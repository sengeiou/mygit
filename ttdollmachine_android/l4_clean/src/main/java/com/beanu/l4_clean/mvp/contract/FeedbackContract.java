package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;

import io.reactivex.Observable;

/**
 * 反馈
 */

public interface FeedbackContract {

    public interface View extends BaseView {
        void requestSuccess();
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void uploadImage(String path);

        public abstract void pushFeedback(String content);
    }

    public interface Model extends BaseModel {

        Observable<String> getQNToken();

        Observable<String> push(String content);
    }


}