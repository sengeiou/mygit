package com.beanu.l4_clean.ui.shop;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.l2_recyclerview.SimplestListActivity;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.CommodityViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Commodity;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 积分商城
 */
public class JIFenShopActivity extends SimplestListActivity<Commodity> {

    @BindView(R.id.txt_jifen) TextView mTxtJifen;
    @BindView(R.id.txt_record) LinearLayout mTxtRecord;

    private Items mItems = new Items();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mTxtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ExchangeRecordsListActivity.class);
            }
        });

        getRecyclerView().addItemDecoration(new GridLayoutItemDecoration(16, 2));
    }

    @Override
    public void autoRefresh() {
        mPresenter.loadDataFirst();
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(Commodity.class, new CommodityViewBinder());
        return mAdapter;
    }


    @Override
    public Observable<? extends IPageModel<Commodity>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).shopList(pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<Commodity>>handleResult());
    }

    @Override
    public void loadDataComplete(List<Commodity> beans) {

        mItems.addAll(beans);
        getAdapter().notifyDataSetChanged();

        mTxtJifen.setText("" + AppHolder.getInstance().user.getCoins());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jifen_shop;
    }

    @Override
    protected RecyclerView.LayoutManager provideLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        return gridLayoutManager;
    }

    @Override
    public String setupToolBarTitle() {
        return "积分商城";
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

}