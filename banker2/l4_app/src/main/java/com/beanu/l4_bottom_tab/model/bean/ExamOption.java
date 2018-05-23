package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 考题 选项
 * Created by Beanu on 2017/3/21.
 */

public class ExamOption implements Parcelable {

    private String id;      //    选项ID
    private String content; //    选项内容
    private String name;    //    选项名称 A、B...
    private int is_correct; //    是否正确答案 0否 1是
    private int choice_num; //    选择此项的人数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(int is_correct) {
        this.is_correct = is_correct;
    }

    public int getChoice_num() {
        return choice_num;
    }

    public void setChoice_num(int choice_num) {
        this.choice_num = choice_num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.name);
        dest.writeInt(this.is_correct);
        dest.writeInt(this.choice_num);
    }

    public ExamOption() {
    }

    protected ExamOption(Parcel in) {
        this.id = in.readString();
        this.content = in.readString();
        this.name = in.readString();
        this.is_correct = in.readInt();
        this.choice_num = in.readInt();
    }

    public static final Parcelable.Creator<ExamOption> CREATOR = new Parcelable.Creator<ExamOption>() {
        @Override
        public ExamOption createFromParcel(Parcel source) {
            return new ExamOption(source);
        }

        @Override
        public ExamOption[] newArray(int size) {
            return new ExamOption[size];
        }
    };
}
