package com.beanu.l4_clean.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_shoppingcart.AddressChooseActivity;
import com.beanu.l3_shoppingcart.model.bean.AddressItem;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.ShowClawViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.MyDolls;
import com.beanu.l4_clean.model.bean.ShowClaw;
import com.beanu.l4_clean.support.dialog.ConfirmOrderDialog;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 确认订单
 */
public class ConfirmOrderActivity extends BaseTTActivity {

    private static final int REQUEST_ADDRESS = 11;

    @BindView(R.id.recycler_claw) RecyclerView mRecyclerClaw;
    @BindView(R.id.text_address) TextView mTextAddress;
    @BindView(R.id.text_change) TextView mTextChange;
    @BindView(R.id.edit_remark) EditText mEditText;

    private AddressItem mAddress;

    private Items mItems;
    private MultiTypeAdapter mAdapter;

    public static void startActivity(Activity context, ArrayList<MyDolls> myDollsList, int requestCode) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putParcelableArrayListExtra("date", myDollsList);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);

        mItems = new Items();
        ArrayList<MyDolls> list = getIntent().getParcelableArrayListExtra("date");
        for (MyDolls myDolls : list) {
            ShowClaw showClaw = new ShowClaw(myDolls.getId(), myDolls.getName(), myDolls.getImage());
            mItems.add(showClaw);
        }
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(ShowClaw.class, new ShowClawViewBinder());

        mRecyclerClaw.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerClaw.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).size(16).build());
        mRecyclerClaw.setNestedScrollingEnabled(false);
        mRecyclerClaw.setAdapter(mAdapter);


        requestDefaultAddress();
    }


    @Override
    public String setupToolBarTitle() {
        return "确认订单";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK && data != null) {
            mAddress = data.getParcelableExtra("address");

            if (mAddress != null) {
                mTextAddress.setText(mAddress.getProvinceName() + mAddress.getCityName() + mAddress.getAddress());
                mTextChange.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.rl_address, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_address:

                Intent intent = new Intent(this, AddressChooseActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS);

                break;
            case R.id.btn_confirm:

                if (mAddress != null) {
                    showProgress();

                    String remark = mEditText.getText().toString();
                    ArrayList<MyDolls> list = getIntent().getParcelableArrayListExtra("date");
                    String ids = "";
                    for (MyDolls myDolls : list) {
                        ids += myDolls.getLogId() + ",";
                    }

                    orderConfirm(ids, remark, mAddress.getAddressId());

                } else {
                    ToastUtils.showShort("地址不能为空");
                }

                break;
        }
    }


    private void requestDefaultAddress() {
        API.getInstance(APIService.class).myDefaultAddress().compose(RxHelper.<Map<String, String>>handleResult())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Map<String, String> map) {
                        mTextAddress.setText(map.get("address"));

                        mAddress = new AddressItem();
                        mAddress.setAddressId(map.get("addressId"));
                        mTextChange.setVisibility(View.VISIBLE);

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

    private void orderConfirm(String logId, String remark, String addressId) {
        API.getInstance(APIService.class).confirmOrder(logId, remark, addressId).compose(RxHelper.<String>handleResult())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                        ConfirmOrderDialog.show(getSupportFragmentManager(), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });

                    }
                });

    }
}