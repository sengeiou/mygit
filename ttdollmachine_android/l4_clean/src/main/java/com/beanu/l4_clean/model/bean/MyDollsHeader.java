package com.beanu.l4_clean.model.bean;

/**
 * Created by Beanu on 2017/11/10.
 */
public class MyDollsHeader {

    private String total;

    public MyDollsHeader() {
    }

    public MyDollsHeader(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}