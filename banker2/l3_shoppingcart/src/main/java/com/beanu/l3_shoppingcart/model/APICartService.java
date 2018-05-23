package com.beanu.l3_shoppingcart.model;


import com.beanu.l3_common.model.HttpModel;
import com.beanu.l3_shoppingcart.model.bean.AddressItem;
import com.beanu.l3_shoppingcart.model.bean.CartItem;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import io.reactivex.Observable;

/**
 * 购物车接口专用
 */
public interface APICartService {

    //*********************************************************************************************
    //                                  购物车商品 相关
    //*********************************************************************************************

    /**
     * 2.3 更新购物车中的某个商品
     */
    @FormUrlEncoded
    @POST("updateShoppingCart")
    Observable<HttpModel<Integer>> update_shop_cart(@HeaderMap Map<String, String> header, @Field("cartId") String cartId, @Field("num") int num, @Field("isSelect") int isSelect);


    /**
     * 2.4 获取购物车列表
     */
    @GET("shopCarListMap")
    Observable<HttpModel<List<CartItem>>> shop_cart_list(@HeaderMap Map<String, String> header);

    /**
     * 2.5 创建图书订单
     */
    @FormUrlEncoded
    @POST("createBookOrder")
    Observable<HttpModel<Map<String, String>>> createBookOrder(@HeaderMap Map<String, String> header, @Field("addressId") String addressId, @Field("cartIds") String cartIds);

    /**
     * 2.7 支付宝签名
     */
    @GET("alipaySign")
    Observable<HttpModel<String>> requestAlipaySign(@Query("id") String orderId);

    /**
     * 2.27 删除购物车中的商品
     *
     * @param cartIds 购物车商品ID数组 逗号隔开
     */
    @FormUrlEncoded
    @POST("delShoppingCart")
    Observable<HttpModel<Void>> delete_shop_cart(@HeaderMap Map<String, String> header, @Field("cartIds") String cartIds);

    /**
     * 2.28 购物车商品的全选或取消
     *
     * @param type 0全部取消 1全选
     */
    @FormUrlEncoded
    @POST("allSelect")
    Observable<HttpModel<Void>> select_shop_cart(@HeaderMap Map<String, String> header, @Field("type") int type);


    /**
     * 2.33 微信签名
     */
    @GET("wxSign")
    Observable<HttpModel<Map<String, String>>> requestWePaySign(@Query("id") String orderId);


    //*********************************************************************************************
    //                                  收货地址的添加和维护
    //*********************************************************************************************

    /**
     * 2.23 添加地址
     */
    @FormUrlEncoded
    @POST("addAddress")
    Observable<HttpModel<String>> address_add(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> params);

    /**
     * 2.24 删除地址
     */
    @GET("delAddress")
    Observable<HttpModel<String>> address_delete(@HeaderMap Map<String, String> header, @Query("id") String addressId);

    /**
     * 2.25 地址列表
     */
    @GET("myAddress")
    Observable<HttpModel<List<AddressItem>>> address_list(@HeaderMap Map<String, String> header);

    /**
     * 2.30 设置默认地址
     */
    @GET("setIsDefault")
    Observable<HttpModel<String>> address_default(@HeaderMap Map<String, String> header, @Query("id") String addressId);

    /**
     * 2.31 我的默认地址
     */
    @GET("myDefaultAddress")
    Observable<HttpModel<AddressItem>> my_address_default(@HeaderMap Map<String, String> header);

}