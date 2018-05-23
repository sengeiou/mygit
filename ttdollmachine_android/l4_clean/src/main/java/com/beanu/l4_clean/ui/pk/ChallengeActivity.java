package com.beanu.l4_clean.ui.pk;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.statusbar.ImmersionBar;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.PKMatch;
import com.beanu.l4_clean.mvp.contract.ChallengeContract;
import com.beanu.l4_clean.mvp.model.ChallengeModelImpl;
import com.beanu.l4_clean.mvp.presenter.ChallengePresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发起／接受挑战
 */
public class ChallengeActivity extends ToolBarActivity<ChallengePresenterImpl, ChallengeModelImpl> implements ChallengeContract.View {

    @BindView(R.id.edit_code) EditText mEditCode;
    @BindView(R.id.layout_inviter) ConstraintLayout mLayoutInviter;
    @BindView(R.id.img_pk_avatar) ImageView mImgPkAvatar;
    @BindView(R.id.txt_pk_username) TextView mTxtPkUsername;
    @BindView(R.id.txt_pk_info) TextView mTxtPkInfo;
    @BindView(R.id.btn_start) Button mBtnStart;
    @BindView(R.id.txt_code) TextView mTxtCode;
    @BindView(R.id.btn_invite) Button mBtnInvite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        ButterKnife.bind(this);
    }


    @Override
    public String setupToolBarTitle() {
        return "";
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
    protected void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar().init();
    }

    @OnClick({R.id.btn_invite, R.id.btn_ok, R.id.btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_invite:

                if (mPresenter.getPKMatch() != null) {
                    uiAcceptInvitation(mPresenter.getPKMatch().getId());
                }
//                } else {
//                    startActivity(InvitationActivity.class);
//                }

                break;
            case R.id.btn_ok:

                String code = mEditCode.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    mPresenter.acceptInvitation(code);
                }

                break;
            case R.id.btn_start:

                uiAcceptInvitation(mPresenter.getPKMatch().getId());

                break;
        }
    }


    @Override
    public void uiInit(PKMatch pkMatch) {

//        if (pkMatch.getIsHave() == 1) {
//            mBtnInvite.setText("进入我的邀请页面");
//        } else {
//            mBtnInvite.setText("立即邀请好友");
//        }

    }

    @Override
    public void uiAcceptInvitation(String chatRoomId) {
        PKChooseDollActivity.startActivity(this, chatRoomId, mPresenter.getPKMatch().getCode());
        finish();
    }

    @Override
    public void uiInvite(String code, String shareUrl) {
        //TODO

    }
}