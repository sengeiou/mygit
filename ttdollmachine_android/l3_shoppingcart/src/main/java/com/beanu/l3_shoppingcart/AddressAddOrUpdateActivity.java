package com.beanu.l3_shoppingcart;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_shoppingcart.model.bean.AddressItem;
import com.beanu.l3_shoppingcart.mvp.contract.AddressModifyContract;
import com.beanu.l3_shoppingcart.mvp.model.AddressModifyModelImpl;
import com.beanu.l3_shoppingcart.mvp.presenter.AddressModifyPresenterImpl;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * 添加收货地址or编辑收货地址
 */
public class AddressAddOrUpdateActivity extends ToolBarActivity<AddressModifyPresenterImpl, AddressModifyModelImpl> implements AddressModifyContract.View {

    private EditText mEditName;
    private EditText mEditPhone;
    private TextView mTxtCity;
    private EditText mEditAddress;
    private Button mBtnSave;

    private Province mProvince;
    private City mCity;
    private County mCounty;

    private int model;//0 添加 1编辑
    private AddressItem addressItem;//需要编辑的地址信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity_address_add);


        mEditName = findViewById(R.id.edit_name);
        mEditPhone = findViewById(R.id.edit_phone);
        mTxtCity = findViewById(R.id.txt_city);
        mEditAddress = findViewById(R.id.edit_address);
        mBtnSave = findViewById(R.id.btn_save);

        model = getIntent().getIntExtra("model", 0);
        addressItem = getIntent().getParcelableExtra("address");

        if (model == 1) {
            mEditName.setText(addressItem.getLinkName());
            mEditPhone.setText(addressItem.getLinkPhone());
            mTxtCity.setText(addressItem.getProvinceName() + addressItem.getCityName() + addressItem.getCountyName());
            mEditAddress.setText(addressItem.getAddress());
        }

        mTxtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddressPickTask task = new AddressPickTask(AddressAddOrUpdateActivity.this);
                task.setHideProvince(false);
                task.setHideCounty(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        KLog.d("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        mProvince = province;
                        mCity = city;
                        mCounty = county;
                        if (mProvince != null && mCity != null && mCounty != null) {
                            mTxtCity.setText(mProvince.getAreaName() + mCity.getAreaName() + mCounty.getAreaName());
                        }
                    }
                });
                if (model == 1) {
                    task.execute(addressItem.getProvinceName(), addressItem.getCityName(), addressItem.getCountyName());
                } else {
                    task.execute();
                }
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEditName.getText().toString();
                String phone = mEditPhone.getText().toString();
                String address = mEditAddress.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
                    ToastUtils.showShort("信息填写不完整");
                } else {

                    ArrayMap<String, String> params = new ArrayMap<>();
                    params.put("linkName", name);
                    params.put("linkPhone", phone);
                    params.put("address", address);

                    if (mProvince != null && mCity != null && mCounty != null) {
                        params.put("provinceId", mProvince.getAreaId());
                        params.put("cityId", mCity.getAreaId());
                        params.put("countyId", mCounty.getAreaId());
                    } else if (addressItem != null) {
                        params.put("provinceId", addressItem.getProvinceId());
                        params.put("cityId", addressItem.getCityId());
                        params.put("countyId", addressItem.getCountyId());
                    }

                    params.put("userId", AppHolder.getInstance().user.getId());

                    mPresenter.addOrUpdateAddress(params);

                }


            }
        });
    }

    @Override
    public String setupToolBarTitle() {
        return "添加地址";
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
    public void addOrUpdateSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void addOrUpdateFailed(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }
}