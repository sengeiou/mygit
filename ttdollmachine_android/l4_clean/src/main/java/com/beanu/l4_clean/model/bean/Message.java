package com.beanu.l4_clean.model.bean;

/**
 * 个人中心-消息
 * Created by Beanu on 2017/11/10.
 */

public class Message {

    private String messageId;//消息id
    private String title;//标题
    private String content;//内容
    private String createtime;//创建时间
    private int type;//消息类型0系统消息 1活动
    private String images;//消息图片
    private String url;//详情url

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}