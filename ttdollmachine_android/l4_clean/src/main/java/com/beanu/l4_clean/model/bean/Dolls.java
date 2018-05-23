package com.beanu.l4_clean.model.bean;

/**
 * 娃娃
 * Created by Beanu on 2017/11/8.
 */
public class Dolls {

    private String id;//娃娃ID
    private String name;//娃娃名称
    private String image_cover;//图片URL

    private String brief;//描述
    private int freeNum;//空闲娃娃机数量
    private int machine_num;//娃娃机数量

    //娃娃机器
    private int game_status;//当前状态 0 空闲 1游戏中
    private int price;//价格

    //娃娃类型
    private boolean isTop;

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

    public String getImage_cover() {
        return image_cover;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
    }

    public int getMachine_num() {
        return machine_num;
    }

    public void setMachine_num(int machine_num) {
        this.machine_num = machine_num;
    }

    public int getGame_status() {
        return game_status;
    }

    public void setGame_status(int game_status) {
        this.game_status = game_status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}