package com.beanu.l4_clean.ui.user;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l2_recyclerview.SimplestListActivity;
import com.beanu.l3_common.model.HttpModel;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.PKDetailViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.PKDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class PKDetailActivity extends SimplestListActivity<PKDetail> {

    private Items mItems = new Items();

    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, PKDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    @Override
    protected ArrayMap<String, ?> provideParams() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        return map;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pkdetail;
    }

    @Override
    public void autoRefresh() {
        mPresenter.loadDataFirst();
    }

    @Override
    protected void initList() {
        super.initList();
        getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).size(10).build());
    }

    @Override
    protected void initPtr() {

    }


    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(PKDetail.class, new PKDetailViewBinder());
        return adapter;
    }

    @Override
    public String setupToolBarTitle() {
        return "对战记录";
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton1) {
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return true;
    }

    @Override
    public Observable<? extends IPageModel<PKDetail>> loadData(Map<String, Object> params, int pageIndex) {

        String id = (String) params.get("id");
        return API.getInstance(APIService.class).pkRecordDetail(id)
                .flatMap(new Function<HttpModel<PKDetail>, ObservableSource<HttpModel<PageModel<PKDetail>>>>() {
                    @Override
                    public ObservableSource<HttpModel<PageModel<PKDetail>>> apply(final HttpModel<PKDetail> pkDetailHttpModel) throws Exception {


                        return Observable.create(new ObservableOnSubscribe<HttpModel<PageModel<PKDetail>>>() {
                            @Override
                            public void subscribe(ObservableEmitter<HttpModel<PageModel<PKDetail>>> subscriber) throws Exception {

                                HttpModel<PageModel<PKDetail>> baseModel = new HttpModel<>();
                                baseModel.succeed = "000";

                                PageModel<PKDetail> pageModel = new PageModel<>();
                                pageModel.pageNumber = 1;
                                pageModel.totalPage = 1;
                                pageModel.dataList = new ArrayList<>();
                                pageModel.dataList.add(pkDetailHttpModel.getResults());

                                baseModel.dataInfo = pageModel;

                                subscriber.onNext(baseModel);
                                subscriber.onComplete();
                            }
                        });


                    }
                }).compose(RxHelper.<PageModel<PKDetail>>handleResult());


//        return FakeLoader.loadList(PKDetail.class, pageIndex);
    }

    @Override
    public void loadDataComplete(List<PKDetail> beans) {
        mItems.clear();
        mItems.addAll(mPresenter.getList());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void contentLoadingComplete() {

    }

    @Override
    public void contentLoadingEmpty() {

    }

    @Override
    public void contentLoadingError() {

    }
}
