package com.beanu.l4_clean.model.bean;

/**
 * 好友娃娃
 */
public class FirendDollsHeader {

    private String nickname;//  用户昵称
    private String icon;// 头像
    private String userId;// 用户ID
    private int sucNum;// 共抓中次数

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSucNum() {
        return sucNum;
    }

    public void setSucNum(int sucNum) {
        this.sucNum = sucNum;
    }
}