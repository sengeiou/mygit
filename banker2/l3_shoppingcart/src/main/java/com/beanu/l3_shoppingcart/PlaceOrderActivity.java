package com.beanu.l3_shoppingcart;

import android.os.Bundle;
import android.view.View;

import com.beanu.arad.base.ToolBarActivity;

/**
 * 下订单页面
 */
public class PlaceOrderActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity_place_order);

        PlaceOrderActivityFragment fragment = PlaceOrderActivityFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment, "placeOrder").commit();
    }

    @Override
    public String setupToolBarTitle() {
        return "下单";
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
}
