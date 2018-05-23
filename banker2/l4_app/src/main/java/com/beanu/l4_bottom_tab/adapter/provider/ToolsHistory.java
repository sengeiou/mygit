package com.beanu.l4_bottom_tab.adapter.provider;

/**
 * 练习历史
 * Created by Beanu on 2017/4/25.
 */
public class ToolsHistory {

    private String id;//  答题记录Id
    private String name;//  答题记录名称
    private int answer_total;//  总答题量
    private int answer_realy;//  答对数量
    private String createtime;//  答题时间
    private String courseName;//  科目名称
    private int type;//0 专题练习 1智能练习 2试卷

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

    public int getAnswer_total() {
        return answer_total;
    }

    public void setAnswer_total(int answer_total) {
        this.answer_total = answer_total;
    }

    public int getAnswer_realy() {
        return answer_realy;
    }

    public void setAnswer_realy(int answer_realy) {
        this.answer_realy = answer_realy;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}