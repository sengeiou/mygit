package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 答案记录 需要上传
 * Created by sunxiaoyu on 2017/3/22.
 */
public class AnswerRecordJson implements Parcelable {

    private String paperId;//真题试卷ID 只有type=2真题的时候传
    private String courseId;//科目ID  只有type=0的时候传,为了记录当前试卷的名称
    private int type;//类型0练习 1智能组卷
    private int isFinish;//是否完成 0否
    private String answerTime;//答题时间 秒
    private int answerTotal;//总题数
    private int answerRealy;//答对数量
    private List<AnswerRecordDetailJson> ardj;//详情json

    //==================以上内容需要序列化=========================

    //练习报告页面需要
    private String id;
    private String courseName;
    private String difficulty;
    private String zql;
    private String submitTime;
    //练习报告中 只有试卷的时候需要
    private List<AnswerRecordModel> modelList;//试卷模块列表


    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public List<AnswerRecordDetailJson> getArdj() {
        return ardj;
    }

    public int getAnswerTotal() {
        return answerTotal;
    }

    public void setAnswerTotal(int answerTotal) {
        this.answerTotal = answerTotal;
    }

    public int getAnswerRealy() {
        return answerRealy;
    }

    public void setAnswerRealy(int answerRealy) {
        this.answerRealy = answerRealy;
    }

    public void setArdj(List<AnswerRecordDetailJson> ardj) {
        this.ardj = ardj;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getZql() {
        return zql;
    }

    public void setZql(String zql) {
        this.zql = zql;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AnswerRecordModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<AnswerRecordModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paperId);
        dest.writeString(this.courseId);
        dest.writeInt(this.type);
        dest.writeInt(this.isFinish);
        dest.writeString(this.answerTime);
        dest.writeInt(this.answerTotal);
        dest.writeInt(this.answerRealy);
        dest.writeTypedList(this.ardj);
    }

    public AnswerRecordJson() {
    }

    protected AnswerRecordJson(Parcel in) {
        this.paperId = in.readString();
        this.courseId = in.readString();
        this.type = in.readInt();
        this.isFinish = in.readInt();
        this.answerTime = in.readString();
        this.answerTotal = in.readInt();
        this.answerRealy = in.readInt();
        this.ardj = in.createTypedArrayList(AnswerRecordDetailJson.CREATOR);
    }

    public static final Parcelable.Creator<AnswerRecordJson> CREATOR = new Parcelable.Creator<AnswerRecordJson>() {
        @Override
        public AnswerRecordJson createFromParcel(Parcel source) {
            return new AnswerRecordJson(source);
        }

        @Override
        public AnswerRecordJson[] newArray(int size) {
            return new AnswerRecordJson[size];
        }
    };
}
