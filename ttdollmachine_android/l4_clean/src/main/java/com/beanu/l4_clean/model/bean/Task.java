package com.beanu.l4_clean.model.bean;

/**
 * 任务
 * Created by Beanu on 2018/2/23.
 */

public class Task {

    private String title;
    private String coins;
    private boolean isComplete;

    private int type;//-1 什么都没有 0 玩游戏  1分享  2填写邀请码

    public Task(String title, String coins, boolean isComplete, int type) {
        this.title = title;
        this.coins = coins;
        this.isComplete = isComplete;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}