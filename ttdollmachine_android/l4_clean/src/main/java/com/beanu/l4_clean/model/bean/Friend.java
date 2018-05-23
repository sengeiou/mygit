package com.beanu.l4_clean.model.bean;

/**
 * 融云 朋友信息
 * Created by Beanu on 2017/11/20.
 */

public class Friend {

    private String id;      //用户ID
    private String nickname;// 昵称
    private String icon;    // 头像
    private int sex;        // 性别 0男 1女

    public Friend() {
    }

    public Friend(String id, String nickname, String icon) {
        this.id = id;
        this.nickname = nickname;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}