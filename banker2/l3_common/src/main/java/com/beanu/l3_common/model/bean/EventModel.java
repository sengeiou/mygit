package com.beanu.l3_common.model.bean;

import java.util.List;

/**
 * 所有的事件分发
 * Created by Beanu on 16/9/8.
 */
public class EventModel {


    //答题卡选择事件
    public static class ExamPreviewQuesionSelectEvent {
        public int position;

        public ExamPreviewQuesionSelectEvent(int position) {
            this.position = position;
        }
    }

    //登陆事件
    public static class LoginEvent {

        public User mUser;

        public LoginEvent(User user) {
            mUser = user;
        }
    }

    //图书筛选
    public static class BookFilterEvent {

        public int type;
        public int position;

        public BookFilterEvent(int type, int position) {
            this.type = type;
            this.position = position;
        }
    }

    //购物车购买成功
    public static class CartBuySuccess {

        public CartBuySuccess() {
        }

    }

    //选择科目事件
    public static class ChangeSubject {
        public ChangeSubject() {
        }
    }



    //高清网课 课时刷新
    public static class OnlineLessonRefreshPeriod {
        public List<String> periodUrl;

        public OnlineLessonRefreshPeriod(List<String> periodUrl) {
            this.periodUrl = periodUrl;
        }
    }

    //高清网课 课时刷新
    public static class OnlineLessonRefreshPeriodInfo {
        public List<String> period;

        public OnlineLessonRefreshPeriodInfo(List<String> periodUrl) {
            this.period = periodUrl;
        }
    }

    //高清网课 课时选择
    public static class OnlineLessonChangePeriod {
        public int position;

        public OnlineLessonChangePeriod(int position) {
            this.position = position;
        }
    }

    //高清网课，选集选择 更新列表选择
    public static class OnlineLessonPeriodSelected {
        public String id;

        public OnlineLessonPeriodSelected(String id) {
            this.id = id;
        }
    }


}
