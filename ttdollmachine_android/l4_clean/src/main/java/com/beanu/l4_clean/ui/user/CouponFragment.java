package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l2_recyclerview.SimplestListFragment;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.CouponViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Coupon;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 优惠券
 */
public class CouponFragment extends SimplestListFragment<Coupon> {

    private static final String ARG_PARAM1 = "param1";
    private int mType;

    private Items mItems = new Items();

    public CouponFragment() {
        // Required empty public constructor
    }

    public static CouponFragment newInstance(int param1) {
        CouponFragment fragment = new CouponFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_PARAM1);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());
    }

    @Override
    public Observable<? extends IPageModel<Coupon>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).couponList(mType, pageIndex, 30).compose(RxHelper.<PageModel<Coupon>>handleResult());
    }

    @Override
    public void loadDataComplete(List<Coupon> beans) {
        mItems.clear();
        mItems.addAll(mPresenter.getList());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {

        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(Coupon.class, new CouponViewBinder(mType));
        return adapter;
    }

//    @Override
//    protected RecyclerView.LayoutManager provideLayoutManager() {
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        return gridLayoutManager;
//    }
}