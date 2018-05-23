package com.beanu.l4_bottom_tab.model.bean;

import java.util.List;

/**
 * 考题 实体类(模块)
 * Created by Beanu on 2017/3/21.
 */

public class ExamEntry {

    private String id;
    private String name;
    private String describe;

    private List<ExamQuestion> questionList;

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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<ExamQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<ExamQuestion> questionList) {
        this.questionList = questionList;
    }
}
