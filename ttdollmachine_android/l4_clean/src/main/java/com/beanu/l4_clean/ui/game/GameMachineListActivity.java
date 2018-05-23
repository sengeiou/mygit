package com.beanu.l4_clean.ui.game;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.arad.support.recyclerview.divider.GridLayoutWhitHeaderDecoration;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.BannerViewBinder;
import com.beanu.l4_clean.adapter.binder.DollsViewBinder;
import com.beanu.l4_clean.model.bean.Banner;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.mvp.contract.GameMachineListContract;
import com.beanu.l4_clean.mvp.model.GameMachineListModelImpl;
import com.beanu.l4_clean.mvp.presenter.GameMachineListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 机器列表
 */
public class GameMachineListActivity extends ToolBarActivity<GameMachineListPresenterImpl, GameMachineListModelImpl> implements GameMachineListContract.View {


    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.arad_content) PtrClassicFrameLayout mPtrFrame;

    MultiTypeAdapter mAdapter;
    final Items items = new Items();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_machine_list);
        ButterKnife.bind(this);

        mAdapter = new MultiTypeAdapter(items);
        mAdapter.register(Banner.class, new BannerViewBinder());
        mAdapter.register(Dolls.class, new DollsViewBinder());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0)
                    return 2;
                return 1;
            }
        });

        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.setAdapter(new LoadMoreAdapterWrapper(this, mAdapter, mPresenter));

        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });
        mRecycleView.addItemDecoration(new GridLayoutWhitHeaderDecoration(getResources().getDimensionPixelSize(R.dimen.grid_space), 2));

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Object header = items.get(0);
                items.clear();
                items.add(header);

                mPresenter.loadDataFirst();
            }
        });


        mPresenter.loadDataFirst();

    }

    @Override
    public void loadDataComplete(List<Dolls> beans) {
        items.addAll(beans);
        mAdapter.notifyDataSetChanged();
        mPtrFrame.refreshComplete();
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

    @Override
    public String setupToolBarTitle() {
        return "我要抓娃娃";
    }

    @Override
    public void uiRefreshBanner(List<BannerItem> list) {

        Banner banner = new Banner();
        banner.setItemList(list);
        items.add(0, banner);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void uiRefreshTOP(List<Dolls> pageModel) {

    }
}