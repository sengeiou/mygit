package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 订单order
 * Created by Beanu on 2017/4/6.
 */
public class OrderItem implements Parcelable {

    private String id;// 订单Id
    private double total;//  订单总金额
    private int status;// 订单状态 0 未支付 1已支付 2已发货 3已完成 4失效
    private List<OrderShop> opList;
    private String order_code;
    private String createtime;

    private int orderType;//外部使用 0直播  1网课 2图书

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderShop> getOpList() {
        return opList;
    }

    public void setOpList(List<OrderShop> opList) {
        this.opList = opList;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeDouble(this.total);
        dest.writeInt(this.status);
        dest.writeTypedList(this.opList);
        dest.writeInt(this.orderType);
        dest.writeString(this.order_code);
        dest.writeString(this.createtime);
    }

    public OrderItem() {
    }

    protected OrderItem(Parcel in) {
        this.id = in.readString();
        this.total = in.readDouble();
        this.status = in.readInt();
        this.opList = in.createTypedArrayList(OrderShop.CREATOR);
        this.orderType = in.readInt();
        this.order_code = in.readString();
        this.createtime = in.readString();
    }

    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel source) {
            return new OrderItem(source);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}