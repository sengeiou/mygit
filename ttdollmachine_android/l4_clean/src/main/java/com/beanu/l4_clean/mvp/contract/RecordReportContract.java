package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;

import java.util.Map;

import io.reactivex.Observable;

public interface RecordReportContract {


    public interface View extends BaseView {
        void reportSuccess();
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void uploadImage(String path);

        public abstract void pushReport(Map<String, String> params);
    }

    public interface Model extends BaseModel {

        Observable<String> getQNToken();

        Observable<Object> report(Map<String, String> params);
    }


}