package com.beanu.l4_clean.ui.game;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.WinnersViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Winners;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 抓中列表／抓娃娃达人
 */
public class GamePeopleFragment extends Fragment {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private static final String ARG_PARAM1 = "dollMaId";


    private String mMachineId;//机器ID

    private Items mItems;
    private MultiTypeAdapter mMultiTypeAdapter;


    public GamePeopleFragment() {
        // Required empty public constructor
    }

    public static GamePeopleFragment newInstance(String param) {
        GamePeopleFragment fragment = new GamePeopleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMachineId = getArguments().getString(ARG_PARAM1);
        }

        mItems = new Items();

        Observer<PageModel<Winners>> observer = new Observer<PageModel<Winners>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PageModel<Winners> winnersPageModel) {
                mItems.addAll(winnersPageModel.getDataList());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mMultiTypeAdapter.notifyDataSetChanged();
            }
        };


        API.getInstance(APIService.class).winnerRecordList(mMachineId, 1, 30)
                .compose(RxHelper.<PageModel<Winners>>handleResult())
                .subscribe(observer);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_people, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMultiTypeAdapter = new MultiTypeAdapter(mItems);
        mMultiTypeAdapter.register(Winners.class, new WinnersViewBinder());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mMultiTypeAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(android.R.color.transparent).size(8).build());
        mRecycleView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}