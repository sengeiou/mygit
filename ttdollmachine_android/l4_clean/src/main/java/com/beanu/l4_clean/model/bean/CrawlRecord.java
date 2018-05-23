package com.beanu.l4_clean.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 抓中记录
 */

public class CrawlRecord implements Parcelable {


    private String id;//用户ID
    private String name;//用户昵称
    private String image_cover;//头像地址
    private String createtime;//抓中时间
    private int isSucceed;//是否成功 0否 1是
    private String code;//游戏编号

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

    public String getImage_cover() {
        return image_cover;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(int isSucceed) {
        this.isSucceed = isSucceed;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image_cover);
        dest.writeString(this.createtime);
        dest.writeInt(this.isSucceed);
        dest.writeString(this.code);
    }

    public CrawlRecord() {
    }

    protected CrawlRecord(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image_cover = in.readString();
        this.createtime = in.readString();
        this.isSucceed = in.readInt();
        this.code = in.readString();
    }

    public static final Parcelable.Creator<CrawlRecord> CREATOR = new Parcelable.Creator<CrawlRecord>() {
        @Override
        public CrawlRecord createFromParcel(Parcel source) {
            return new CrawlRecord(source);
        }

        @Override
        public CrawlRecord[] newArray(int size) {
            return new CrawlRecord[size];
        }
    };
}
