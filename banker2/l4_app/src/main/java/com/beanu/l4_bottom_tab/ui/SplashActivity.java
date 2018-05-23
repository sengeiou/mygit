package com.beanu.l4_bottom_tab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.beanu.l3_common.util.Constants;

/**
 * APP 秒开
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, StartActivity.class);

        if (getIntent().getBundleExtra(Constants.EXTRA_BUNDLE) != null) {
            intent.putExtra(Constants.EXTRA_BUNDLE, getIntent().getBundleExtra(Constants.EXTRA_BUNDLE));
        }
        startActivity(intent);

        finish();
        overridePendingTransition(0, 0);
    }

}
