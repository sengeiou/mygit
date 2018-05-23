package com.beanu.l4_bottom_tab.adapter.provider;

/**
 * Created by Beanu on 2017/3/8.
 */
public class Online_HotLesson {

    private String id;//高清网课Id
    private String name;//名称
    private int is_charges;//是否收费 0否 1是
    private double price;//现价
    private double original_price;//原价
    private String cover_app;//列表图片ＵＲＬ
    private int star_rating;//评价星级
    private int eealuate_num;//评价数量
    private int sale_num;//销量
    private String long_time;//时长
    private int state;//课程状态  0：更新  1：完结
    private int classNum;//多少节课

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

    public int getIs_charges() {
        return is_charges;
    }

    public void setIs_charges(int is_charges) {
        this.is_charges = is_charges;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(double original_price) {
        this.original_price = original_price;
    }

    public String getCover_app() {
        return cover_app;
    }

    public void setCover_app(String cover_app) {
        this.cover_app = cover_app;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }

    public int getEealuate_num() {
        return eealuate_num;
    }

    public void setEealuate_num(int eealuate_num) {
        this.eealuate_num = eealuate_num;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public String getLong_time() {
        return long_time;
    }

    public void setLong_time(String long_time) {
        this.long_time = long_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }
}