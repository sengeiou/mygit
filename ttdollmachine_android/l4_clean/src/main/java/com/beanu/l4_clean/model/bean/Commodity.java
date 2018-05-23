package com.beanu.l4_clean.model.bean;

/**
 * 商品
 * Created by Beanu on 2017/11/10.
 */

public class Commodity {

    private String id;//商品ID
    private String name;// 商品名称
    private int stock;// 库存剩余数量
    private String image;// 列表图片
    private int score;// 所需积分
    private String price;// 价格描述

    private String detail_imaage;
    private String url;

    private int status;//状态 0 待发货 1已发货 2已收货
    private String createtime;//兑换时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail_imaage() {
        return detail_imaage;
    }

    public void setDetail_imaage(String detail_imaage) {
        this.detail_imaage = detail_imaage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}