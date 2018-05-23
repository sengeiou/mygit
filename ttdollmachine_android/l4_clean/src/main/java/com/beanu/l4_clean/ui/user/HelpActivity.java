package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.view.View;

import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.ui.WebActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 帮助中心
 */
public class HelpActivity extends BaseTTActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4, R.id.rl_5, R.id.txt_phone, R.id.txt_kefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_1:
                WebActivity.startActivity(this, Constants.URL + "helpDetails?alias=game_failure", "游戏故障");
                break;
            case R.id.rl_2:
                WebActivity.startActivity(this, Constants.URL + "helpDetails?alias=order_failure", "娃娃机订单问题");

                break;
            case R.id.rl_3:

                WebActivity.startActivity(this, Constants.URL + "helpDetails?alias=recharge_failure ", "充值问题");
                break;
            case R.id.rl_4:

                WebActivity.startActivity(this, Constants.URL + "helpDetails?alias=activity_failure", "活动问题");
                break;
            case R.id.rl_5:

                startActivity(FeedbackActivity.class);
                break;
            case R.id.txt_phone:

                break;
            case R.id.txt_kefu:

                break;
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "客服与帮助";
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
}