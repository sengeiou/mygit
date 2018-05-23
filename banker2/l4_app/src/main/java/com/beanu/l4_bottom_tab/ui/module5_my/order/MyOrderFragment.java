package com.beanu.l4_bottom_tab.ui.module5_my.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l3_shoppingcart.CartPayActivity;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.OrderItemViewBinder;
import com.beanu.l4_bottom_tab.model.bean.OrderItem;
import com.beanu.l4_bottom_tab.mvp.contract.MyOrderContract;
import com.beanu.l4_bottom_tab.mvp.model.MyOrderModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.MyOrderPresenterImpl;
import com.beanu.l4_bottom_tab.ui.common.CommentPostActivity;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 我的订单
 */
public class MyOrderFragment extends ToolBarFragment<MyOrderPresenterImpl, MyOrderModelImpl> implements MyOrderContract.View, OrderItemViewBinder.ButtonListener {

    private static final String ARG_PARAM1 = "type";
    private static final int REQUEST_COMMENT = 100;

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.arad_content) PtrClassicFrameLayout mPtrFrame;
    Unbinder unbinder;

    private int mType;

    Items mItems;
    MultiTypeAdapter mMultiAdapter;
    LoadMoreAdapterWrapper mAdapterWrapper;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    public static MyOrderFragment newInstance(int type) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_PARAM1);
        }

        mItems = new Items();
        mMultiAdapter = new MultiTypeAdapter(mItems);
        mMultiAdapter.register(OrderItem.class, new OrderItemViewBinder(this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mAdapterWrapper = new LoadMoreAdapterWrapper(getActivity(), mMultiAdapter, mPresenter);
        mRecycleView.setAdapter(mAdapterWrapper);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(android.R.color.transparent).sizeResId(R.dimen.list_margin_8).build());

        //上拉监听
        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });

        //下拉刷新监听
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.loadDataFirst();
            }
        });

        if (mPresenter.getList().size() == 0) {
            contentLoading();

            ArrayMap<String, Object> params = new ArrayMap<>();
            params.put("type", mType);

            mPresenter.initLoadDataParams(params);
            mPresenter.loadDataFirst();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadDataComplete(List<OrderItem> beans) {
        mItems.clear();

        for (OrderItem item : beans) {
            item.setOrderType(mType);
        }
        mItems.addAll(beans);

        mPtrFrame.refreshComplete();
        mMultiAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteOrderSuccess(int position) {
        mItems.remove(position);
        mMultiAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onDeleteClick(final int position, final String orderId) {

        UIUtils.showAlertDialog(getChildFragmentManager(), "提示", "确定要删除这个订单吗？", "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                mPresenter.deleteOrder(position, orderId);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

    }

    @Override
    public void onPayClick(int position, String orderId) {
        switch (mType) {
            case 0:
            case 1:
            case 2:
                //图书
                //TODO 购买之后页面跳转有问题
                DecimalFormat df = new DecimalFormat("0.00");

                OrderItem orderItem = mPresenter.getList().get(position);
                Intent intent = new Intent(getActivity(), CartPayActivity.class);
                intent.putExtra("priceSum", Double.parseDouble(df.format(orderItem.getTotal())));
                intent.putExtra("orderId", orderId);
                intent.putExtra("type", mType);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onCommentClick(int position, int child) {
        Intent intent = new Intent(getActivity(), CommentPostActivity.class);
        intent.putExtra("shop", mPresenter.getList().get(position).getOpList().get(child));
        intent.putExtra("type", mType);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_COMMENT);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_COMMENT && resultCode == Activity.RESULT_OK) {
//            int position = data.getIntExtra("position", 0);
//
//            mItems.get(position)
//            mMultiAdapter.notifyItemChanged(position);
//        }
//    }
}