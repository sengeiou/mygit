package com.beanu.l4_bottom_tab.ui.module5_my.live_lesson;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.MyLiveLessonViewBinder;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.MyLiveLesson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import com.beanu.l4_bottom_tab.util.Subscriber;


/**
 * 我的课程
 */
public class MyLiveLessonFragment extends Fragment {

    private static final String ARG_PARAM1 = "type";

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private int mParamType;//状态 0未开课 1直播中 2回放
    private MultiTypeAdapter mAdapter;
    private Items mItems;

    public MyLiveLessonFragment() {
    }

    public static MyLiveLessonFragment newInstance(int param1) {
        MyLiveLessonFragment fragment = new MyLiveLessonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamType = getArguments().getInt(ARG_PARAM1);
        }

        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(MyLiveLesson.class, new MyLiveLessonViewBinder());

        //请求网络
        API.getInstance(ApiService.class).my_live_lesson_list(API.createHeader(), String.valueOf(mParamType), null,null)
                .compose(RxHelper.<List<MyLiveLesson>>handleResult())
                .subscribe(new Subscriber<List<MyLiveLesson>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<MyLiveLesson> liveLessons) {

                        mItems.addAll(liveLessons);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_live_lesson, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
