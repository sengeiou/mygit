package com.beanu.l4_bottom_tab.model.bean;

/**
 * 老师介绍
 * Created by Beanu on 2017/3/30.
 */
public class TeacherIntro {
    private String id;//    教师ID
    private String name;//    教师名称
    private String intro;//    教师富文本
    private String head_portrait;//    头像URL
    private int star_rating;//    评价星级总数
    private int evaluate_num;//    评价人数
    private int teaching_time;//授课时长

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }

    public int getEvaluate_num() {
        return evaluate_num;
    }

    public void setEvaluate_num(int evaluate_num) {
        this.evaluate_num = evaluate_num;
    }

    public int getTeaching_time() {
        return teaching_time;
    }

    public void setTeaching_time(int teaching_time) {
        this.teaching_time = teaching_time;
    }
}