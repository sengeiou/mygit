package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.model.bean.OrderItem;

import io.reactivex.Observable;

/**
 * 我的订单
 * Created by Beanu on 2017/4/7.
 */

public interface MyOrderContract {

    public interface View extends ILoadMoreView<OrderItem> {
        public void deleteOrderSuccess(int position);
    }

    public abstract class Presenter extends LoadMorePresenterImpl<OrderItem, View, Model> {
        public abstract void deleteOrder(int position, String orderId);
    }

    public interface Model extends ILoadMoreModel<OrderItem> {
        Observable<String> deleteOrder(String orderId);
    }


}