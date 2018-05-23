package com.beanu.l4_bottom_tab.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 我的 直播课
 * Created by Beanu on 2017/4/1.
 */
public class MyLiveLesson implements Parcelable {

    private String id;              //高清网课Id
    private String name;            //名称
    private int is_charges;         //是否收费 0否 1是
    private double price;           //现价
    private double original_price;  //原价
    private String cover_app;       //列表图片ＵＲＬ
    private String introUrl;        //详情url
    private int evaluate_num;       //评价数量
    private int sale_num;           //销量
    private int state;              ////0 未开课 1开播中 2 回放
    private String start_time;      //开始时间
    private String end_time;        //结束时间
    private String stop_sale;       //停售时间
    private List<TeacherIntro> teacherList;//老师列表
    private int star_rating;//评价星级

    //直播信息
//    private String code;//直播编码
//    private String stu_app_psw;//直播课学生客户端口令
//    private String stu_web_psw;//直播课学生web端口令
//    private String url;//直播URL

    //课时列表（我的课程表使用）
    private List<LiveLessonTimeTable> detailList;

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

    public int getIs_charges() {
        return is_charges;
    }

    public void setIs_charges(int is_charges) {
        this.is_charges = is_charges;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(double original_price) {
        this.original_price = original_price;
    }

    public String getCover_app() {
        return cover_app;
    }

    public void setCover_app(String cover_app) {
        this.cover_app = cover_app;
    }

    public String getIntroUrl() {
        return introUrl;
    }

    public void setIntroUrl(String introUrl) {
        this.introUrl = introUrl;
    }

    public int getEvaluate_num() {
        return evaluate_num;
    }

    public void setEvaluate_num(int evaluate_num) {
        this.evaluate_num = evaluate_num;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStop_sale() {
        return stop_sale;
    }

    public void setStop_sale(String stop_sale) {
        this.stop_sale = stop_sale;
    }

    public List<TeacherIntro> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherIntro> teacherList) {
        this.teacherList = teacherList;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }

//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getStu_app_psw() {
//        return stu_app_psw;
//    }
//
//    public void setStu_app_psw(String stu_app_psw) {
//        this.stu_app_psw = stu_app_psw;
//    }
//
//    public String getStu_web_psw() {
//        return stu_web_psw;
//    }
//
//    public void setStu_web_psw(String stu_web_psw) {
//        this.stu_web_psw = stu_web_psw;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }

    public List<LiveLessonTimeTable> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<LiveLessonTimeTable> detailList) {
        this.detailList = detailList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.is_charges);
        dest.writeDouble(this.price);
        dest.writeDouble(this.original_price);
        dest.writeString(this.cover_app);
        dest.writeString(this.introUrl);
        dest.writeInt(this.evaluate_num);
        dest.writeInt(this.sale_num);
        dest.writeInt(this.state);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeString(this.stop_sale);
        dest.writeInt(this.star_rating);
//        dest.writeString(this.code);
//        dest.writeString(this.stu_app_psw);
//        dest.writeString(this.stu_web_psw);
//        dest.writeString(this.url);
    }

    public MyLiveLesson() {
    }

    protected MyLiveLesson(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.is_charges = in.readInt();
        this.price = in.readDouble();
        this.original_price = in.readDouble();
        this.cover_app = in.readString();
        this.introUrl = in.readString();
        this.evaluate_num = in.readInt();
        this.sale_num = in.readInt();
        this.state = in.readInt();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.stop_sale = in.readString();
        this.star_rating = in.readInt();
//        this.code = in.readString();
//        this.stu_app_psw = in.readString();
//        this.stu_web_psw = in.readString();
//        this.url = in.readString();
    }

    public static final Parcelable.Creator<MyLiveLesson> CREATOR = new Parcelable.Creator<MyLiveLesson>() {
        @Override
        public MyLiveLesson createFromParcel(Parcel source) {
            return new MyLiveLesson(source);
        }

        @Override
        public MyLiveLesson[] newArray(int size) {
            return new MyLiveLesson[size];
        }
    };
}