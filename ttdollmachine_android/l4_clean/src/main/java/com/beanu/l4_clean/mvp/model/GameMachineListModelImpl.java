package com.beanu.l4_clean.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.mvp.contract.GameMachineListContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/12/05
 */

public class GameMachineListModelImpl implements GameMachineListContract.Model {

    @Override
    public Observable<? extends IPageModel<Dolls>> loadData(Map<String, Object> params, int pageIndex) {
        String typeId = (String) params.get("typeId");

        return API.getInstance(APIService.class).dollMachineList(typeId, pageIndex, Constants.AmountOfPrePage)
                .compose(RxHelper.<PageModel<Dolls>>handleResult());
    }

    @Override
    public Observable<List<BannerItem>> bannerList() {
        return API.getInstance(APIService.class).bannerList(1).compose(RxHelper.<List<BannerItem>>handleResult());
    }

    @Override
    public Observable<PageModel<Dolls>> topDolls() {
        return API.getInstance(APIService.class).dollMachineTOPList().compose(RxHelper.<PageModel<Dolls>>handleResult());
    }
}