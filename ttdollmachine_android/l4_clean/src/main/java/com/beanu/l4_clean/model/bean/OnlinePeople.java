package com.beanu.l4_clean.model.bean;

/**
 * 在线的人
 * Created by Beanu on 2017/11/13.
 */

public class OnlinePeople {
    private String id;
    private String userName;
    private String header;

    public OnlinePeople(String id, String userName, String header) {
        this.id = id;
        this.userName = userName;
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

