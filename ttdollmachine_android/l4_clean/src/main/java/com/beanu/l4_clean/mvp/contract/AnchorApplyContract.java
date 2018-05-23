package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;

import java.util.Map;

import io.reactivex.Observable;

/**
 * 申请主播
 * Created by Beanu on 2018/1/25.
 */

public interface AnchorApplyContract {

    public interface View extends BaseView {

        public void uiApplyAnchor(boolean success);

    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void applyAnchor(Map<String, String> parmas, String pathC, String pathF, String pathB);
    }

    public interface Model extends BaseModel {
        Observable<String> refreshQNToken();

        Observable<Object> applyAnchor(Map<String, String> params);

    }


}