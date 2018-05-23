package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l2_recyclerview.SimplestListFragment;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.DeliverOrderViewBinder;
import com.beanu.l4_clean.adapter.binder.EmptyClawViewBinder;
import com.beanu.l4_clean.adapter.binder.MyDollsHeaderViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.DeliverOrder;
import com.beanu.l4_clean.model.bean.EmptyClaw;
import com.beanu.l4_clean.model.bean.MyDollsHeader;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 已发货
 */

public class DeliverFragment extends SimplestListFragment<DeliverOrder> implements MyDollsHeaderViewBinder.OnTabClickListener {

    private Items mItems = new Items();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dolls_deliver;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uiRefresh();
//        getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).size(1).colorResId(R.color.base_line).build());

    }

    @Override
    public Observable<? extends IPageModel<DeliverOrder>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).myDollFinish(pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<DeliverOrder>>handleResult());

    }

    @Override
    public void loadDataComplete(List<DeliverOrder> beans) {
        MyDollsHeader myDollsHeader = null;
        if (mItems.size() > 0 && mItems.get(0) instanceof MyDollsHeader) {
            myDollsHeader = (MyDollsHeader) mItems.get(0);
        }
        mItems.clear();
        if (myDollsHeader != null)
            mItems.add(myDollsHeader);

        List list = mPresenter.getList();
        if (list.isEmpty()) {
            mItems.add(new EmptyClaw());
        } else {
            mItems.addAll(mPresenter.getList());
        }
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(MyDollsHeader.class, new MyDollsHeaderViewBinder(MyDollsHeaderViewBinder.DELIVER, this));
        adapter.register(DeliverOrder.class, new DeliverOrderViewBinder());
        adapter.register(EmptyClaw.class, new EmptyClawViewBinder());
        return adapter;
    }

    @Override
    public void onTabClick(int tab) {
        FragmentActivity activity = getActivity();
        if (activity instanceof MyDollsActivity) {
            ((MyDollsActivity) activity).getSwitcher().switchFragment(DollsWaitFragment.class.getName());
        }
    }

    @Override
    public void contentLoadingEmpty() {
        getRefreshLayout().refreshComplete();
    }

    @Override
    public void contentLoadingError() {
        getRefreshLayout().refreshComplete();
    }


    private void uiRefresh() {
        API.getInstance(APIService.class).myDollCaughtIn2().compose(RxHelper.<MyDollsHeader>handleResult())
                .subscribe(new Observer<MyDollsHeader>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(MyDollsHeader map) {

                        mItems.add(0, map);
                        getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
