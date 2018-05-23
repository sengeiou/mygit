package com.beanu.l4_clean.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l2_recyclerview.RecyclerViewPtrHandler;
import com.beanu.l2_recyclerview.SimplestListFragment;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.RankPeopleHeaderViewBinder;
import com.beanu.l4_clean.adapter.binder.RankPeopleViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.RankPeople;
import com.beanu.l4_clean.model.bean.RankPeopleHeader;

import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 排行榜
 */
public class RankFragment extends SimplestListFragment<RankPeople> {

    private Items mItems = new Items();
    private int mType;

    public RankFragment() {
        // Required empty public constructor
    }

    public static RankFragment newInstance(int type) {
        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt("type", 0);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRefreshLayout().setPtrHandler(new RecyclerViewPtrHandler(getRecyclerView()) {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadDataFirst();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }
        });
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(RankPeopleHeader.class, new RankPeopleHeaderViewBinder());
        adapter.register(RankPeople.class, new RankPeopleViewBinder());
        return adapter;
    }

    @Override
    public Observable<? extends IPageModel<RankPeople>> loadData(Map<String, Object> params, int pageIndex) {

        return API.getInstance(APIService.class).rankList(mType, pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<RankPeople>>handleResult());
    }

    @Override
    public void loadDataComplete(List<RankPeople> beans) {
        mItems.clear();

        RankPeopleHeader peopleHeader = new RankPeopleHeader();
        if (beans.size() >= 3) {
            peopleHeader.setRankPeople1(beans.get(0));
            peopleHeader.setRankPeople2(beans.get(1));
            peopleHeader.setRankPeople3(beans.get(2));

            mItems.add(0, new RankPeopleHeader());
            mItems.addAll(beans.subList(4, beans.size() - 1));

        } else {

            if (beans.size() == 1) {
                peopleHeader.setRankPeople1(beans.get(0));
            }

            if (beans.size() == 2) {
                peopleHeader.setRankPeople1(beans.get(0));
                peopleHeader.setRankPeople2(beans.get(1));
            }

            mItems.add(0, peopleHeader);
        }

        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank;
    }
}