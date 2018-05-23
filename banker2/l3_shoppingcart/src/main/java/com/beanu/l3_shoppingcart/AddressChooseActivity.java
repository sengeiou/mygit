package com.beanu.l3_shoppingcart;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_shoppingcart.adapter.AddressAdapter;
import com.beanu.l3_shoppingcart.mvp.contract.AddressContract;
import com.beanu.l3_shoppingcart.mvp.model.AddressModelImpl;
import com.beanu.l3_shoppingcart.mvp.presenter.AddressPresenterImpl;

/**
 * 选择收货地址 / 我的地址
 */
public class AddressChooseActivity extends ToolBarActivity<AddressPresenterImpl, AddressModelImpl> implements AddressContract.View, View.OnClickListener, AddressAdapter.OnAddressListener {

    private static final int REQUEST_ADD = 0;
    private static final int REQUEST_EDIT = 1;
    AddressAdapter mAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity_address_choose);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.cart_address_recycleview);
        TextView mTxtAddressAdd = (TextView) findViewById(R.id.cart_txt_address_add);
        mTxtAddressAdd.setOnClickListener(this);

        mAddressAdapter = new AddressAdapter(this, mPresenter.getAddressItemList(), this);
        mRecyclerView.setAdapter(mAddressAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.base_line).build());

        //请求地址列表
        mPresenter.addressList();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cart_txt_address_add) {
            Intent intent = new Intent(this, AddressAddOrUpdateActivity.class);
            intent.putExtra("model", 0);//添加
            startActivityForResult(intent, REQUEST_ADD);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD:
                    mPresenter.addressList();
                    break;
                case REQUEST_EDIT:
                    mPresenter.addressList();
                    break;
            }
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "选择地址";
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

//    @Override
//    protected void setStatusBar() {
//        StatusBarUtil.setTransparentForImageView(this, null);
//
//        //设置toolbar的低版本的高度
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            ViewGroup.LayoutParams layoutParams = getToolbar().getLayoutParams();
//            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.toolbar);
//            getToolbar().setLayoutParams(layoutParams);
//        }
//    }

    @Override
    public void refreshAddressList() {
        mAddressAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshDeleteAddress(int position) {
        mAddressAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshDefaultAddress(int position) {
        mAddressAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteAddress(int position) {
        mPresenter.addressDelete(position);
    }

    @Override
    public void editAddress(int position) {
        if (mPresenter.getAddressItemList() == null || mPresenter.getAddressItemList().isEmpty()) {
            return;
        }
        Intent intent = new Intent(this, AddressAddOrUpdateActivity.class);
        intent.putExtra("model", 1);//编辑
        intent.putExtra("address", mPresenter.getAddressItemList().get(position));
        startActivityForResult(intent, REQUEST_EDIT);
    }

    @Override
    public void chooseAddress(int position) {
        Intent intent = getIntent();
        intent.putExtra("address", mPresenter.getAddressItemList().get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void defaultAddress(int position) {
        mPresenter.addressDefault(position);
    }
}
