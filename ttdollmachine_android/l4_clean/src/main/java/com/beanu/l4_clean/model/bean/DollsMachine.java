package com.beanu.l4_clean.model.bean;

/**
 * 娃娃机 详情
 * Created by Beanu on 2017/11/8.
 */
public class DollsMachine {

    private String id;//娃娃ID
    private String name;//娃娃名称
    private int game_status;//当前状态 0 空闲 1游戏中
    private int price;//价格
    private String image_cover;//图片URL、

    private String detailUrl;//描述

    private String push_flow_ahead;
    private String push_flow_side;

    private String userId;//当前正在抓的人
    private String user_icon;
    private String user_nickname;

    private String room_token_ahead;// 前方token
    private String room_token_side;// 侧面token
    private String room_name_ahead;// 前方名称
    private String room_name_side;// 侧面名称

    private int suc_num;//抓中总次数
    private String mac;//娃娃机mac地址

    private int total_num;//总抓取次数
    private String type_id;//类型ID
    private String doll_id;//娃娃ID


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

    public String getImage_cover() {
        return image_cover;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }


    public String getPush_flow_ahead() {
        return push_flow_ahead;
    }

    public void setPush_flow_ahead(String push_flow_ahead) {
        this.push_flow_ahead = push_flow_ahead;
    }

    public String getPush_flow_side() {
        return push_flow_side;
    }

    public void setPush_flow_side(String push_flow_side) {
        this.push_flow_side = push_flow_side;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getRoom_token_ahead() {
        return room_token_ahead;
    }

    public void setRoom_token_ahead(String room_token_ahead) {
        this.room_token_ahead = room_token_ahead;
    }

    public String getRoom_token_side() {
        return room_token_side;
    }

    public void setRoom_token_side(String room_token_side) {
        this.room_token_side = room_token_side;
    }

    public String getRoom_name_ahead() {
        return room_name_ahead;
    }

    public void setRoom_name_ahead(String room_name_ahead) {
        this.room_name_ahead = room_name_ahead;
    }

    public String getRoom_name_side() {
        return room_name_side;
    }

    public void setRoom_name_side(String room_name_side) {
        this.room_name_side = room_name_side;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getSuc_num() {
        return suc_num;
    }

    public void setSuc_num(int suc_num) {
        this.suc_num = suc_num;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getDoll_id() {
        return doll_id;
    }

    public void setDoll_id(String doll_id) {
        this.doll_id = doll_id;
    }
}