package com.beanu.l4_clean.model.bean;

/**
 * 抢占机器的结果
 * Created by Beanu on 2017/11/21.
 */

public class SeizeResult {
    private boolean result;
    private String id;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
