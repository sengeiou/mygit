package com.beanu.l4_bottom_tab.ui.module5_my.info;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_shoppingcart.AddressChooseActivity;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.support.push.PushManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 账号设置
 */
public class AccountInfoActivity extends BaseSDActivity {

    @BindView(R.id.txt_second_title) TextView mTxtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        ButterKnife.bind(this);

        mTxtPhone.setText(AppHolder.getInstance().user.getMobile());
    }

    @Override
    public String setupToolBarTitle() {
        return "账号管理";
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

    @OnClick({R.id.rl_my_address, R.id.rl_change_password, R.id.rl_feed_back, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_my_address:
                Intent intent = new Intent(this, AddressChooseActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_change_password:
                startActivity(ChangePwdActivity.class);
                break;
            case R.id.rl_feed_back:
                startActivity(FeedbackActivity.class);
                break;
            case R.id.btn_exit:

                UIUtils.showAlertDialog(getSupportFragmentManager(), "提示", "确定要退出当前账号吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //清空内存数据
                        AppHolder.getInstance().reset();

                        Arad.preferences.putString(Constants.P_ACCOUNT, "");
                        Arad.preferences.putString(Constants.P_PWD, "");
                        Arad.preferences.putString(Constants.P_LOGIN_TYPE, "");
                        Arad.preferences.putString(Constants.P_LOGIN_OPENID, "");
                        Arad.preferences.flush();

                        //推送暂停
                        PushManager.pause(getApplicationContext());

                        setResult(RESULT_OK);
                        finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                break;
        }
    }
}