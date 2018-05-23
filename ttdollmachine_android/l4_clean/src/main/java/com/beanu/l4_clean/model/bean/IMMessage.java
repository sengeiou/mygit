package com.beanu.l4_clean.model.bean;

/**
 * 聊天消息
 * Created by Beanu on 2017/11/14.
 */

public class IMMessage {

    private String userName;
    private String content;

    public IMMessage(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
