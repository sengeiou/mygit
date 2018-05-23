package com.beanu.l4_clean.ui.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.ui.MainActivity;
import com.beanu.l4_clean.ui.shop.JIFenShopActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCenterActivity extends ToolBarActivity {

    @BindView(R.id.img_avatar) ImageView mImgAvatar;
    @BindView(R.id.txt_user_nickname) TextView mTxtUserNickname;
    @BindView(R.id.txt_user_sign) TextView mTxtUserSign;
    @BindView(R.id.txt_balance) TextView mTxtBalance;
    @BindView(R.id.img_anchor_tag) ImageView mImgAnchorTag;

    private int USER_INFO_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        ButterKnife.bind(this);

        User user = AppHolder.getInstance().user;
        uiRefreshUserInfo(user);
    }

    @OnClick({R.id.img_avatar, R.id.rl_item_recharge, R.id.rl_item_bill, R.id.rl_item_pk_records,
            R.id.rl_item_my_dolls})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.rl_item_recharge:
                startActivity(MyDollsActivity.class);

                break;
            case R.id.rl_item_bill:

                startActivity(RechargeActivity.class);
                break;
            case R.id.rl_item_pk_records:
                startActivity(JIFenShopActivity.class);
                break;
            case R.id.rl_item_my_dolls:
                startActivity(CouponActivity.class);
                break;
            case R.id.rl_logout:

                UIUtils.showAlertDialog(getSupportFragmentManager(), "提示", "确定要退出当前账号吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //清空内存数据
                        AppHolder.getInstance().reset();

                        Arad.preferences.putString(Constants.P_ACCOUNT, "");
                        Arad.preferences.putString(Constants.P_PWD, "");
                        Arad.preferences.putString(Constants.P_LOGIN_TYPE, "");
                        Arad.preferences.putString(Constants.P_LOGIN_OPENID, "");
                        Arad.preferences.putString(Constants.P_User_Id, "");
                        Arad.preferences.flush();

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
//            case R.id.item_feedback:
//                startActivity(FeedbackActivity.class);
//                break;
//            case R.id.item_share:
//                startActivity(ShareActivity.class);
//                break;
//            case R.id.item_collaboration:
//                WebActivity.startActivity(this, Constants.COO_URL + "/cooperation.html", "商务合作");
//                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MainActivity.EXIT_CODE) {
            setResult(RESULT_OK);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        uiRefreshUserInfo(AppHolder.getInstance().user);
    }

    private void uiRefreshUserInfo(User user) {

        if (user != null) {
            Glide.with(this)
                    .load(user.getIcon())
                    .apply(new RequestOptions().placeholder(R.drawable.base_head_default).error(R.drawable.base_head_default).circleCrop())
                    .into(mImgAvatar);

            mTxtUserNickname.setText(user.getNickname());
            String motto = TextUtils.isEmpty(user.getMotto()) ? "这个人好懒哦，什么都没有写~" : user.getMotto();
            mTxtUserSign.setText(motto);
            mTxtBalance.setText(user.getCoins() + "");

            if (user.getType() == 1) {
                mImgAnchorTag.setVisibility(View.VISIBLE);
            } else {
                mImgAnchorTag.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton1) {
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserCenterActivity.this, MyRecordListActivity.class);
                startActivityForResult(intent, MainActivity.EXIT_CODE);

            }
        });
        return true;
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
}
