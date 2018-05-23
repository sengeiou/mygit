package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.SizeUtils;
import com.beanu.l2_recyclerview.RecyclerViewPtrHandler;
import com.beanu.l2_recyclerview.SimplestListActivity;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.BillViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.BillItem;
import com.beanu.l4_clean.util.PtrSpringHelper;

import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 娃娃币账单
 */
public class BillActivity extends SimplestListActivity<BillItem> {
    private final Items items = new Items();

    private TextView mTxtBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTxtBalance = findViewById(R.id.txt_balance);
        mPresenter.loadDataFirst();
    }

    @Override
    public void autoRefresh() {

    }

    @Override
    protected void initPtr() {

        new PtrSpringHelper(findViewById(R.id.img_progress), findViewById(R.id.layout_header), SizeUtils.dp2px(110))
                .blackMode(true)
                .attachTo(getRefreshLayout(), true);

        getRefreshLayout().setPtrHandler(new RecyclerViewPtrHandler(getRecyclerView()) {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadDataFirst();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bill;
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(items);
        adapter.register(BillItem.class, new BillViewBinder());
        return adapter;
    }

    @Override
    public Observable<? extends IPageModel<BillItem>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).coinList(pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<BillItem>>handleResult());
    }

    @Override
    public void loadDataComplete(List<BillItem> beans) {
        items.clear();
        items.addAll(mPresenter.getList());
        getAdapter().notifyDataSetChanged();

        mTxtBalance.setText(AppHolder.getInstance().user.getCoins() + "");
    }

    @Override
    public String setupToolBarTitle() {
        return "我的T币";
    }
}