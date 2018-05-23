package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.OrderItem;
import com.beanu.l4_bottom_tab.mvp.contract.MyOrderContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/07
 */

public class MyOrderModelImpl implements MyOrderContract.Model {

    @Override
    public Observable<? extends IPageModel<OrderItem>> loadData(Map<String, Object> params, int pageIndex) {

        return API.getInstance(ApiService.class).my_order_list(API.createHeader(), params, pageIndex, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<OrderItem>>handleResult());
    }

    @Override
    public Observable<String> deleteOrder(String orderId) {
        return API.getInstance(ApiService.class).delete_order(API.createHeader(), orderId)
                .compose(RxHelper.<String>handleResult());
    }
}