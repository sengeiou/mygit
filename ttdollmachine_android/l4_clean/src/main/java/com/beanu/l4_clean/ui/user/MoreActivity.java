package com.beanu.l4_clean.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.beanu.l4_clean.R;
import com.beanu.l3_common.base.BaseTTActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreActivity extends BaseTTActivity {

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }

    @OnClick({R.id.rl_item_recharge, R.id.rl_item_task, R.id.rl_item_notice, R.id.rl_item_invite, R.id.rl_item_daichong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_item_recharge:
                startActivity(RechargeActivity.class);
                break;
            case R.id.rl_item_task:
                startActivityForResult(TaskActivity.class, REQUEST_CODE);
                break;
            case R.id.rl_item_notice:
                startActivity(MyMessageActivity.class);
                break;
            case R.id.rl_item_invite:
                startActivity(ShareActivity.class);
                break;
            case R.id.rl_item_daichong:
                break;
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "更多";
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
    public boolean setupToolBarRightButton1(View rightButton1) {
        if (rightButton1 instanceof ImageView) {
            ((ImageView) rightButton1).setImageResource(R.drawable.a_set);
        }
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SettingActivity.class);
            }
        });

        return true;
    }
}