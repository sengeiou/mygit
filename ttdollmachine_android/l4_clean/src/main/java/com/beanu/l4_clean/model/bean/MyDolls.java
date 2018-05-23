package com.beanu.l4_clean.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的娃娃
 * Created by Beanu on 2017/11/10.
 */

public class MyDolls implements Parcelable {

    private boolean checked;

    private String id;
    private String createtime;//创建时间
    private String name;//娃娃名称
    private String image;//娃娃图片

    //暂时还未用到
    private String logId;//记录id
    private String nickname;//用户名称
    private String icon;//用户头像
    private int total;//共抓中次数
    private int coins;//可兑换的金币数量

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
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

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.createtime);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.logId);
        dest.writeString(this.nickname);
        dest.writeString(this.icon);
        dest.writeInt(this.total);
        dest.writeInt(this.coins);
    }

    public MyDolls() {
    }

    protected MyDolls(Parcel in) {
        this.checked = in.readByte() != 0;
        this.id = in.readString();
        this.createtime = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.logId = in.readString();
        this.nickname = in.readString();
        this.icon = in.readString();
        this.total = in.readInt();
        this.coins = in.readInt();
    }

    public static final Parcelable.Creator<MyDolls> CREATOR = new Parcelable.Creator<MyDolls>() {
        @Override
        public MyDolls createFromParcel(Parcel source) {
            return new MyDolls(source);
        }

        @Override
        public MyDolls[] newArray(int size) {
            return new MyDolls[size];
        }
    };
}
