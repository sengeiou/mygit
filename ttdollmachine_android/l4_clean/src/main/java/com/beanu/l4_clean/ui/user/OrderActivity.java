package com.beanu.l4_clean.ui.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.arad.utils.ClipboardUtils;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.ShowClawViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.DeliverOrderDetail;
import com.beanu.l4_clean.model.bean.ShowClaw;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class OrderActivity extends BaseTTActivity {

    @BindView(R.id.text_status)
    TextView mTextStatus;
    @BindView(R.id.text_express_id)
    TextView mTextExpressId;
    @BindView(R.id.btn_copy)
    TextView mBtnCopy;
    @BindView(R.id.text_id)
    TextView mTextId;
    @BindView(R.id.recycler_claw)
    RecyclerView mRecyclerClaw;
    @BindView(R.id.text_user_name)
    TextView mTextUserName;
    @BindView(R.id.text_user_phone)
    TextView mTextUserPhone;
    @BindView(R.id.text_address)
    TextView mTextAddress;

    private Items mItems;
    private MultiTypeAdapter mAdapter;

    public static void startActivity(Context context, String orderId) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(ShowClaw.class, new ShowClawViewBinder());
        mRecyclerClaw.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerClaw.setNestedScrollingEnabled(false);
        mRecyclerClaw.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());
        mRecyclerClaw.setAdapter(mAdapter);

        requestData(getIntent().getStringExtra("orderId"));
    }

    @Override
    public String setupToolBarTitle() {
        return "订单详情";
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

    @Override
    public boolean setupToolBarRightButton1(View rightButton1) {
        ((ImageView) rightButton1).setImageResource(R.drawable.kefu_white);
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "mqqwpa://im/chat?chat_type=wpa&uin=2484740137";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        return false;
    }

    @OnClick(R.id.btn_copy)
    public void onViewClicked() {
        String s = mTextExpressId.getText().toString();
        if (!TextUtils.isEmpty(s) && !TextUtils.equals("暂无", s)) {
            ClipboardUtils.copyText(s);
            ToastUtils.showShort("已复制到剪切板");
        }
    }

    private void requestData(String orderId) {
        API.getInstance(APIService.class)
                .orderDetail(orderId)
                .compose(RxHelper.<DeliverOrderDetail>handleResult())
                .subscribe(new Observer<DeliverOrderDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(DeliverOrderDetail deliverOrderDetail) {
                        refreshUI(deliverOrderDetail);
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

    private void refreshUI(DeliverOrderDetail deliverOrderDetail) {
        if (deliverOrderDetail.getDollList() != null) {
            mItems.addAll(deliverOrderDetail.getDollList());
        }
        mAdapter.notifyDataSetChanged();

        switch (deliverOrderDetail.getStatus()) {
            case 0:
                mTextStatus.setText("等待发货");
                break;
            case 1:
                mTextStatus.setText("已发货");
                break;
            case 2:
                mTextStatus.setText("已签收");
                break;
        }

        mTextExpressId.setText(deliverOrderDetail.getExpressNumber());
        mTextId.setText(deliverOrderDetail.getOrderId());
        mTextUserName.setText(deliverOrderDetail.getLinkName());
        mTextUserPhone.setText(deliverOrderDetail.getLinkPhone());
        mTextAddress.setText(deliverOrderDetail.getLinkAddress());
    }
}