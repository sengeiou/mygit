package com.beanu.l4_clean.model.bean;

import java.util.List;

/**
 * 已发货订单
 */
public class DeliverOrder {

    private String orderId;// 订单id
    private int status;// 状态 0未处理 1已发货 2已签收
    private String number;//  娃娃总数
    private List<ShowClaw> dollList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<ShowClaw> getDollList() {
        return dollList;
    }

    public void setDollList(List<ShowClaw> dollList) {
        this.dollList = dollList;
    }
}