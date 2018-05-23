package com.beanu.l4_clean.model.bean;

/**
 * 订单中展示的娃娃
 */
public class ShowClaw {

    private String id;
    private String name;//娃娃名称
    private String image;//娃娃图片

    public ShowClaw() {
    }

    public ShowClaw(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}