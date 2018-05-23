package com.beanu.l4_clean.model.bean;

import java.util.List;

/**
 * 已发货订单详情
 */
public class DeliverOrderDetail {

    private String orderId;// 订单id
    private int status;// 状态 0未处理 1已发货 2已签收
    private List<ShowClaw> dollList;
    private String expressNumber;//快递单号
    private String orderCode;//订单号
    private String linkName;//联系人名称
    private String linkPhone;//联系人电话
    private String linkAddress;//详细联系地址

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

    public List<ShowClaw> getDollList() {
        return dollList;
    }

    public void setDollList(List<ShowClaw> dollList) {
        this.dollList = dollList;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }
}