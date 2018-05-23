package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每个题的答案细节
 * Created by sunxiaoyu on 2017/3/22.
 */
public class AnswerRecordDetailJson implements Parcelable {
    private String questionId;//题目ID
    private String answer;//答案ID或者逗号隔开
    private String courseId;//科目ID
    private int isRealy;//是否正确 0否 1是 2未作答
    private int type;//类型

    //真题
    private int qNo;//题号

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getIsRealy() {
        return isRealy;
    }

    public void setIsRealy(int isRealy) {
        this.isRealy = isRealy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getqNo() {
        return qNo;
    }

    public void setqNo(int qNo) {
        this.qNo = qNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionId);
        dest.writeString(this.answer);
        dest.writeString(this.courseId);
        dest.writeInt(this.isRealy);
        dest.writeInt(this.type);
        dest.writeInt(this.qNo);
    }

    public AnswerRecordDetailJson() {
    }

    protected AnswerRecordDetailJson(Parcel in) {
        this.questionId = in.readString();
        this.answer = in.readString();
        this.courseId = in.readString();
        this.isRealy = in.readInt();
        this.type = in.readInt();
        this.qNo = in.readInt();
    }

    public static final Creator<AnswerRecordDetailJson> CREATOR = new Creator<AnswerRecordDetailJson>() {
        @Override
        public AnswerRecordDetailJson createFromParcel(Parcel source) {
            return new AnswerRecordDetailJson(source);
        }

        @Override
        public AnswerRecordDetailJson[] newArray(int size) {
            return new AnswerRecordDetailJson[size];
        }
    };
}
