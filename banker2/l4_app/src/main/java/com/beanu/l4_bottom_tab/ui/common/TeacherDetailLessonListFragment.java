package com.beanu.l4_bottom_tab.ui.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.TeacherWithLiveLessonViewProvider;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.mvp.contract.TeacherLessonListContract;
import com.beanu.l4_bottom_tab.mvp.model.TeacherLessonListModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.TeacherLessonListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 老师详情页面-在售课程
 */
public class TeacherDetailLessonListFragment extends ToolBarFragment<TeacherLessonListPresenterImpl, TeacherLessonListModelImpl> implements TeacherLessonListContract.View {

    private static final String ARG_PARAM1 = "teacherId";

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private String mTeacherId;

    MultiTypeAdapter mAdapter;
    Items mItems;

    public TeacherDetailLessonListFragment() {
        // Required empty public constructor
    }

    public static TeacherDetailLessonListFragment newInstance(String teacherId) {
        TeacherDetailLessonListFragment fragment = new TeacherDetailLessonListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, teacherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeacherId = getArguments().getString(ARG_PARAM1);
        }

        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(LiveLesson.class, new TeacherWithLiveLessonViewProvider());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_detail_lesson_list, container, false);
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

        //上拉监听
        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });

        //第一次加载数据
        if (mPresenter.getList().size() == 0) {

            ArrayMap<String, Object> params = new ArrayMap<>();
            params.put("id", mTeacherId);

            mPresenter.initLoadDataParams(params);
            mPresenter.loadDataFirst();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadDataComplete(List<LiveLesson> beans) {
        mItems.clear();
        mItems.addAll(beans);
        mAdapter.notifyDataSetChanged();
    }
}
