package com.beanu.l4_clean.model.bean;

/**
 * 充值选项
 */
public class RechargeOption {

    private String rechargeId;
    private double price;
    private int currency;
    private int give_coins;//赠送免费卡数量
    private String describe_cn;//文字描述

    private boolean selected;

    public RechargeOption(int currency, double price) {
        this.currency = currency;
        this.price = price;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }


    public int getGive_coins() {
        return give_coins;
    }

    public void setGive_coins(int give_coins) {
        this.give_coins = give_coins;
    }

    public String getDescribe_cn() {
        return describe_cn;
    }

    public void setDescribe_cn(String describe_cn) {
        this.describe_cn = describe_cn;
    }
}