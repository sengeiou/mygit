package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 高清网课
 * Created by Beanu on 2017/3/8.
 */
public class OnlineLesson implements Parcelable {

    //列表
    private String id;          //高清网课Id
    private String name;        //名称
    private int is_charges;     //是否收费 0否 1是
    private double price;       //现价
    private double original_price;//原价
    private String cover_app;   //列表图片ＵＲＬ
    private int evaluate_num;   //评价数量
    private int sale_num;       //销量
    private int state;          //课程状态  0：更新  1：完结
    private int star_rating;    //评价星级
    private String long_time;   //时长

    //详情页面
    private String introUrl;    //简介URL
    private int isBuy;          //是否购买过   0否  1是

    //时效
    private int isTime;//是否在限制时间内 0否 1是
    private String endDays;

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

    public String getLong_time() {
        return long_time;
    }

    public void setLong_time(String long_time) {
        this.long_time = long_time;
    }

    public int getIsTime() {
        return isTime;
    }

    public void setIsTime(int isTime) {
        this.isTime = isTime;
    }

    public String getEndDays() {
        return endDays;
    }

    public void setEndDays(String endDays) {
        this.endDays = endDays;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.is_charges);
        dest.writeDouble(this.price);
        dest.writeDouble(this.original_price);
        dest.writeString(this.cover_app);
        dest.writeInt(this.evaluate_num);
        dest.writeInt(this.sale_num);
        dest.writeInt(this.state);
        dest.writeInt(this.star_rating);
        dest.writeString(this.long_time);
        dest.writeString(this.introUrl);
        dest.writeInt(this.isBuy);
        dest.writeInt(this.isTime);
        dest.writeString(this.endDays);
    }

    public OnlineLesson() {
    }

    protected OnlineLesson(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.is_charges = in.readInt();
        this.price = in.readDouble();
        this.original_price = in.readDouble();
        this.cover_app = in.readString();
        this.evaluate_num = in.readInt();
        this.sale_num = in.readInt();
        this.state = in.readInt();
        this.star_rating = in.readInt();
        this.long_time = in.readString();
        this.introUrl = in.readString();
        this.isBuy = in.readInt();
        this.isTime = in.readInt();
        this.endDays = in.readString();
    }

    public static final Parcelable.Creator<OnlineLesson> CREATOR = new Parcelable.Creator<OnlineLesson>() {
        @Override
        public OnlineLesson createFromParcel(Parcel source) {
            return new OnlineLesson(source);
        }

        @Override
        public OnlineLesson[] newArray(int size) {
            return new OnlineLesson[size];
        }
    };
}