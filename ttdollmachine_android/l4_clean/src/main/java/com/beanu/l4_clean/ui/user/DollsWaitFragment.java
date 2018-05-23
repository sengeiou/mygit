package com.beanu.l4_clean.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_recyclerview.SimplestListFragment;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.EmptyClawViewBinder;
import com.beanu.l4_clean.adapter.binder.MyDollsHeaderViewBinder;
import com.beanu.l4_clean.adapter.binder.MyDollsViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.EmptyClaw;
import com.beanu.l4_clean.model.bean.MyDolls;
import com.beanu.l4_clean.model.bean.MyDollsHeader;
import com.beanu.l4_clean.support.dialog.ConfirmRewardsDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 等待发货
 */
public class DollsWaitFragment extends SimplestListFragment<MyDolls> implements MyDollsHeaderViewBinder.OnTabClickListener,
        MyDollsViewBinder.OnSelectedChangeListener, View.OnClickListener {

    private static final int REQUEST_CODE = 222;
    private Items mItems = new Items();
    private Button btnMail;
    private Button btnChange;
    private MyDollsViewBinder mDollsViewBinder;
    private int coins;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnMail = view.findViewById(R.id.btn_ems);
        btnChange = view.findViewById(R.id.btn_change);
        btnMail.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        uiRefresh();
//        getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).size(1).colorResId(R.color.base_line).build());
        mPresenter.loadDataFirst();

    }


    @Override
    public void autoRefresh() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wait_dolls;
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(MyDollsHeader.class, new MyDollsHeaderViewBinder(MyDollsHeaderViewBinder.NOT_DELIVER, this));
        mDollsViewBinder = new MyDollsViewBinder(1, this);
        adapter.register(MyDolls.class, mDollsViewBinder);
        adapter.register(EmptyClaw.class, new EmptyClawViewBinder());
        return adapter;
    }

    @Override
    public Observable<? extends IPageModel<MyDolls>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).myPrizeList(pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<MyDolls>>handleResult());
    }

    @Override
    public void loadDataComplete(List<MyDolls> beans) {

        MyDollsHeader myDollsHeader = null;
        if (mItems.size() > 0 && mItems.get(0) instanceof MyDollsHeader) {
            myDollsHeader = (MyDollsHeader) mItems.get(0);
        }
        mItems.clear();
        if (myDollsHeader != null)
            mItems.add(myDollsHeader);

        //TODO TEST
        mItems.add(0, new MyDollsHeader());


        if (beans.isEmpty()) {
            mItems.add(new EmptyClaw());
        } else {
            mItems.addAll(beans);
        }
        getAdapter().notifyDataSetChanged();
        btnMail.setEnabled(false);
        btnChange.setEnabled(false);
        btnChange.setText("兑换游戏币");

        mDollsViewBinder.getSelectedDolls().clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getRefreshLayout().autoRefresh();
        }
    }

    @Override
    public void onTabClick(int tab) {
        FragmentActivity activity = getActivity();
        if (activity instanceof MyDollsActivity) {
            ((MyDollsActivity) activity).getSwitcher().switchFragment(DeliverFragment.class.getName());
        }
    }

    @Override
    public void onSelectedChanged(int length) {
        if (length >= 2) {
            btnMail.setEnabled(true);
        } else {
            btnMail.setEnabled(false);
        }

        if (length >= 1) {
            btnChange.setEnabled(true);
            ArrayList<MyDolls> selectedDolls = (ArrayList<MyDolls>) mDollsViewBinder.getSelectedDolls();
            coins = 0;
            for (MyDolls selectedDoll : selectedDolls) {
                coins += selectedDoll.getCoins();
            }
            btnChange.setText("兑换" + coins + "游戏币");
        } else {
            btnChange.setEnabled(false);
            btnChange.setText("兑换游戏币");
        }


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_ems:
                ArrayList<MyDolls> selectedDolls = (ArrayList<MyDolls>) mDollsViewBinder.getSelectedDolls();

                Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                intent.putParcelableArrayListExtra("date", selectedDolls);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_change:

                ArrayList<MyDolls> dolls = (ArrayList<MyDolls>) mDollsViewBinder.getSelectedDolls();
                String ids = "";
                for (MyDolls selectedDoll : dolls) {
                    ids += selectedDoll.getLogId() + ",";
                }
                dollToScore(ids);

                break;
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

    private void dollToScore(String ids) {
        API.getInstance(APIService.class).dollToScore(ids).compose(RxHelper.handleResult())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                        getRefreshLayout().autoRefresh();
                        updateUserBalance();


                        ConfirmRewardsDialog.show(getFragmentManager(), coins, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                });
    }

    private void updateUserBalance() {
        API.getInstance(APIService.class).userBalance().compose(RxHelper.<Map<String, Integer>>handleResult())
                .subscribe(new Observer<Map<String, Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Map<String, Integer> map) {
                        int coin = map.get("coins");
                        if (coin >= 0) {
                            AppHolder.getInstance().user.setCoins(coin);
                        }
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