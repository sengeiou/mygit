package com.beanu.l4_bottom_tab.model.bean;

/**
 * 新闻标题
 * Created by Beanu on 2017/3/7.
 */

public class NewsTitle {

    private String name;
    private String id;
    private int sort;

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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
