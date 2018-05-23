package com.beanu.l4_clean.model.bean;

/**
 * 排行榜
 * Created by Beanu on 2018/2/11.
 */

public class RankPeople {

    private String userId;      //用户ID
    private String nickname;    //昵称
    private String icon;        //头像
    private int num;            //抓中次数

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
