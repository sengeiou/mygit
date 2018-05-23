package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 新闻ITEM
 * Created by Beanu on 2017/3/7.
 */

public class NewsItem implements Parcelable {

    private int id;//学科Id
    private String imgUrl;//图片地址
    private String title;//标题
    private String url;//详情链接地址
    private String showTime;//展示时间
    private int click;//点击次数
    private String author;//作者

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.imgUrl);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.showTime);
        dest.writeInt(this.click);
        dest.writeString(this.author);
    }

    public NewsItem() {
    }

    protected NewsItem(Parcel in) {
        this.id = in.readInt();
        this.imgUrl = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.showTime = in.readString();
        this.click = in.readInt();
        this.author = in.readString();
    }

    public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel source) {
            return new NewsItem(source);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };
}
