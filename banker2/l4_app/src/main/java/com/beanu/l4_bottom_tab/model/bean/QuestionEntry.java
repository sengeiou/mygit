package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用来 初始化 每个试题
 * Created by Beanu on 2017/4/14.
 */

public class QuestionEntry implements Parcelable {

    private ExamQuestion mExamQuestion;
    private int position;//当前试卷的位置
    private String title;//试卷的名称
    private int count;//总题数
    private int exam_or_analysis;//0 做题  1分析  2错题本（选择完之后显示答案）

    public ExamQuestion getExamQuestion() {
        return mExamQuestion;
    }

    public void setExamQuestion(ExamQuestion examQuestion) {
        mExamQuestion = examQuestion;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getExam_or_analysis() {
        return exam_or_analysis;
    }

    public void setExam_or_analysis(int exam_or_analysis) {
        this.exam_or_analysis = exam_or_analysis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mExamQuestion, flags);
        dest.writeInt(this.position);
        dest.writeString(this.title);
        dest.writeInt(this.count);
        dest.writeInt(this.exam_or_analysis);
    }

    public QuestionEntry() {
    }

    protected QuestionEntry(Parcel in) {
        this.mExamQuestion = in.readParcelable(ExamQuestion.class.getClassLoader());
        this.position = in.readInt();
        this.title = in.readString();
        this.count = in.readInt();
        this.exam_or_analysis = in.readInt();
    }

    public static final Parcelable.Creator<QuestionEntry> CREATOR = new Parcelable.Creator<QuestionEntry>() {
        @Override
        public QuestionEntry createFromParcel(Parcel source) {
            return new QuestionEntry(source);
        }

        @Override
        public QuestionEntry[] newArray(int size) {
            return new QuestionEntry[size];
        }
    };
}
