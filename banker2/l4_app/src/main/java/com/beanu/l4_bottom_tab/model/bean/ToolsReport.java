package com.beanu.l4_bottom_tab.model.bean;

/**
 * 练习报告
 * Created by Beanu on 2017/4/19.
 */

public class ToolsReport {
    private int id;//  用户ID
    private String answer_time;//  答题时长
    private double maxScore;//  最高分
    private double forecastScore;//  预测分
    private int answer_day;//  练习天数
    private int answer_total;//  答题总数
    private int pm;//  排名
    private String zql;//  正确率

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public double getForecastScore() {
        return forecastScore;
    }

    public void setForecastScore(double forecastScore) {
        this.forecastScore = forecastScore;
    }

    public int getAnswer_day() {
        return answer_day;
    }

    public void setAnswer_day(int answer_day) {
        this.answer_day = answer_day;
    }

    public int getAnswer_total() {
        return answer_total;
    }

    public void setAnswer_total(int answer_total) {
        this.answer_total = answer_total;
    }

    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public String getZql() {
        return zql;
    }

    public void setZql(String zql) {
        this.zql = zql;
    }
}
