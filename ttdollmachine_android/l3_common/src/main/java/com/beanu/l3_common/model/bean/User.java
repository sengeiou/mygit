package com.beanu.l3_common.model.bean;

import java.io.Serializable;

/**
 * 用户信息
 * Created by Beanu on 16/2/23.
 */
public class User implements Serializable {

    private String id;//用户ID
    private String nickname;// 昵称
    private String mobile;// 手机号
    private int sex;// 性别 0男 1女
    private String icon;// 头像
    private String motto;// 个性签名
    private String invitation_code;// 我的邀请码
    private String recommend_code;// 我的推荐人邀请码
    private int coins;// 开心币剩余总额
    private int recharge_coins;// 充值开心币剩余数量
    private int give_coins;// 赠送获得开心币余额数量
    private int success_num;// 抓取成功次数
    private int invitation_num;// 我邀请的人数
    private String shareCodeUrl;// 我的推荐码分享URL
    private String rongyunToken;// 融云Token
    private int type;//0 普通用户 1主播

    private int score;//积分
    private int isSign;//今天是否签到 0否 1是
    private int sustainDay;
    private String lastSignTime;

    public boolean isLogin() {
        return (null != id && !"".equals(id));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getRecommend_code() {
        return recommend_code;
    }

    public void setRecommend_code(String recommend_code) {
        this.recommend_code = recommend_code;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getRecharge_coins() {
        return recharge_coins;
    }

    public void setRecharge_coins(int recharge_coins) {
        this.recharge_coins = recharge_coins;
    }

    public int getGive_coins() {
        return give_coins;
    }

    public void setGive_coins(int give_coins) {
        this.give_coins = give_coins;
    }

    public int getSuccess_num() {
        return success_num;
    }

    public void setSuccess_num(int success_num) {
        this.success_num = success_num;
    }

    public int getInvitation_num() {
        return invitation_num;
    }

    public void setInvitation_num(int invitation_num) {
        this.invitation_num = invitation_num;
    }

    public String getShareCodeUrl() {
        return shareCodeUrl;
    }

    public void setShareCodeUrl(String shareCodeUrl) {
        this.shareCodeUrl = shareCodeUrl;
    }

    public String getRongyunToken() {
        return rongyunToken;
    }

    public void setRongyunToken(String rongyunToken) {
        this.rongyunToken = rongyunToken;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public int getSustainDay() {
        return sustainDay;
    }

    public void setSustainDay(int sustainDay) {
        this.sustainDay = sustainDay;
    }

    public String getLastSignTime() {
        return lastSignTime;
    }

    public void setLastSignTime(String lastSignTime) {
        this.lastSignTime = lastSignTime;
    }
}
