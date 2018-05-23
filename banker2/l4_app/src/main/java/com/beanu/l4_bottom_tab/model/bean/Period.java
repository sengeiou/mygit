package com.beanu.l4_bottom_tab.model.bean;

/**
 * 高清网课 课时
 * Created by Beanu on 2017/3/31.
 */
public class Period {

    String id;              //课时ID
    String name;            //课时名称
    String rcId;            //节ID
    int sort;               //排序值
    String longTime;        //时长
    String recordingUrl;     //URL
    String teacher;         //主讲老师
    private int isTry;      //是否试看  0否 1是
    private boolean isPlaying;//是否正在播放

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

    public String getRcId() {
        return rcId;
    }

    public void setRcId(String rcId) {
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

    public String getRecordingUrl() {
        return recordingUrl;
    }

    public void setRecordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
    }

    public int getIsTry() {
        return isTry;
    }

    public void setIsTry(int isTry) {
        this.isTry = isTry;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}