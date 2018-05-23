package com.beanu.l4_clean.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Address;
import com.beanu.l4_clean.util.AddressPickTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

@Route(path = "/user/address")
public class SetAddressActivity extends ToolBarActivity {

    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_city)
    TextView mEditCity;
    @BindView(R.id.edit_address)
    EditText mEditAddress;
    private Province mProvince;
    private City mCity;
    private County mCounty;

    public static void startActivity(Activity context, Address address, int requestCode) {
        Intent intent = new Intent(context, SetAddressActivity.class);
        if (address != null) {
            intent.putExtra("address", address);
        }
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, SetAddressActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);
        ButterKnife.bind(this);

        Address address = getIntent().getParcelableExtra("address");
        if (address != null) {
            mEditName.setText(address.name);
            mEditPhone.setText(address.phone);
            mEditCity.setText(address.city);
            mEditAddress.setText(address.address);
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
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @OnClick({R.id.edit_city, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_city:
                showPicker();
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    private void save() {
        String name = mEditName.getText().toString().trim();
        String phone = mEditPhone.getText().toString().trim();
        String address = mEditAddress.getText().toString();
        String city = mEditCity.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("请填写收货人");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请填写手机号");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort("请填写详细地址");
            return;
        }
        if (TextUtils.isEmpty(city)) {
            ToastUtils.showShort("请选择地址");
            return;
        }

        Address address1 = new Address(name, phone, city, address);
        Intent intent = new Intent();
        intent.putExtra("address", address1);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showPicker() {
        AddressPickTask task = new AddressPickTask(this);
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
                    mEditCity.setText(mProvince.getAreaName() + mCity.getAreaName() + mCounty.getAreaName());
                }
            }
        });
        if (mProvince != null && mCity != null && mCounty != null) {
            task.execute(mProvince.getAreaName(), mCity.getAreaName(), mCounty.getAreaName());
        } else {
            task.execute();
        }

    }
}
