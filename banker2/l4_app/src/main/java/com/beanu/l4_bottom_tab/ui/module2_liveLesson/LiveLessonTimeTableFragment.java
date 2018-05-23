package com.beanu.l4_bottom_tab.ui.module2_liveLesson;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.LiveLessonTimeTableViewBinder;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.LiveLessonTimeTable;
import com.beanu.l4_bottom_tab.model.bean.MyLiveLesson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import com.beanu.l4_bottom_tab.util.Subscriber;


/**
 * 直播课 课程表
 */
public class LiveLessonTimeTableFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "lesson";


    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private String mLiveLessonId;
    private int model;//0详情页面课时列表   1我的课程
    private MyLiveLesson mMyLiveLesson;

    MultiTypeAdapter mAdapter;
    Items mItems;


    public LiveLessonTimeTableFragment() {
        // Required empty public constructor
    }

    public static LiveLessonTimeTableFragment newInstance(String liveLessonId, int model, MyLiveLesson myLiveLesson) {
        LiveLessonTimeTableFragment fragment = new LiveLessonTimeTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, liveLessonId);
        args.putInt(ARG_PARAM2, model);
        args.putParcelable(ARG_PARAM3, myLiveLesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLiveLessonId = getArguments().getString(ARG_PARAM1);
            model = getArguments().getInt(ARG_PARAM2);
            mMyLiveLesson = getArguments().getParcelable("lesson");
        }
        mItems = new Items();

        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(LiveLessonTimeTable.class, new LiveLessonTimeTableViewBinder(model, mMyLiveLesson));

        //请求网络
        API.getInstance(ApiService.class).live_lesson_time_table_list(API.createHeader(), mLiveLessonId)
                .compose(RxHelper.<List<LiveLessonTimeTable>>handleResult())
                .subscribe(new Subscriber<List<LiveLessonTimeTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<LiveLessonTimeTable> liveLessonTimeTables) {
                        mItems.addAll(liveLessonTimeTables);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_lesson_time_table, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(R.color.color_line).size(1).build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
