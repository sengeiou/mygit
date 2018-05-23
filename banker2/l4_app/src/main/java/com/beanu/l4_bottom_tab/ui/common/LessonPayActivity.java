package com.beanu.l4_bottom_tab.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_pay.PayResultCallBack;
import com.beanu.l2_pay.PayType;
import com.beanu.l2_pay.PayUtil;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_shoppingcart.AddressChooseActivity;
import com.beanu.l3_shoppingcart.model.APICartService;
import com.beanu.l3_shoppingcart.model.bean.AddressItem;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.beanu.l4_bottom_tab.util.Subscriber;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 网课 直播课 购买界面 (参考 CartPayActivity页面)
 */
public class LessonPayActivity extends BaseSDActivity implements PayResultCallBack {

    private static final int REQUEST_CHOOSE = 0;

    @BindView(R.id.txt_order_title) TextView mTxtOrderTitle;
    @BindView(R.id.txt_order_time) TextView mTxtOrderTime;
    @BindView(R.id.txt_order_teachers) TextView mTxtOrderTeachers;
    @BindView(R.id.txt_price) TextView mTxtPrice;
    @BindView(R.id.cbAli) RadioButton mCbAli;
    @BindView(R.id.cbWeixin) RadioButton mCbWeixin;

    @BindView(R.id.cart_txt_address_userName) TextView mCartTxtAddressUserName;
    @BindView(R.id.cart_txt_address_userPhone) TextView mCartTxtAddressUserPhone;
    @BindView(R.id.cart_txt_address) TextView mCartTxtAddress;
    @BindView(R.id.edit_mark) EditText mEditMark;

    String orderId;

    int orderType;//0 直播课  1网课
    String lessonId;//课程ID
    double priceSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_pay);
        ButterKnife.bind(this);

        mEditMark.clearFocus();
//        AndroidBug5497Workaround.assistActivity(this);

        orderType = getIntent().getIntExtra("orderType", 0);
        lessonId = getIntent().getStringExtra("lessonId");
        String orderTitle = getIntent().getStringExtra("title");
        String orderTime = getIntent().getStringExtra("time");
        priceSum = getIntent().getDoubleExtra("price", 0);

        mTxtPrice.setText("¥" + priceSum);
        mTxtOrderTitle.setText(orderTitle);
        if (orderType == 0) {
            mTxtOrderTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_the_tag2, 0);
            mTxtOrderTime.setText("上课时间：" + orderTime);
        } else {
            mTxtOrderTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_the_tag1, 0);
            mTxtOrderTime.setText(orderTime);
        }

        PayUtil.initWx(Constants.WX_APPID);

        //显示老师列表
        Observer subscriber = new Observer<List<TeacherIntro>>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<TeacherIntro> list) {
                String name = "";
                for (TeacherIntro teacherIntro : list) {
                    name += teacherIntro.getName() + ",";
                }
                if (name.length() > 0) {
                    mTxtOrderTeachers.setText("授课老师：" + name.substring(0, name.length() - 1));
                }
            }
        };
        if (orderType == 0) {
            API.getInstance(ApiService.class).live_lesson_teacher_list(API.createHeader(), lessonId)
                    .compose(RxHelper.<List<TeacherIntro>>handleResult())
                    .subscribe(subscriber);
        } else {
            API.getInstance(ApiService.class).online_lesson_teacher_list(API.createHeader(), lessonId)
                    .compose(RxHelper.<List<TeacherIntro>>handleResult())
                    .subscribe(subscriber);
        }

        //显示默认地址
        API.getInstance(APICartService.class)
                .my_address_default(API.createHeader())
                .compose(RxHelper.<AddressItem>handleResult())
                .subscribe(new Observer<AddressItem>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddressItem addressItem) {
                        refreshAddress(addressItem);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHOOSE) {
            AddressItem addressItem = data.getParcelableExtra("address");
            refreshAddress(addressItem);
        }
    }

    @OnClick({R.id.rl_address, R.id.rlAli, R.id.rlWeixin, R.id.btn_cart_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_address:
                Intent intent = new Intent(this, AddressChooseActivity.class);
                startActivityForResult(intent, REQUEST_CHOOSE);
                break;
            case R.id.rlAli:
                mCbAli.setChecked(true);
                mCbWeixin.setChecked(false);
                break;
            case R.id.rlWeixin:
                mCbAli.setChecked(false);
                mCbWeixin.setChecked(true);
                break;
            case R.id.btn_cart_pay:

                if (TextUtils.isEmpty(orderId)) {

                    String addressId = (String) mCartTxtAddress.getTag();
                    String remark = mEditMark.getText().toString();

                    if (!TextUtils.isEmpty(addressId)) {
                        API.getInstance(ApiService.class).createLessonOrder(API.createHeader(), orderType, lessonId, addressId, remark)
                                .compose(RxHelper.<Map<String, String>>handleResult())
                                .subscribe(new Observer<Map<String,String>>() {

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Map<String, String> map) {
                                        orderId = map.get("orderId");
                                        gotoPay();
                                    }
                                });
                    } else {
                        ToastUtils.showShort( "填写收货地址");
                    }
                } else {
                    gotoPay();
                }

                break;
        }
    }


    //更新地址视图
    private void refreshAddress(AddressItem address) {
        mCartTxtAddressUserName.setText(address.getLink_name());
        mCartTxtAddressUserPhone.setText(address.getLink_phone());
        mCartTxtAddress.setText(address.getProvince_cn() + address.getCity_cn() + address.getCounty_cn() + address.getLink_address());
        mCartTxtAddress.setTag(address.getId());
    }

    private void gotoPay() {

        if (priceSum == 0) {
            onPaySuccess(null);
        } else {
            if (mCbAli.isChecked()) {
                //支付宝支付
                showProgress();
                API.getInstance(APICartService.class).requestAlipaySign(orderId)
                        .compose(RxHelper.<String>handleResult())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                hideProgress();
                            }

                            @Override
                            public void onNext(String signParam) {
                                //启动支付宝
                                PayUtil.pay(LessonPayActivity.this, PayType.ALI, signParam, LessonPayActivity.this);
                            }
                        });

            } else {
                //微信支付
                showProgress();
                API.getInstance(APICartService.class).requestWePaySign(orderId)
                        .compose(RxHelper.<Map<String, String>>handleResult())
                        .subscribe(new Subscriber<Map<String, String>>() {
                            @Override
                            public void onCompleted() {
                                hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                hideProgress();
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
                                PayUtil.wxPay(LessonPayActivity.this, req, LessonPayActivity.this);
                            }
                        });

            }
        }

    }


    @Override
    public String setupToolBarTitle() {
        return "确认订单";
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }


    @Override
    public void onPaySuccess(PayType type) {
        ToastUtils.showShort("支付成功");
        ARouter.getInstance().build("/app/my/orderList").withInt("page", orderType).navigation();

        Arad.bus.post(new EventModel.CartBuySuccess());
        finish();
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
        Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();

    }
}
