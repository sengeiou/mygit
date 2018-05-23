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
import com.beanu.l4_clean.adapter.binder.SameDollsViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Dolls;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 同款列表
 */
public class SameListFragment extends Fragment {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mMachineType;//机器ID
    private String mDollId;//娃娃ID

    private Items mItems;
    private MultiTypeAdapter mMultiTypeAdapter;

    public SameListFragment() {
        // Required empty public constructor
    }


    public static SameListFragment newInstance(String type, String dollId) {
        SameListFragment fragment = new SameListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, dollId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMachineType = getArguments().getString(ARG_PARAM1);
            mDollId = getArguments().getString(ARG_PARAM2);
        }
        mItems = new Items();

        API.getInstance(APIService.class).sameDollMachine(mMachineType, mDollId)
                .compose(RxHelper.<PageModel<Dolls>>handleResult())
                .subscribe(new Observer<PageModel<Dolls>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PageModel<Dolls> winnersPageModel) {
                        mItems.addAll(winnersPageModel.getDataList());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mMultiTypeAdapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_same_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMultiTypeAdapter = new MultiTypeAdapter(mItems);
        mMultiTypeAdapter.register(Dolls.class, new SameDollsViewBinder());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mMultiTypeAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(android.R.color.transparent).size(8).build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}