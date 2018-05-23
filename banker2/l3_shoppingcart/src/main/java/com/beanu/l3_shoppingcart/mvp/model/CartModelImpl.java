package com.beanu.l3_shoppingcart.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_shoppingcart.model.APICartService;
import com.beanu.l3_shoppingcart.model.bean.CartItem;
import com.beanu.l3_shoppingcart.mvp.contract.CartContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/03/10
 */

public class CartModelImpl implements CartContract.Model {

    @Override
    public Observable<List<CartItem>> requestCartList() {

        return API.getInstance(APICartService.class)
                .shop_cart_list(API.createHeader())
                .compose(RxHelper.<List<CartItem>>handleResult());
    }

    @Override
    public Observable<Integer> updateCartShop(CartItem cartItem) {

        return API.getInstance(APICartService.class).update_shop_cart(API.createHeader(), cartItem.getId(), cartItem.getNum(), cartItem.isSelect())
                .compose(RxHelper.<Integer>handleResult());
    }

    @Override
    public Observable<Void> deleteCartShop(String cartIds) {

        return API.getInstance(APICartService.class).delete_shop_cart(API.createHeader(), cartIds)
                .compose(RxHelper.<Void>handleResult());
    }

    @Override
    public Observable<Void> updateAllCartShop(int type) {
        return API.getInstance(APICartService.class).select_shop_cart(API.createHeader(), type)
                .compose(RxHelper.<Void>handleResult());
    }
}