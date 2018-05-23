package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单内的 商品
 * Created by Beanu on 2017/4/7.
 */

public class OrderShop implements Parcelable {

    private int num;//购买数量
    private double price;//购买时价格
    private int is_comment;//是否评论0否1是
    private String id;//对应网课ID或者图书ID
    private String brief;//网课简介
    private String coverAPP;//图片URL
    private String timeSlot;//直播课直播时间段
    private String press;//图书出版社
    private String name;//网课、直播课、图书名称
    private String teacher;//教师
    private String orderProId;//商品 在 订单中的ID

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(int is_comment) {
        this.is_comment = is_comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getCoverAPP() {
        return coverAPP;
    }

    public void setCoverAPP(String coverAPP) {
        this.coverAPP = coverAPP;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getOrderProId() {
        return orderProId;
    }

    public void setOrderProId(String orderProId) {
        this.orderProId = orderProId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.num);
        dest.writeDouble(this.price);
        dest.writeInt(this.is_comment);
        dest.writeString(this.id);
        dest.writeString(this.brief);
        dest.writeString(this.coverAPP);
        dest.writeString(this.timeSlot);
        dest.writeString(this.press);
        dest.writeString(this.name);
        dest.writeString(this.teacher);
        dest.writeString(this.orderProId);
    }

    public OrderShop() {
    }

    protected OrderShop(Parcel in) {
        this.num = in.readInt();
        this.price = in.readDouble();
        this.is_comment = in.readInt();
        this.id = in.readString();
        this.brief = in.readString();
        this.coverAPP = in.readString();
        this.timeSlot = in.readString();
        this.press = in.readString();
        this.name = in.readString();
        this.teacher = in.readString();
        this.orderProId=in.readString();
    }

    public static final Parcelable.Creator<OrderShop> CREATOR = new Parcelable.Creator<OrderShop>() {
        @Override
        public OrderShop createFromParcel(Parcel source) {
            return new OrderShop(source);
        }

        @Override
        public OrderShop[] newArray(int size) {
            return new OrderShop[size];
        }
    };
}
