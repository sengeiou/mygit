package com.beanu.l4_clean.ui.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.arad.widget.dialog.BottomDialog;
import com.beanu.l2_pay.PayResultCallBack;
import com.beanu.l2_pay.PayType;
import com.beanu.l2_pay.PayUtil;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_shoppingcart.model.APICartService;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.RechargeOptionViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.RechargeOption;
import com.beanu.l4_clean.support.dialog.NeutralDialogFragment;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 充值
 */
public class RechargeActivity extends ToolBarActivity implements RechargeOptionViewBinder.OnRechargeSelectedListener, PayResultCallBack {

    @BindView(R.id.recycler_recharge) RecyclerView mRechargeList;
    @BindView(R.id.editText) EditText mEditText;
    @BindView(R.id.txt_balance) TextView mTxtBalance;
    @BindView(R.id.text_tip) TextView mTxtTips;
    @BindView(R.id.img_daichong) ImageView mImgDaichong;

    private Items mItems;
    private MultiTypeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);

        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);

        mAdapter.register(RechargeOption.class, new RechargeOptionViewBinder(this));
        mRechargeList.setLayoutManager(new LinearLayoutManager(this));
        mRechargeList.setAdapter(mAdapter);

        requestRechargeList();
        refreshUserBalance();

        //初始化微信支付
        PayUtil.initWx("wx65468d887db13f5b");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }

    @Override
    public String setupToolBarTitle() {
        return "我的T币";
    }

    @Override
    public boolean setupToolBarRightButton2(View rightButton2) {

        if (rightButton2 instanceof TextView) {
            ((TextView) rightButton2).setText("明细");
            rightButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(BillActivity.class);
                }
            });
        }
        return true;
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
    public void onItemSelected(RechargeOption rechargeOption, boolean isSelected) {
        for (Object option : mItems) {
            ((RechargeOption) option).setSelected(false);
        }
        rechargeOption.setSelected(!isSelected);
        mAdapter.notifyDataSetChanged();
        mTxtTips.setText(rechargeOption.getDescribe_cn());

        showPayDialog();
    }

    private void showPayDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_charge_pay, null);
        final BottomDialog bottomDialog = new BottomDialog(this, view);
        bottomDialog.getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomDialog.show();

        ImageView img_wx = view.findViewById(R.id.img_wx);
        ImageView img_ali = view.findViewById(R.id.img_ali);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
                pay(view);
            }
        };
        img_wx.setOnClickListener(listener);
        img_ali.setOnClickListener(listener);

    }


    public void pay(View view) {

        double price = 0;
        String orderId = null;

        for (Object option : mItems) {
            if (((RechargeOption) option).isSelected()) {
                price = ((RechargeOption) option).getPrice();
                orderId = ((RechargeOption) option).getRechargeId();
                break;
            }
        }


        if (price == 0) {
            ToastUtils.showShort("选择一个购买方案");
            return;
        }

        switch (view.getId()) {
            case R.id.img_ali:

                showProgress();
                API.getInstance(APICartService.class).requestAlipaySign(orderId)
                        .compose(RxHelper.<String>handleResult())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mRxManage.add(d);
                            }

                            @Override
                            public void onNext(String s) {
                                //启动支付宝
                                PayUtil.pay(RechargeActivity.this, PayType.ALI, s, RechargeActivity.this);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                hideProgress();
                            }

                            @Override
                            public void onComplete() {
                                hideProgress();
                            }
                        });


                break;
            case R.id.img_wx:

                //微信支付
                showProgress();
                API.getInstance(APICartService.class).requestWePaySign(orderId)
                        .compose(RxHelper.<Map<String, String>>handleResult())
                        .subscribe(new Observer<Map<String, String>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mRxManage.add(d);
                            }

                            @Override
                            public void onNext(Map<String, String> map) {
                                PayReq req = new PayReq();
                                req.appId = map.get("appid");
                                req.partnerId = map.get("partnerid");
                                req.prepayId = map.get("prepayid");
                                req.packageValue = map.get("packageValue");
                                req.nonceStr = map.get("noncestr");
                                req.timeStamp = map.get("timestamp");
                                req.sign = map.get("sign");

                                //调起微信支付
                                PayUtil.wxPay(RechargeActivity.this, req, RechargeActivity.this);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                hideProgress();
                            }

                            @Override
                            public void onComplete() {
                                hideProgress();
                            }
                        });
                break;
        }
    }


    private void requestRechargeList() {
        API.getInstance(APIService.class).rechargeList().compose(RxHelper.<List<RechargeOption>>handleResult())
                .subscribe(new Observer<List<RechargeOption>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(List<RechargeOption> rechargeOptions) {
                        mItems.addAll(rechargeOptions);
                        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onPaySuccess(PayType type) {

        NeutralDialogFragment dialogFragment = new NeutralDialogFragment();
        dialogFragment.show("提示", "充值成功", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                applyUserBalance();
            }
        }, getSupportFragmentManager());
    }

    private void applyUserBalance() {
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
                        refreshUserBalance();
                    }
                });
    }

    private void refreshUserBalance() {
        mTxtBalance.setText(AppHolder.getInstance().user.getCoins() + "");
    }

    @Override
    public void onPayDealing(PayType type) {

    }

    @Override
    public void onPayError(PayType type, String errorCode, String rawErrorCode) {
        Toast.makeText(this, "支付失败 " + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayCancel(PayType type) {
        ToastUtils.showShort("支付取消");
    }
}