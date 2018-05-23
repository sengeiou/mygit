package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 考题 问题
 * Created by Beanu on 2017/3/21.
 */

public class ExamQuestion implements Parcelable {

    private String id;          //  题目ID
    private String stem;        //  题干
    private int type;           //  题目类型 0 单项选择题 1多项选择题 2判断题 3填空题 4主观题 5完型填空 6材料题
    private int difficulty;     //  难度系数
    private String courseId;    //  所属科目ID
    private int qNo;            //  题号
    private List<ExamOption> dataList;      //选项
    private int source;         //来源 0习题 1智能组卷 2真题
    private int collection;//是否已经收藏 0 未收藏 1已收藏

    //试卷模块
    private String mid;//模块ID
    private String name;//模块名称
    //材料题
    private String sid;//题干ID
    private String content;//题干内容


    //********解析的时候用***************
    private String answer;//所选答案
    private String analysis;    //解析
    private int isRealy;//答题结果 0错误 1正确
    private ExamNote note;//笔记
    //*********************************

    //********删除错题的时候使用*****************
    private String wid;//错题记录ID
    //*********************************

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public List<ExamOption> getDataList() {
        return dataList;
    }

    public void setDataList(List<ExamOption> dataList) {
        this.dataList = dataList;
    }


    public int getIsRealy() {
        return isRealy;
    }

    public void setIsRealy(int isRealy) {
        this.isRealy = isRealy;
    }

    public ExamNote getNote() {
        return note;
    }

    public void setNote(ExamNote note) {
        this.note = note;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getqNo() {
        return qNo;
    }

    public void setqNo(int qNo) {
        this.qNo = qNo;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.stem);
        dest.writeInt(this.type);
        dest.writeInt(this.difficulty);
        dest.writeString(this.courseId);
        dest.writeInt(this.qNo);
        dest.writeTypedList(this.dataList);
        dest.writeString(this.mid);
        dest.writeString(this.name);
        dest.writeString(this.sid);
        dest.writeString(this.content);
        dest.writeInt(this.source);
        dest.writeInt(this.collection);
    }

    public ExamQuestion() {
    }

    protected ExamQuestion(Parcel in) {
        this.id = in.readString();
        this.stem = in.readString();
        this.type = in.readInt();
        this.difficulty = in.readInt();
        this.courseId = in.readString();
        this.qNo = in.readInt();
        this.dataList = in.createTypedArrayList(ExamOption.CREATOR);
        this.mid = in.readString();
        this.name = in.readString();
        this.sid = in.readString();
        this.content = in.readString();
        this.source = in.readInt();
        this.collection = in.readInt();
    }

    public static final Parcelable.Creator<ExamQuestion> CREATOR = new Parcelable.Creator<ExamQuestion>() {
        @Override
        public ExamQuestion createFromParcel(Parcel source) {
            return new ExamQuestion(source);
        }

        @Override
        public ExamQuestion[] newArray(int size) {
            return new ExamQuestion[size];
        }
    };
}
