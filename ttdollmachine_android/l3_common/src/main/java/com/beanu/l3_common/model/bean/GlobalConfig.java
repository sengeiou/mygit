package com.beanu.l3_common.model.bean;

import java.io.Serializable;

/**
 * 全局配置
 * Created by Beanu on 2017/11/7.
 */

public class GlobalConfig implements Serializable {

    private String adImg;
    private String aboutUs;
    private String detail;
    private String cusPhone;
    private String iosversion;
    private String APPShare;
    private String registerProtocol;
    private String apkurl;
    private String apkversion;

    public String getAdImg() {
        return adImg;
    }

    public void setAdImg(String adImg) {
        this.adImg = adImg;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getIosversion() {
        return iosversion;
    }

    public void setIosversion(String iosversion) {
        this.iosversion = iosversion;
    }

    public String getAPPShare() {
        return APPShare;
    }

    public void setAPPShare(String APPShare) {
        this.APPShare = APPShare;
    }

    public String getRegisterProtocol() {
        return registerProtocol;
    }

    public void setRegisterProtocol(String registerProtocol) {
        this.registerProtocol = registerProtocol;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }

    public String getApkversion() {
        return apkversion;
    }

    public void setApkversion(String apkversion) {
        this.apkversion = apkversion;
    }
}