package com.beanu.l4_clean.ui.user;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l2_recyclerview.SimplestListFragment;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.adapter.binder.CrawlRecordViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.CrawlRecord;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 我的记录列表
 */
public class MyRecordListFragment extends SimplestListFragment<CrawlRecord> {

    private static final String ARG_PARAM1 = "param1";

    private int mParam1;
    private Items mItems = new Items();

    public MyRecordListFragment() {
        // Required empty public constructor
    }

    public static MyRecordListFragment newInstance(int param1) {
        MyRecordListFragment fragment = new MyRecordListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
////        getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).colorResId(android.R.color.transparent).marginResId(R.dimen.grid_space).build());
//    }

    @Override
    public Observable<? extends IPageModel<CrawlRecord>> loadData(Map<String, Object> params, int pageIndex) {

        return API.getInstance(APIService.class).crawlRecordList(mParam1, pageIndex, 30).compose(RxHelper.<PageModel<CrawlRecord>>handleResult());
    }

    @Override
    public void loadDataComplete(List<CrawlRecord> beans) {
        mItems.clear();
        mItems.addAll(mPresenter.getList());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(CrawlRecord.class, new CrawlRecordViewBinder());

        return adapter;
    }

}
