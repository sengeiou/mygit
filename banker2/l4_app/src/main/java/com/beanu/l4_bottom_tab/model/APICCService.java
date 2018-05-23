package com.beanu.l4_bottom_tab.model;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/10/17.
 */

public interface APICCService {

    /**
     * 获取cc视频信息
     */
    @GET("api/video/v3")
    Observable<JSONObject> initProgram(@HeaderMap Map<String, String> header);

}
