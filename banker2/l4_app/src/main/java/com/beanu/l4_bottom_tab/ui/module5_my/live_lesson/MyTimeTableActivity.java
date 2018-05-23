package com.beanu.l4_bottom_tab.ui.module5_my.live_lesson;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.MyLiveLessonViewBinder;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.LiveLessonTimeTable;
import com.beanu.l4_bottom_tab.model.bean.MyLiveLesson;
import com.jmavarez.materialcalendar.CalendarView;
import com.jmavarez.materialcalendar.Interface.OnDateChangedListener;
import com.jmavarez.materialcalendar.Interface.OnMonthChangedListener;
import com.jmavarez.materialcalendar.Util.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import io.reactivex.Observable;
import com.beanu.l4_bottom_tab.util.Subscriber;

import static com.beanu.l4_bottom_tab.R.id.calendarView;

/**
 * 我的课程表
 */
public class MyTimeTableActivity extends BaseSDActivity {

    @BindView(calendarView) CalendarView mCalendarView;
    @BindView(R.id.recycle_view) RecyclerView mRecycleView;

    HashSet<CalendarDay> calendarDays;

    MultiTypeAdapter mAdapter;
    Items mItems;//显示当先选中天的数据列表

    List<MyLiveLesson> mList;//保存这个月的所有数据
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_time_table);
        ButterKnife.bind(this);

        //日历标示已有事件
        calendarDays = new HashSet<>();
        CalendarDay calendarDay = CalendarDay.from(new Date());

//        for (int i = 1; i < CalendarUtils.getEndOfMonth(calendarDay.getCalendar()) + 1; i++) {
//            if (i % 3 == 0) {
//                CalendarDay day = CalendarDay.from(i, calendarDay.getMonth() + 1, calendarDay.getYear());
//                calendarDays.add(day);
//            }
//        }
//        mCalendarView.addEvents(calendarDays);

        //日历监听
        mCalendarView.setOnDateChangedListener(new OnDateChangedListener() {
            @Override
            public void onDateChanged(Date date) {
                String dddd = new SimpleDateFormat("yyyy-MM-dd").format(date);

                List<MyLiveLesson> dayList = new ArrayList<>();
                for (MyLiveLesson lesson : mList) {
                    for (LiveLessonTimeTable timeTable : lesson.getDetailList()) {
                        if (timeTable.getLiveTime().contains(dddd)) {
                            dayList.add(lesson);
                        }
                    }
                }

                mItems.clear();
                mItems.addAll(dayList);
                mAdapter.notifyDataSetChanged();

            }
        });

        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(Date date) {
                String month = new SimpleDateFormat("yyyy-MM").format(date);
                requestLessons(month);
            }
        });


        //事件列表
        mList = new ArrayList<>();
        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(MyLiveLesson.class, new MyLiveLessonViewBinder());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mAdapter);

        //请求当月的数据
        int month = calendarDay.getMonth() + 1;
        String sMonth = (month < 10) ? ("0" + month) : ("" + month);
        requestLessons(calendarDay.getYear() + "-" + sMonth);
    }


    //获取这个月有哪些课程
    private void requestLessons(String date) {

        API.getInstance(ApiService.class).my_live_lesson_list(API.createHeader(), null, null, date)
                .compose(RxHelper.<List<MyLiveLesson>>handleResult())
                .subscribe(new Subscriber<List<MyLiveLesson>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<MyLiveLesson> myLiveLessons) {

                        mList.clear();
                        mList.addAll(myLiveLessons);

                        mItems.clear();
                        mItems.addAll(myLiveLessons);
                        mAdapter.notifyDataSetChanged();

                        calendarDays.clear();

                        //1.通过我的课程列表，获取所有的课时
                        List<LiveLessonTimeTable> timeTableList = new ArrayList<>();
                        for (MyLiveLesson lesson : myLiveLessons) {
                            timeTableList.addAll(lesson.getDetailList());
                        }

                        //2.通过日期组合在一起
                        Observable.fromIterable(timeTableList)
                                .groupBy(new Function<LiveLessonTimeTable, String>() {
                                    @Override
                                    public String apply(LiveLessonTimeTable liveLessonTimeTable) throws Exception {
                                        return liveLessonTimeTable.getLiveTime().substring(0, 10);
                                    }
                                })
                                .subscribe(new Subscriber<GroupedObservable<String, LiveLessonTimeTable>>() {
                                    @Override
                                    public void onCompleted() {
                                        mCalendarView.addEvents(calendarDays);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(GroupedObservable<String, LiveLessonTimeTable> groupedObservable) {

                                        try {
                                            Date date1 = sdf.parse(groupedObservable.getKey());
                                            CalendarDay day1 = CalendarDay.from(date1);
                                            CalendarDay day = CalendarDay.from(day1.getDay(), day1.getMonth() + 1, day1.getYear());

                                            calendarDays.add(day);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });


                    }
                });
    }

    @Override
    public String setupToolBarTitle() {
        return "课程表";
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }
}
