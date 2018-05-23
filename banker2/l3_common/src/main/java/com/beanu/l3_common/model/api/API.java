package com.beanu.l3_common.model.api;

import com.beanu.arad.Arad;
import com.beanu.l3_common.util.AppHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * 对外提供统一的获取请求服务实例
 * Created by Beanu on 2017/3/28.
 */

public class API {

    /**
     * 获取一般请求的服务实例
     *
     * @param clazz 模块服务对应的实例类型
     * @param <T>   泛型
     * @return 指定的服务类型的实例
     */
    public static <T> T getInstance(Class<T> clazz) {
        return ApiManager.getService(clazz);
    }

    /**
     * 获取指定Url请求的服务实例
     *
     * @param clazz   服务实例类型
     * @param baseUrl 指定请求接口地址
     * @param <T>     泛型
     * @return 指定的服务类型的实例
     */
    public static <T> T getInstanceWithBaseUrl(Class<T> clazz, String baseUrl) {
        return ApiManager.getServiceWithBaseUrl(clazz, baseUrl);
    }

    public static <T> T getInstanceWithBaseUrl2(Class<T> clazz, String baseUrl) {
        return ApiManager.getServiceWithBaseUrl2(clazz, baseUrl);
    }

    /**
     * 创建共用的头部信息
     *
     * @return header
     */
    public static Map<String, String> createHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("uuid", Arad.app.deviceInfo.getDeviceID());
        if (AppHolder.getInstance().user.getId() != null) {
            header.put("userId", AppHolder.getInstance().user.getId());
        }
        header.put("sign", "");
        header.put("timestamp", System.currentTimeMillis() + "");
        header.put("phoneType", "0");
        header.put("apkVersion", Arad.app.deviceInfo.getVersionName());
        return header;
    }
}
