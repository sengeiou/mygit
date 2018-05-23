package com.beanu.l4_bottom_tab.ui.module3_onlineLesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.PeriodViewBinder;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.Period;
import com.beanu.l4_bottom_tab.ui.common.LessonPayActivity;
import com.beanu.l4_bottom_tab.util.Subscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 高清网课 课时列表
 */
public class OnlineLessonPeriodListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private String mLessonId;
    private OnlineLesson mOnlineLesson;


    MultiTypeAdapter periodAdapter;
    Items mPeriodItems;

    public OnlineLessonPeriodListFragment() {
        // Required empty public constructor
    }

    public static OnlineLessonPeriodListFragment newInstance(String param1, OnlineLesson param2) {
        OnlineLessonPeriodListFragment fragment = new OnlineLessonPeriodListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLessonId = getArguments().getString(ARG_PARAM1);
            mOnlineLesson = getArguments().getParcelable(ARG_PARAM2);
        }

        //初始化课时
        mPeriodItems = new Items();

        periodAdapter = new MultiTypeAdapter(mPeriodItems);
        periodAdapter.register(Period.class, new PeriodViewBinder(mOnlineLesson, new PeriodViewBinder.PeriodClickEvent() {
            @Override
            public void onClick(int position) {

                 //课时的position的点击事件

                if (mOnlineLesson != null && mOnlineLesson.getIsTime() == 1) {
                    for (Object periodItem : mPeriodItems) {
                        if (periodItem instanceof Period) {
                            ((Period) periodItem).setPlaying(false);
                        }
                    }

                    ((Period) mPeriodItems.get(position)).setPlaying(true);
                    periodAdapter.notifyDataSetChanged();

                    int real_position = getRealPosition(position);
                    Arad.bus.post(new EventModel.OnlineLessonChangePeriod(real_position));
                } else {
                    ToastUtils.showShort( "网课已到期");
                }

            }



            @Override
            public void onPay() {

                if (mOnlineLesson != null && mOnlineLesson.getIsTime() == 1) {
                    //购买课程
                    OnlineLesson onlineLesson = mOnlineLesson;
                    //跳转到 支付页面
                    if (onlineLesson != null) {
                        Intent intent = new Intent(getActivity(), LessonPayActivity.class);
                        intent.putExtra("orderType", 1);
                        intent.putExtra("lessonId", onlineLesson.getId());
                        intent.putExtra("title", onlineLesson.getName());
                        intent.putExtra("time", String.format("精品录播课（%s节）", mPeriodItems.size()));
                        intent.putExtra("price", onlineLesson.getPrice());
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.showShort("网课已到期");

                }


            }
        }));

        //请求数据
        API.getInstance(ApiService.class).online_lesson_period_list(API.createHeader(), mLessonId)
                .compose(RxHelper.<List<Period>>handleResult())
                .subscribe(new Subscriber<List<Period>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Period> periods) {
                        mPeriodItems.addAll(periods);
                        periodAdapter.notifyDataSetChanged();

                        //刷新video的url
                        ArrayList<String> _list = new ArrayList<>();
                        ArrayList<String> _listInfo = new ArrayList<>();



                        if (isUserCharged()) {
                            //用户已购买
                            for (Period period : periods) {
                                _list.add(period.getRecordingUrl());
                                _listInfo.add(period.getName() + "," + period.getTeacher() + "," + period.getLongTime());
                            }

                        } else {
                            //未购买
                            for (Period period : periods) {
                                if (period.getIsTry() == 1) {
                                    _list.add(period.getRecordingUrl());
                                    _listInfo.add(period.getName() + "," + period.getTeacher() + "," + period.getLongTime());

                                }
                            }
                        }

                        ////把所有课时的URL以list的形式传到activity中
                        Arad.bus.post(new EventModel.OnlineLessonRefreshPeriod(_list));
                        Arad.bus.post(new EventModel.OnlineLessonRefreshPeriodInfo(_listInfo));







                    }
                });

        //事件监听
        Arad.bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_lesson_period_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(R.color.color_line).size(1).build());
        mRecycleView.setAdapter(periodAdapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPeriodSelected(EventModel.OnlineLessonPeriodSelected event) {
        int pos = getRealPositionById(event.id);

        if (mOnlineLesson != null && mOnlineLesson.getIsTime() == 1) {
            for (Object periodItem : mPeriodItems) {
                if (periodItem instanceof Period) {
                    ((Period) periodItem).setPlaying(false);
                }
            }

            Period period = (Period) mPeriodItems.get(pos);
            period.setPlaying(true);
            periodAdapter.notifyDataSetChanged();

        } else {
            ToastUtils.showShort("网课已到期");
        }


    }


    //根据id获得位置信息
    private int getRealPositionById(String id) {
        int position = 0;
        for (int i = 0; i < mPeriodItems.size(); i++) {
            if (id.equals(((Period) mPeriodItems.get(i)).getRecordingUrl())) {
                position = i;
                break;
            }
        }

        return position;
    }


    //根据点击位置 获取真实的位置
    private int getRealPosition(int position) {
        if (isUserCharged()) {
            return position;
        } else {

            int currentPosition = -1;
            for (int i = 0; i <= position; i++) {
                Object item = mPeriodItems.get(i);
                if (item instanceof Period) {
                    if (((Period) item).getIsTry() == 1) {
                        currentPosition++;
                    }
                }
            }

            KLog.d("真实位置：" + currentPosition);
            return currentPosition;
        }
    }

    private boolean isUserCharged() {
        if (mOnlineLesson != null) {
            if (mOnlineLesson.getIs_charges() == 0 || mOnlineLesson.getIsBuy() == 1) {
                return true;
            }
        }
        return false;
    }
}