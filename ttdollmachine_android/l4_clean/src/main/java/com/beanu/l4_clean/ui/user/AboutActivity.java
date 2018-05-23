package com.beanu.l4_clean.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.ui.WebActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于界面
 */
public class AboutActivity extends BaseTTActivity {

    @BindView(R.id.txt_version) TextView mTxtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        mTxtVersion.setText(String.format("当前版本 V%s", Arad.app.deviceInfo.getVersionName()));

    }


    @OnClick({R.id.rl_score, R.id.rl_version_desc, R.id.rl_help, R.id.rl_feedback, R.id.rl_user_protcol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_score:

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.rl_version_desc:
                WebActivity.startActivity(this, AppHolder.getInstance().mConfig.getAboutUs(), "版本介绍");

                break;
            case R.id.rl_help:
                startActivity(HelpActivity.class);
                break;
            case R.id.rl_feedback:
                startActivity(FeedbackActivity.class);
                break;
            case R.id.rl_user_protcol:
                WebActivity.startActivity(this, AppHolder.getInstance().mConfig.getRegisterProtocol(), "用户协议");

                break;

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
        return "关于我们";
    }
}