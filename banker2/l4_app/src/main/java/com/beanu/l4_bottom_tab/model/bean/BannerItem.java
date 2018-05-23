package com.beanu.l4_bottom_tab.model.bean;

/**
 * Banner 内容 实体类
 * Created by Beanu on 2017/2/27.
 */

public class BannerItem {

    private String id;//广告ID
    private String title;//标题
    private int type;//0内部WEB页面 1外部URL 2高清网课 3直播课 4图书商城  5资讯
    private int objectId;//对应对象ID
    private String url;//链接地址
    private String imgUrl;//图片地址

    public BannerItem() {
    }

    public BannerItem(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
