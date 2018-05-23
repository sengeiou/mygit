package com.beanu.l3_common.model.bean;

import java.io.Serializable;

/**
 * 版本信息
 * Created by Beanu on 16/6/7.
 */
public class Version implements Serializable {

    private String apkversion;
    private String apkurl;
    private String detail;
    private String promotionUrl;

    public String getApkversion() {
        return apkversion;
    }

    public void setApkversion(String apkversion) {
        this.apkversion = apkversion;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPromotionUrl() {
        return promotionUrl;
    }

    public void setPromotionUrl(String promotionUrl) {
        this.promotionUrl = promotionUrl;
    }
}
