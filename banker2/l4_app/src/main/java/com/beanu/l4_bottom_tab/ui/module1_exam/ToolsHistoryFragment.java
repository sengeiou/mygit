package com.beanu.l4_bottom_tab.ui.module1_exam;


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
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.ToolsHistory;
import com.beanu.l4_bottom_tab.adapter.provider.ToolsHistoryViewBinder;
import com.beanu.l4_bottom_tab.mvp.contract.ToolsHistoryContract;
import com.beanu.l4_bottom_tab.mvp.model.ToolsHistoryModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.ToolsHistoryPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 练习历史
 */
public class ToolsHistoryFragment extends ToolBarFragment<ToolsHistoryPresenterImpl, ToolsHistoryModelImpl> implements ToolsHistoryContract.View {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.arad_content) PtrClassicFrameLayout mPtrFrame;
    Unbinder unbinder;

    private static final String ARG_PARAM1 = "type";

    private int mType;

    private Items items;
    private MultiTypeAdapter adapter;
    private LoadMoreAdapterWrapper mAdapterWrapper;

    public ToolsHistoryFragment() {
    }

    public static ToolsHistoryFragment newInstance(int type) {
        ToolsHistoryFragment fragment = new ToolsHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_PARAM1);
        }

        //初始化ArrayList
        items = new Items();

        //初始化adapter
        adapter = new MultiTypeAdapter(items);
        adapter.register(ToolsHistory.class, new ToolsHistoryViewBinder());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //设置recycleview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mAdapterWrapper = new LoadMoreAdapterWrapper(getActivity(), adapter, mPresenter);
        mRecycleView.setAdapter(mAdapterWrapper);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(R.color.color_line).size(1).build());

        //上拉监听
        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });

        //下拉刷新监听
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadDataFirst();
            }
        });

        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("type", mType);
        mPresenter.initLoadDataParams(params);
        mPresenter.loadDataFirst();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadDataComplete(List<ToolsHistory> beans) {
        if (mType == 1) {//试卷 特殊处理
            for (ToolsHistory bean : beans) {
                bean.setType(2);
            }
        }

        items.clear();
        items.addAll(beans);

        adapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
    }
}
