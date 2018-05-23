package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.LiveRoom;

import java.util.List;

import io.reactivex.Observable;


/**
 * 主播设置
 * Created by Beanu on 2017/12/2.
 */

public interface AnchorLivingSettingContract {

    public interface View extends BaseView {
        void uiRefresh(LiveRoom liveRoom);

        void uiFailure();

        void uiDollsList(List<Dolls> dollsList);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void startLiving(String path, String name, String machineId, boolean updateInfo);
    }

    public interface Model extends BaseModel {
        Observable<String> refreshQNToken();

        Observable<LiveRoom> startLiving(String path, String name, String machineId);

        Observable<PageModel<Dolls>> machineList();
    }


}