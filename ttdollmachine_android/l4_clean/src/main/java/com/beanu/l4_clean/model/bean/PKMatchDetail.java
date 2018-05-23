package com.beanu.l4_clean.model.bean;

/**
 * pk比赛页面 初始化信息
 * Created by Beanu on 2017/11/28.
 */

public class PKMatchDetail {

    private String userId;//发起者ID
    private String pushFlowAhead;//发起者前方推流地址
    private String pushFlowSide;//发起者侧面推流地址
    private String ip;//发起者IP
    private String port;//发起者端口
    private String oppUserId;//挑战者ID
    private String oppPushFlowAhead;//挑战者前方推流地址
    private String oppPushFlowSide;//挑战者侧面推流地址
    private String oppIp;//挑战者ip
    private String oppPort;//挑战者端口

    private String dollMaId;//发起者机器ID
    private String oppDollMaId;//挑战者机器ID
    private String gameId;//局ID

    private String room_token_ahead;//发起者前方token
    private String room_token_side;//发起者侧面token

    private String room_name_ahead;//发起者前面名字
    private String room_name_side;//发起者后面名字

    private String opp_room_token_ahead;//挑战者前方token
    private String opp_room_token_side;//挑战者侧面token

    private String opp_room_name_ahead;
    private String opp_room_name_side;
    private String mac;
    private String oppMac;

    private int price;//发起者
    private int opp_price;//挑战者


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPushFlowAhead() {
        return pushFlowAhead;
    }

    public void setPushFlowAhead(String pushFlowAhead) {
        this.pushFlowAhead = pushFlowAhead;
    }

    public String getPushFlowSide() {
        return pushFlowSide;
    }

    public void setPushFlowSide(String pushFlowSide) {
        this.pushFlowSide = pushFlowSide;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOppUserId() {
        return oppUserId;
    }

    public void setOppUserId(String oppUserId) {
        this.oppUserId = oppUserId;
    }

    public String getOppPushFlowAhead() {
        return oppPushFlowAhead;
    }

    public void setOppPushFlowAhead(String oppPushFlowAhead) {
        this.oppPushFlowAhead = oppPushFlowAhead;
    }

    public String getOppPushFlowSide() {
        return oppPushFlowSide;
    }

    public void setOppPushFlowSide(String oppPushFlowSide) {
        this.oppPushFlowSide = oppPushFlowSide;
    }

    public String getOppIp() {
        return oppIp;
    }

    public void setOppIp(String oppIp) {
        this.oppIp = oppIp;
    }

    public String getOppPort() {
        return oppPort;
    }

    public void setOppPort(String oppPort) {
        this.oppPort = oppPort;
    }

    public String getDollMaId() {
        return dollMaId;
    }

    public void setDollMaId(String dollMaId) {
        this.dollMaId = dollMaId;
    }

    public String getOppDollMaId() {
        return oppDollMaId;
    }

    public void setOppDollMaId(String oppDollMaId) {
        this.oppDollMaId = oppDollMaId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public String getOpp_room_token_ahead() {
        return opp_room_token_ahead;
    }

    public void setOpp_room_token_ahead(String opp_room_token_ahead) {
        this.opp_room_token_ahead = opp_room_token_ahead;
    }

    public String getOpp_room_token_side() {
        return opp_room_token_side;
    }

    public void setOpp_room_token_side(String opp_room_token_side) {
        this.opp_room_token_side = opp_room_token_side;
    }

    public String getOpp_room_name_ahead() {
        return opp_room_name_ahead;
    }

    public void setOpp_room_name_ahead(String opp_room_name_ahead) {
        this.opp_room_name_ahead = opp_room_name_ahead;
    }

    public String getOpp_room_name_side() {
        return opp_room_name_side;
    }

    public void setOpp_room_name_side(String opp_room_name_side) {
        this.opp_room_name_side = opp_room_name_side;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOppMac() {
        return oppMac;
    }

    public void setOppMac(String oppMac) {
        this.oppMac = oppMac;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOpp_price() {
        return opp_price;
    }

    public void setOpp_price(int opp_price) {
        this.opp_price = opp_price;
    }
}
