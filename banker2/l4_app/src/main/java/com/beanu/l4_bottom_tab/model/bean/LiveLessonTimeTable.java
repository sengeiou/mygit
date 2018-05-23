package com.beanu.l4_bottom_tab.model.bean;

/**
 * 直播课 课程表
 * Created by Beanu on 2017/3/30.
 */
public class LiveLessonTimeTable {
    private int id;//课时ID
    private String name;//课时名称
    private int rcId;//网课ID
    private int sort;//排序值
    private String longTime;//时长
    private String liveTime;//直播开始时间
    private int state;//是否直播完成转录播 0否 1是

    private String sdkId;//录播ID
    private String code;//录播密码

    private String liveCode;//直播编号
    private String stuAppPsw;//直播密码
//    private String recordingUrl;//录播URL

    private String end_time;//新加 展示时间用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRcId() {
        return rcId;
    }

    public void setRcId(int rcId) {
        this.rcId = rcId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getSdkId() {
        return sdkId;
    }

    public void setSdkId(String sdkId) {
        this.sdkId = sdkId;
    }

//    public String getRecordingUrl() {
//        return recordingUrl;
//    }
//
//    public void setRecordingUrl(String recordingUrl) {
//        this.recordingUrl = recordingUrl;
//    }


    public String getLiveCode() {
        return liveCode;
    }

    public void setLiveCode(String liveCode) {
        this.liveCode = liveCode;
    }

    public String getStuAppPsw() {
        return stuAppPsw;
    }

    public void setStuAppPsw(String stuAppPsw) {
        this.stuAppPsw = stuAppPsw;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}