package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图书item
 * Created by Beanu on 2017/3/9.
 */

public class BookItem implements Parcelable {

    private String id;
    private String detailUrl;//详情说明URL
    private String list_img;//列表图片URL
    private String press;//出版社
    private double price;
    private double original_price;
    private String name;
    private int is_postage;//是否包邮 0否 1是
    private int star_rating;//评价总星级
    private int evaluate_num;//评价总人数
    private double postage;//邮费
    private int sold_num;//已出售数量
    private String pay_url;//淘宝购买地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getList_img() {
        return list_img;
    }

    public void setList_img(String list_img) {
        this.list_img = list_img;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_postage() {
        return is_postage;
    }

    public void setIs_postage(int is_postage) {
        this.is_postage = is_postage;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }

    public int getEvaluate_num() {
        return evaluate_num;
    }

    public void setEvaluate_num(int evaluate_num) {
        this.evaluate_num = evaluate_num;
    }

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public int getSold_num() {
        return sold_num;
    }

    public void setSold_num(int sold_num) {
        this.sold_num = sold_num;
    }

    public String getPay_url() {
        return pay_url;
    }

    public void setPay_url(String pay_url) {
        this.pay_url = pay_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.detailUrl);
        dest.writeString(this.list_img);
        dest.writeString(this.press);
        dest.writeDouble(this.price);
        dest.writeDouble(this.original_price);
        dest.writeString(this.name);
        dest.writeInt(this.is_postage);
        dest.writeInt(this.star_rating);
        dest.writeInt(this.evaluate_num);
        dest.writeDouble(this.postage);
        dest.writeInt(this.sold_num);
        dest.writeString(this.pay_url);
    }

    public BookItem() {
    }

    protected BookItem(Parcel in) {
        this.id = in.readString();
        this.detailUrl = in.readString();
        this.list_img = in.readString();
        this.press = in.readString();
        this.price = in.readDouble();
        this.original_price = in.readDouble();
        this.name = in.readString();
        this.is_postage = in.readInt();
        this.star_rating = in.readInt();
        this.evaluate_num = in.readInt();
        this.postage = in.readDouble();
        this.sold_num = in.readInt();
        this.pay_url = in.readString();
    }

    public static final Parcelable.Creator<BookItem> CREATOR = new Parcelable.Creator<BookItem>() {
        @Override
        public BookItem createFromParcel(Parcel source) {
            return new BookItem(source);
        }

        @Override
        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };
}
