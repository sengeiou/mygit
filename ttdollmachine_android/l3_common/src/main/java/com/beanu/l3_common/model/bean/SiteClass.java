package com.beanu.l3_common.model.bean;

import java.io.Serializable;

/**
 * 场地 类型
 * Created by Beanu on 2018/3/19.
 */

public class SiteClass implements Serializable {

    private String id;
    private String name;

    private int is_limit;//是否限玩 0否 1是
    private int limit_times;//限玩次数
    private int pay_type;//付费类型 0金币 1免费卡
    private int sort;//排序值

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

    public int getIs_limit() {
        return is_limit;
    }

    public void setIs_limit(int is_limit) {
        this.is_limit = is_limit;
    }

    public int getLimit_times() {
        return limit_times;
    }

    public void setLimit_times(int limit_times) {
        this.limit_times = limit_times;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}