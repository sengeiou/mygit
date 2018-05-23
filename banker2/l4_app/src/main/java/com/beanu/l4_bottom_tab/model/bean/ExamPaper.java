package com.beanu.l4_bottom_tab.model.bean;

/**
 * 试卷
 * Created by Beanu on 2017/3/21.
 */

public class ExamPaper {

    private String id;          //  试卷Id
    private String name;        //  试卷名称
    private int already_answer; //  已作答题数
    private int total;          //  总题数
    private int answer_number;  //  作答人数
    private int difficulty;     //  难度

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

    public int getAlready_answer() {
        return already_answer;
    }

    public void setAlready_answer(int already_answer) {
        this.already_answer = already_answer;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAnswer_number() {
        return answer_number;
    }

    public void setAnswer_number(int answer_number) {
        this.answer_number = answer_number;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
