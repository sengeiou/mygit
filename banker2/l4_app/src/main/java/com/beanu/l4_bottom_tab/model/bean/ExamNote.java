package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 笔记
 * Created by Beanu on 2017/4/17.
 */

public class ExamNote implements Parcelable {

    private int id;
    private String content;
    private String img_one;
    private String img_two;
    private String img_three;
    private String img_four;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_one() {
        return img_one;
    }

    public void setImg_one(String img_one) {
        this.img_one = img_one;
    }

    public String getImg_two() {
        return img_two;
    }

    public void setImg_two(String img_two) {
        this.img_two = img_two;
    }

    public String getImg_three() {
        return img_three;
    }

    public void setImg_three(String img_three) {
        this.img_three = img_three;
    }

    public String getImg_four() {
        return img_four;
    }

    public void setImg_four(String img_four) {
        this.img_four = img_four;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.content);
        dest.writeString(this.img_one);
        dest.writeString(this.img_two);
        dest.writeString(this.img_three);
        dest.writeString(this.img_four);
    }

    public ExamNote() {
    }

    protected ExamNote(Parcel in) {
        this.id = in.readInt();
        this.content = in.readString();
        this.img_one = in.readString();
        this.img_two = in.readString();
        this.img_three = in.readString();
        this.img_four = in.readString();
    }

    public static final Parcelable.Creator<ExamNote> CREATOR = new Parcelable.Creator<ExamNote>() {
        @Override
        public ExamNote createFromParcel(Parcel source) {
            return new ExamNote(source);
        }

        @Override
        public ExamNote[] newArray(int size) {
            return new ExamNote[size];
        }
    };
}
