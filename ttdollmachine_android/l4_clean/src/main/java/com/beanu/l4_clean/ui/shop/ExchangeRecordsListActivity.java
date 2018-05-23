package com.beanu.l4_clean.ui.shop;

import android.support.v7.widget.RecyclerView;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l2_recyclerview.SimplestListActivity;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.adapter.binder.CommodityExchangeViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Commodity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 兑换记录
 */
public class ExchangeRecordsListActivity extends SimplestListActivity<Commodity> {

    private Items mItems = new Items();

    @Override
    public Observable<? extends IPageModel<Commodity>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).exchangeRecordList(pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<Commodity>>handleResult());
    }

    @Override
    public void loadDataComplete(List<Commodity> beans) {
        mItems.clear();
        mItems.addAll(mPresenter.getList());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(Commodity.class, new CommodityExchangeViewBinder());
        return adapter;
    }



    @Override
    public String setupToolBarTitle() {
        return "兑换记录";
    }
}
