package com.beanu.l4_clean.ui.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_shoppingcart.AddressChooseActivity;
import com.beanu.l3_shoppingcart.model.bean.AddressItem;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Commodity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 商品详情
 */
public class ShopDetailActivity extends BaseTTActivity {

    @BindView(R.id.img_top) ImageView mImgTop;
    @BindView(R.id.txt_name) TextView mTxtName;
    @BindView(R.id.txt_price) TextView mTxtPrice;
    @BindView(R.id.txt_score) TextView mTxtScore;
    @BindView(R.id.txt_address) TextView mTxtAddress;
    @BindView(R.id.webview) WebView mWebview;
    @BindView(R.id.btn_exchange) Button mBtnExchange;

    String id;
    private static final int REQUEST_CHOOSE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);

        id = getIntent().getStringExtra("id");
        initData(id);
    }


    @OnClick({R.id.txt_address, R.id.btn_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_address:
                Intent intent = new Intent(this, AddressChooseActivity.class);
                startActivityForResult(intent, REQUEST_CHOOSE);
                break;
            case R.id.btn_exchange:
                String addressId = null;
                if (mTxtAddress.getTag() != null) {
                    addressId = (String) mTxtAddress.getTag();
                }

                if (addressId != null) {

                    UIUtils.showAlertDialog(getSupportFragmentManager(), "提示", "确定需要兑换此商品吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            String addid = (String) mTxtAddress.getTag();
                            scoreToProduct(id, addid);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                } else {
                    ToastUtils.showShort("选择收货地址");
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHOOSE) {
            AddressItem addressItem = data.getParcelableExtra("address");

            mTxtAddress.setText(addressItem.getProvinceName() + addressItem.getCityName() + addressItem.getCountyName() + addressItem.getAddress());
            mTxtAddress.setTag(addressItem.getAddressId());

        }
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
    public String setupToolBarTitle() {
        return "商品详情";
    }

    private void initData(String id) {
        API.getInstance(APIService.class).shopDetail(id).compose(RxHelper.<Commodity>handleResult())
                .subscribe(new Observer<Commodity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Commodity commodity) {
                        uiRefresh(commodity);
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

    private void scoreToProduct(String id, String addressId) {
        API.getInstance(APIService.class).scoreToProduct(id, addressId).compose(RxHelper.handleResult())
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
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        ToastUtils.showShort("兑换成功");
                    }
                });
    }

    private void uiRefresh(Commodity commodity) {

        if (!TextUtils.isEmpty(commodity.getDetail_imaage())) {
            Glide.with(this).load(commodity.getDetail_imaage()).into(mImgTop);
        }
        mTxtName.setText(commodity.getName());
        mTxtScore.setText(commodity.getScore() + "积分");
        mTxtPrice.setText(commodity.getPrice());

        if (AppHolder.getInstance().user.getScore() >= commodity.getScore()) {
            mBtnExchange.setEnabled(true);
        } else {
            mBtnExchange.setEnabled(false);
            mBtnExchange.setText("积分不足");
        }

        //TODO address

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 19) {//硬件加速器的使用
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebViewClient(new WebViewClient() { //调用自身，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());


        webView.loadUrl(commodity.getUrl());

    }

}