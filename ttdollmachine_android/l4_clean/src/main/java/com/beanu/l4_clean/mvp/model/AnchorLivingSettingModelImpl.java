package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.LiveRoom;
import com.beanu.l4_clean.mvp.contract.AnchorLivingSettingContract;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/12/02
 */

public class AnchorLivingSettingModelImpl implements AnchorLivingSettingContract.Model {

    @Override
    public Observable<String> refreshQNToken() {
        return API.getInstance(APIService.class).getQNToken().compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<LiveRoom> startLiving(String path, String name, String machineId) {
        return API.getInstance(APIService.class).livingRoomDetail(machineId, path, name).compose(RxHelper.<LiveRoom>handleResult());
    }

    @Override
    public Observable<PageModel<Dolls>> machineList() {
        return API.getInstance(APIService.class)
                .dollMachineList("FFFFFF", 1, 30)
                .compose(RxHelper.<PageModel<Dolls>>handleResult());
    }

}