package com.beanu.l4_bottom_tab.ui.module5_my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.BookViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.LiveLessonViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.OnlineLessonViewProvider;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.mvp.contract.RecommendContract;
import com.beanu.l4_bottom_tab.mvp.model.RecommendModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.RecommendPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 我的推荐
 */
public class RecommendFragment extends ToolBarFragment<RecommendPresenterImpl, RecommendModelImpl> implements RecommendContract.View {

    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private int mPosition;
    private String mParam2;

    Items mItems;
    MultiTypeAdapter mMultiAdapter;
    LoadMoreAdapterWrapper mAdapterWrapper;

    public RecommendFragment() {
    }

    public static RecommendFragment newInstance(int position, String param2) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mItems = new Items();
        mMultiAdapter = new MultiTypeAdapter(mItems);
        mMultiAdapter.register(LiveLesson.class, new LiveLessonViewProvider());
        mMultiAdapter.register(OnlineLesson.class, new OnlineLessonViewProvider());
        mMultiAdapter.register(BookItem.class, new BookViewProvider());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recomment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 2);
        mAdapterWrapper = new LoadMoreAdapterWrapper(getActivity(), mMultiAdapter, mPresenter);

        mRecycleView.setAdapter(mAdapterWrapper);

        switch (mPosition) {
            case 0:
                mRecycleView.setLayoutManager(linearManager);
                break;
            case 1:
                mRecycleView.setLayoutManager(gridManager);
                break;
            case 2:
                mRecycleView.setLayoutManager(linearManager);
                break;
        }

        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("position", mPosition);
        params.put("subjectId", AppHolder.getInstance().user.getSubjectId());
        mPresenter.initLoadDataParams(params);
        mPresenter.loadDataFirst();
    }

    @Override
    public void loadDataComplete(List<Object> beans) {

        mItems.clear();
        mItems.addAll(beans);

        mMultiAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
