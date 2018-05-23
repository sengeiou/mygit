package com.beanu.l4_bottom_tab.model.bean;

import java.util.List;

/**
 * 直播课ITEM
 * Created by Beanu on 2017/3/7.
 */
public class LiveLesson {

    private String id;              //高清网课Id
    private String name;            //名称
    private int is_charges;         //是否收费 0否 1是
    private double price;           //现价
    private double original_price;  //原价
    private String cover_app;       //列表图片ＵＲＬ
    private String introUrl;        //详情url
    private int evaluate_num;       //评价数量
    private int sale_num;           //销量
    private int state;              //课程状态  0：更新  1：完结
    private String start_time;      //开始时间
    private String end_time;        //结束时间
    private String stop_sale;       //停售时间
    private int isBuy;              //是否已购买
    private int star_rating;        //评级

    private List<TeacherIntro> teacherList;//老师列表

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

    public int getEvaluate_num() {
        return evaluate_num;
    }

    public void setEvaluate_num(int evaluate_num) {
        this.evaluate_num = evaluate_num;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public List<TeacherIntro> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherIntro> teacherList) {
        this.teacherList = teacherList;
    }

    public String getStop_sale() {
        return stop_sale;
    }

    public void setStop_sale(String stop_sale) {
        this.stop_sale = stop_sale;
    }

    public String getIntroUrl() {
        return introUrl;
    }

    public void setIntroUrl(String introUrl) {
        this.introUrl = introUrl;
    }

    public int getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(int isBuy) {
        this.isBuy = isBuy;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }
}