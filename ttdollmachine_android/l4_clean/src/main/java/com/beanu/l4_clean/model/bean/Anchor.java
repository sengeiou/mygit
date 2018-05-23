package com.beanu.l4_clean.model.bean;

/**
 * 主播
 * Created by Beanu on 2017/11/9.
 */

public class Anchor {

    private String anchorId;//主播ID
    private String roomId;//房间ID（聊天室ID）
    private String logId;//直播ID
    private String anchorIdNickName;//主播名称
    private String successNum;//成功抓取次数
    private String anchorCover;//封面
    private String signature;//个性签名
    private int isLive;//是否直播中 1 直播中

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAnchorIdNickName() {
        return anchorIdNickName;
    }

    public void setAnchorIdNickName(String anchorIdNickName) {
        this.anchorIdNickName = anchorIdNickName;
    }

    public String getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(String successNum) {
        this.successNum = successNum;
    }

    public String getAnchorCover() {
        return anchorCover;
    }

    public void setAnchorCover(String anchorCover) {
        this.anchorCover = anchorCover;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }
}
