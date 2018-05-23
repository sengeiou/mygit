package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.ExamHistoryViewBinder;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.ExamHistory;
import com.beanu.l4_bottom_tab.mvp.contract.ToolsTestPaperContract;
import com.beanu.l4_bottom_tab.mvp.model.ToolsTestPaperModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.ToolsTestPaperPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 历年真题  作为一个下拉上拉的参考
 */
public class ToolsTestPaperActivity extends BaseSDActivity<ToolsTestPaperPresenterImpl, ToolsTestPaperModelImpl> implements ToolsTestPaperContract.View {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.arad_content) PtrClassicFrameLayout mPtrFrame;

    private Items items;
    private MultiTypeAdapter adapter;
    private LoadMoreAdapterWrapper mAdapterWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_test_paper);
        ButterKnife.bind(this);

        //初始化ArrayList
        items = new Items();

        //初始化adapter
        adapter = new MultiTypeAdapter(items);
        adapter.register(ExamHistory.class, new ExamHistoryViewBinder());

        //设置recycleview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mAdapterWrapper = new LoadMoreAdapterWrapper(this, adapter, mPresenter);
        mRecycleView.setAdapter(mAdapterWrapper);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.color_line).size(1).build());

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

        mPresenter.loadDataFirst();
    }

    @Override
    public void loadDataComplete(List<ExamHistory> beans) {
        items.clear();
        items.addAll(beans);

        adapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
    }

    @Override
    public String setupToolBarTitle() {
        return "历年真题";
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
