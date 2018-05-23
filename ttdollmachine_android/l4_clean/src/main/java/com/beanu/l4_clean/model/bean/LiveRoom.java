package com.beanu.l4_clean.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 直播间
 * Created by Beanu on 2017/12/2.
 */

public class LiveRoom implements Parcelable {

    private String logId;           //本次直播ID
    private String push_flow_side;  //娃娃机侧面推流
    private String push_flow_ahead; //娃娃机正面推流
    private String live_push_url;   //主播推流地址

    private String lm_token;    //连麦token
    private String room_name;   //房间名称
    private String lm_name;     //连麦名称

    private String machineId;   //机器ID
    private String ip;          //机器IP
    private String port;        //机器端口
    private String roomId;      //直播间ID

    private String anchor_live_flow;//主播个人的播放地址
    private String anchor_push_flow;//主播个人的推流地址

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getPush_flow_side() {
        return push_flow_side;
    }

    public void setPush_flow_side(String push_flow_side) {
        this.push_flow_side = push_flow_side;
    }

    public String getPush_flow_ahead() {
        return push_flow_ahead;
    }

    public void setPush_flow_ahead(String push_flow_ahead) {
        this.push_flow_ahead = push_flow_ahead;
    }

    public String getLive_push_url() {
        return live_push_url;
    }

    public void setLive_push_url(String live_push_url) {
        this.live_push_url = live_push_url;
    }

    public String getLm_token() {
        return lm_token;
    }

    public void setLm_token(String lm_token) {
        this.lm_token = lm_token;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getLm_name() {
        return lm_name;
    }

    public void setLm_name(String lm_name) {
        this.lm_name = lm_name;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAnchor_live_flow() {
        return anchor_live_flow;
    }

    public void setAnchor_live_flow(String anchor_live_flow) {
        this.anchor_live_flow = anchor_live_flow;
    }

    public String getAnchor_push_flow() {
        return anchor_push_flow;
    }

    public void setAnchor_push_flow(String anchor_push_flow) {
        this.anchor_push_flow = anchor_push_flow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.logId);
        dest.writeString(this.push_flow_side);
        dest.writeString(this.push_flow_ahead);
        dest.writeString(this.live_push_url);
        dest.writeString(this.lm_token);
        dest.writeString(this.room_name);
        dest.writeString(this.lm_name);
        dest.writeString(this.machineId);
        dest.writeString(this.ip);
        dest.writeString(this.port);
        dest.writeString(this.roomId);
        dest.writeString(this.anchor_live_flow);
        dest.writeString(this.anchor_push_flow);
    }

    public LiveRoom() {
    }

    protected LiveRoom(Parcel in) {
        this.logId = in.readString();
        this.push_flow_side = in.readString();
        this.push_flow_ahead = in.readString();
        this.live_push_url = in.readString();
        this.lm_token = in.readString();
        this.room_name = in.readString();
        this.lm_name = in.readString();
        this.machineId = in.readString();
        this.ip = in.readString();
        this.port = in.readString();
        this.roomId = in.readString();
        this.anchor_live_flow = in.readString();
        this.anchor_push_flow = in.readString();
    }

    public static final Parcelable.Creator<LiveRoom> CREATOR = new Parcelable.Creator<LiveRoom>() {
        @Override
        public LiveRoom createFromParcel(Parcel source) {
            return new LiveRoom(source);
        }

        @Override
        public LiveRoom[] newArray(int size) {
            return new LiveRoom[size];
        }
    };
}
