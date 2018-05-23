package com.beanu.l4_bottom_tab.ui.module_news;


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
import com.beanu.l4_bottom_tab.adapter.NewsListAdapter;
import com.beanu.l4_bottom_tab.model.bean.NewsItem;
import com.beanu.l4_bottom_tab.mvp.contract.NewsListContract;
import com.beanu.l4_bottom_tab.mvp.model.NewsListModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.NewsListPresenterImpl;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class NewsListFragment extends ToolBarFragment<NewsListPresenterImpl, NewsListModelImpl> implements NewsListContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParamId;
    private String mParamProvinceId;

    RecyclerView mRecycleView;
    PtrClassicFrameLayout mPtrFrame;
    NewsListAdapter mAdapter;

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamId = getArguments().getString(ARG_PARAM1);
            mParamProvinceId = getArguments().getString(ARG_PARAM2);
        }
        mAdapter = new NewsListAdapter(getActivity(), mPresenter.getList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化view
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.arad_content);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycle_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(R.color.color_line).build());

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

        //第一次加载数据
        if (mPresenter.getList().size() == 0) {
            ArrayMap<String, Object> params = new ArrayMap<>();
            params.put("id", mParamId);
            params.put("provinceId", mParamProvinceId);
            mPresenter.initLoadDataParams(params);
            mPresenter.loadDataFirst();
        }
    }

    @Override
    public void loadDataComplete(List<NewsItem> beans) {
        mPtrFrame.refreshComplete();
        mAdapter.notifyDataSetChanged();
    }
}
