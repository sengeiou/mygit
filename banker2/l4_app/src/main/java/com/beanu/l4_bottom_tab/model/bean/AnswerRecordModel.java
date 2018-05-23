package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 试卷 模块属性
 * Created by Beanu on 2017/4/21.
 */

public class AnswerRecordModel implements Parcelable {

    private String id;
    private String name;
    private List<AnswerRecordDetailJson> questionList;

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

    public List<AnswerRecordDetailJson> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<AnswerRecordDetailJson> questionList) {
        this.questionList = questionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.questionList);
    }

    public AnswerRecordModel() {
    }

    protected AnswerRecordModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.questionList = in.createTypedArrayList(AnswerRecordDetailJson.CREATOR);
    }

    public static final Parcelable.Creator<AnswerRecordModel> CREATOR = new Parcelable.Creator<AnswerRecordModel>() {
        @Override
        public AnswerRecordModel createFromParcel(Parcel source) {
            return new AnswerRecordModel(source);
        }

        @Override
        public AnswerRecordModel[] newArray(int size) {
            return new AnswerRecordModel[size];
        }
    };
}
