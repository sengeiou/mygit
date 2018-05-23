package com.beanu.l4_clean.model.bean;

/**
 * 观众 看到的 直播间信息
 * Created by Beanu on 2017/12/4.
 */

public class LiveRoom2Audience {

    private String anchorId;//主播ID
    private String anchorIcon;//头像
    private String anchorNickName;//昵称
    private String live_push_url;//推流地址
    private int successNum;//成功数量
    private String anchorLiveFlow;//主播推流地址

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorIcon() {
        return anchorIcon;
    }

    public void setAnchorIcon(String anchorIcon) {
        this.anchorIcon = anchorIcon;
    }

    public String getAnchorNickName() {
        return anchorNickName;
    }

    public void setAnchorNickName(String anchorNickName) {
        this.anchorNickName = anchorNickName;
    }

    public String getLive_push_url() {
        return live_push_url;
    }

    public void setLive_push_url(String live_push_url) {
        this.live_push_url = live_push_url;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public String getAnchorLiveFlow() {
        return anchorLiveFlow;
    }

    public void setAnchorLiveFlow(String anchorLiveFlow) {
        this.anchorLiveFlow = anchorLiveFlow;
    }
}
