package com.beanu.l4_bottom_tab.model.bean;

import java.util.ArrayList;

/**
 * 学科  分类
 * Created by Beanu on 2017/2/24.
 */

public class Subject {

    private String id;
    private String name;
    private String icoUrl;
    private ArrayList<Subject> secSubList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcoUrl() {
        return icoUrl;
    }

    public void setIcoUrl(String icoUrl) {
        this.icoUrl = icoUrl;
    }

    public ArrayList<Subject> getSecSubList() {
        return secSubList;
    }

    public void setSecSubList(ArrayList<Subject> secSubList) {
        this.secSubList = secSubList;
    }
}
