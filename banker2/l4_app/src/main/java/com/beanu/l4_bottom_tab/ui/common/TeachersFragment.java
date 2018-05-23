package com.beanu.l4_bottom_tab.ui.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.TeacherIntroViewBinder;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.mvp.contract.TeachersContract;
import com.beanu.l4_bottom_tab.mvp.model.TeachersModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.TeachersPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 老师列表
 */
public class TeachersFragment extends ToolBarFragment<TeachersPresenterImpl, TeachersModelImpl> implements TeachersContract.View {

    private static final String ARG_PARAM1 = "lessonId";
    private static final String ARG_PARAM2 = "type";

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private String mLessonId;
    private int mType;//0 直播课  1 高清网课

    MultiTypeAdapter mAdapter;
    Items mItems;

    public TeachersFragment() {
        // Required empty public constructor
    }

    public static TeachersFragment newInstance(String lessonId, int type) {
        TeachersFragment fragment = new TeachersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, lessonId);
        args.putInt(ARG_PARAM2, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLessonId = getArguments().getString(ARG_PARAM1);
            mType = getArguments().getInt(ARG_PARAM2);
        }

        mItems = new Items();

        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(TeacherIntro.class, new TeacherIntroViewBinder());

        //请求数据
        mPresenter.requestHttpData(mLessonId, mType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_lesson_teachers, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(R.color.background).sizeResId(R.dimen.list_margin_8).build());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refreshListView(List<TeacherIntro> list) {
        mItems.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
