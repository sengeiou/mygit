package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.updateversion.UpdateChecker;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_shoppingcart.AddressChooseActivity;
import com.beanu.l4_clean.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends ToolBarActivity {

    @BindView(R.id.txt_version)
    TextView mTxtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        mTxtVersion.setText(String.format("当前版本 V%s", Arad.app.deviceInfo.getVersionName()));
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
    public String setupToolBarTitle() {
        return "设置";
    }

    @OnClick({R.id.rl_address, R.id.rl_check_update, R.id.rl_about, R.id.rl_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_address:
                startActivity(AddressChooseActivity.class);
                break;
            case R.id.rl_check_update:

                String version = AppHolder.getInstance().mConfig.getApkversion();
                if (!TextUtils.isEmpty(version)) {
                    if (Integer.parseInt(version) > 0) {
                        UpdateChecker.checkForDialog(SettingActivity.this, AppHolder.getInstance().mConfig.getDetail(), AppHolder.getInstance().mConfig.getApkurl(), Integer.parseInt(version));
                    }
                }

                break;

            case R.id.rl_about:

                startActivity(AboutActivity.class);
                break;
            case R.id.rl_clear:

                break;
        }
    }

}
