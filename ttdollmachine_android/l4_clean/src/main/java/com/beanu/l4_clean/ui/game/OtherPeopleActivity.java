package com.beanu.l4_clean.ui.game;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.EmptyClawViewBinder;
import com.beanu.l4_clean.adapter.binder.FirendDollsHeaderViewBinder;
import com.beanu.l4_clean.adapter.binder.MyDollsViewBinder;
import com.beanu.l4_clean.base.SimplestListActivity;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.EmptyClaw;
import com.beanu.l4_clean.model.bean.FirendDollsHeader;
import com.beanu.l4_clean.model.bean.MyDolls;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 观看其他用户
 */
public class OtherPeopleActivity extends SimplestListActivity<MyDolls> implements MyDollsViewBinder.OnSelectedChangeListener {

    private Items mItems = new Items();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_people);

        requestHeader();
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(FirendDollsHeader.class, new FirendDollsHeaderViewBinder());
        MyDollsViewBinder mDollsViewBinder = new MyDollsViewBinder(0, this);
        adapter.register(MyDolls.class, mDollsViewBinder);
        adapter.register(EmptyClaw.class, new EmptyClawViewBinder());
        return adapter;
    }

    @Override
    public Observable<? extends IPageModel<MyDolls>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).myFriendCaughtList(pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<MyDolls>>handleResult());
    }

    @Override
    public void loadDataComplete(List<MyDolls> beans) {

        FirendDollsHeader myDollsHeader = null;
        if (mItems.size() > 0 && mItems.get(0) instanceof FirendDollsHeader) {
            myDollsHeader = (FirendDollsHeader) mItems.get(0);
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
    public void onSelectedChanged(int length) {

    }


    private void requestHeader() {
        API.getInstance(APIService.class).myFriendCaughtHeader(1, 1).compose(RxHelper.<FirendDollsHeader>handleResult())
                .subscribe(new Observer<FirendDollsHeader>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(FirendDollsHeader firendDollsHeader) {
                        mItems.add(firendDollsHeader);
                        getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}