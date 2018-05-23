package com.beanu.l4_bottom_tab.model.bean;

import java.util.List;

/**
 * 首页的科目  分多级
 * Created by Beanu on 2017/2/27.
 */
public class Course {

    private String id;
    private String name;
    private String icoUrl;
    private int total;
    private int answerNum;
    private int parentid;

    private List<Course> list;

    //工具箱专用（错题本 收藏 练习历史等）
    private int num;        //错题数量
    private int children;   //是否还有子集，大于0表示还有

    //本地自用
    private int childLevel = 0;
    private boolean isExpand;//是否已展开，一级目录只能展开一次


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }

    public int getChildLevel() {
        return childLevel;
    }

    public void setChildLevel(int childLevel) {
        this.childLevel = childLevel;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public String getIcoUrl() {
        return icoUrl;
    }

    public void setIcoUrl(String icoUrl) {
        this.icoUrl = icoUrl;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }
}