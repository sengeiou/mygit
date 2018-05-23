package com.beanu.l3_login.model;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 登录 注册接口专用
 */
public interface ApiQQService {

    /**
     * 获取unionid
     */
    @GET("me")
    Observable<String> getUnionid(@Query("access_token") String token, @Query("unionid") String unionid);

}





