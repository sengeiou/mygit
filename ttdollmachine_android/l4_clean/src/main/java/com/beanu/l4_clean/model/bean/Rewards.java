package com.beanu.l4_clean.model.bean;

/**
 * 签到
 * Created by Beanu on 2017/12/28.
 */

public class Rewards {

    private int loginTimes;//第几次签到
    private int zsCoins;//赠送金币数量
    private int day_one;//第一天金币数量
    private int day_two;//第二天金币数量
    private int day_three;//第三题金币数量
    private int isSign;//今天是否签过到 0否 1是

    public int getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(int loginTimes) {
        this.loginTimes = loginTimes;
    }

    public int getZsCoins() {
        return zsCoins;
    }

    public void setZsCoins(int zsCoins) {
        this.zsCoins = zsCoins;
    }

    public int getDay_one() {
        return day_one;
    }

    public void setDay_one(int day_one) {
        this.day_one = day_one;
    }

    public int getDay_two() {
        return day_two;
    }

    public void setDay_two(int day_two) {
        this.day_two = day_two;
    }

    public int getDay_three() {
        return day_three;
    }

    public void setDay_three(int day_three) {
        this.day_three = day_three;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }
}
