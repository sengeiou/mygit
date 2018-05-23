package com.beanu.l4_clean.ui.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.DollsHotViewBinder;
import com.beanu.l4_clean.adapter.binder.DollsViewBinder;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.mvp.contract.GameMachineListContract;
import com.beanu.l4_clean.mvp.model.GameMachineListModelImpl;
import com.beanu.l4_clean.mvp.presenter.GameMachineListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.Linker;
import me.drakeet.multitype.MultiTypeAdapter;


public class DollListFragment extends ToolBarFragment<GameMachineListPresenterImpl, GameMachineListModelImpl> implements GameMachineListContract.View {

    private static final String ARG_PARAM1 = "paramType";

    private String mParamType;

    @BindView(R.id.arad_content) RecyclerView mRecycleView;
    Unbinder unbinder;

    private Items items;
    private MultiTypeAdapter mAdapter;
    private boolean isRequestTop;


    public DollListFragment() {
        // Required empty public constructor
    }


    public static DollListFragment newInstance(String param1) {
        DollListFragment fragment = new DollListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamType = getArguments().getString(ARG_PARAM1);
        }

        items = new Items();

        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Dolls.class).to(new DollsViewBinder(), new DollsHotViewBinder()).withLinker(new Linker<Dolls>() {
            @Override
            public int index(int position, @NonNull Dolls dolls) {
                if (dolls.isTop()) {
                    return 1;
                } else {
                    return 0;
                }

            }
        });
        mAdapter.setItems(items);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doll_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (position < items.size() && ((Dolls) items.get(position)).isTop())
                    return 2;
                else
                    return 1;
            }
        });

        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.setAdapter(new LoadMoreAdapterWrapper(getActivity(), mAdapter, mPresenter));

        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });
        mRecycleView.addItemDecoration(new GridLayoutItemDecoration(getResources().getDimensionPixelSize(R.dimen.grid_space), 2));

        if (items.size() == 0) {

            ArrayMap<String, Object> params = new ArrayMap<>();
            params.put("typeId", mParamType);
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
    public void uiRefreshBanner(List<BannerItem> list) {

    }

    @Override
    public void uiRefreshTOP(List<Dolls> pageModel) {

        if (items.size() >= 4) {
            int position = 4;
            for (Dolls dolls : pageModel) {
                items.add(position, dolls);
                position++;
            }
        } else {
            items.addAll(pageModel);
        }

        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void loadDataComplete(List<Dolls> beans) {

        items.addAll(beans);
        mAdapter.notifyDataSetChanged();

        if (!isRequestTop) {
            mPresenter.topDolls();
            isRequestTop = true;
        }

    }

}
