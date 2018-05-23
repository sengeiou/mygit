package com.beanu.l4_bottom_tab.ui.module4_book;

import android.os.Bundle;
import android.view.View;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

/**
 * 购物车 模块
 */
public class ShoppingCartActivity extends BaseSDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
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
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
