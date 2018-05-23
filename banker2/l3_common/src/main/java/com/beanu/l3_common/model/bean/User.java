package com.beanu.l3_common.model.bean;

import java.io.Serializable;

/**
 * 用户信息
 * Created by Beanu on 16/2/23.
 */
public class User implements Serializable {

    private String id;          //  用户ID
    private String account;     //  账号
    private String mobile;      //  手机号
    private int sex;            //  性别 0 男 1女
    private String email;       //  邮箱
    private String icon;        //  头像URL
    private String provinceId;  //  省份ID
    private String cityId;      //  城市ID
    private String motto;       //  个性签名
    private String subjectId;   //  学科ID
    private String subjectCn;   //  学科名称
    private String nickname;    //  昵称
    private int level;          //  等级
    private String school;      //  学校

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCn() {
        return subjectCn;
    }

    public void setSubjectCn(String subjectCn) {
        this.subjectCn = subjectCn;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
