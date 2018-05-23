package com.beanu.l4_clean.model.bean;

/**
 * 帮助抓的人
 * Created by Beanu on 2017/12/18.
 */

public class HelpUser {

    private String users_id;//用户ID
    private String users_nickName;//昵称
    private String users_icon;//头像

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String users_id) {
        this.users_id = users_id;
    }

    public String getUsers_nickName() {
        return users_nickName;
    }

    public void setUsers_nickName(String users_nickName) {
        this.users_nickName = users_nickName;
    }

    public String getUsers_icon() {
        return users_icon;
    }

    public void setUsers_icon(String users_icon) {
        this.users_icon = users_icon;
    }
}
