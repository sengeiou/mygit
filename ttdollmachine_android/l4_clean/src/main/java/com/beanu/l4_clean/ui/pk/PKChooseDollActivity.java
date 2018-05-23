package com.beanu.l4_clean.ui.pk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.arad.utils.statusbar.ImmersionBar;
import com.beanu.l2_shareutil.ShareDialogUtil;
import com.beanu.l2_shareutil.share.ShareListener;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.DollsMachineForPKAdapter;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.mvp.contract.PKChooseDollContract;
import com.beanu.l4_clean.mvp.model.PKChooseDollModelImpl;
import com.beanu.l4_clean.mvp.presenter.PKChooseDollPresenterImpl;
import com.beanu.l4_clean.support.dialog.NeutralDialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择PK娃娃页面
 */
public class PKChooseDollActivity extends ToolBarActivity<PKChooseDollPresenterImpl, PKChooseDollModelImpl> implements PKChooseDollContract.View {

    @BindView(R.id.img_avatar_me_pic) ImageView mImgAvatarMe;
    @BindView(R.id.img_avatar_other_pic) ImageView mImgAvatarOther;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.txt_status_me) TextView mTxtStatusMe;
    @BindView(R.id.txt_status_other) TextView mTxtStatusOther;
    @BindView(R.id.imageButton2) Button mBtnReady;
    @BindView(R.id.btn_ready) Button mBtnJoinReady;
    @BindView(R.id.img_vs) ImageView mImgVs;
    @BindView(R.id.constraint_layout) ConstraintLayout mLayout;
    private ConstraintSet mConstraintSet1, mConstraintSet2;

    DollsMachineForPKAdapter mDollsMachineForPKAdapter;
    List<Dolls> mDollsList;

    private String chatRoomId;
    String mChallengerAvatarPath;
    private boolean cannotQuit = false;
    private String mChallengerName;


    public static void startActivity(Context context, String chatRoomId, String code) {
        Intent intent = new Intent(context, PKChooseDollActivity.class);
        intent.putExtra("chatRoomId", chatRoomId);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pkchoose_doll);
        ButterKnife.bind(this);

        mConstraintSet1 = new ConstraintSet();
        mConstraintSet2 = new ConstraintSet();

        mConstraintSet1.clone(mLayout);
        mConstraintSet2.clone(this, R.layout.activity_pkchoose_doll_anim);

        chatRoomId = getIntent().getStringExtra("chatRoomId");

        mDollsList = new ArrayList<>();
        mDollsMachineForPKAdapter = new DollsMachineForPKAdapter(this, mDollsList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mDollsMachineForPKAdapter);
        mRecyclerView.addItemDecoration(new GridLayoutItemDecoration(getResources().getDimensionPixelSize(R.dimen.grid_space), 2));
        mRecyclerView.setNestedScrollingEnabled(false);
        mPresenter.getChatRoomInfo(chatRoomId);

        Arad.bus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);
    }

    @Override
    protected void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar().init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomPKInfoUpdate chatRoom) {

        if ("CPK0".equals(chatRoom.extra) || "CPK1".equals(chatRoom.extra)) {
            mChallengerAvatarPath = chatRoom.userIcon;
            mChallengerName = chatRoom.userName;

            mPresenter.getChatRoomInfo(chatRoomId);
        }

        if ("CPK2".equals(chatRoom.extra)) {
            mPresenter.setOppositeReady(true);//对方已准备
            mTxtStatusOther.setText("已准备");
        }

        if ("CPK3".equals(chatRoom.extra)) {
            //进入比赛
            uiGoToGame();
        }
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cannotQuit) {
                    showQuitTipsDialog();
                } else {
                    onBackPressed();
                }
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton2) {
        if (rightButton2 instanceof TextView) {
            String title = getIntent().getStringExtra("code");
            ((TextView) rightButton2).setText("我的邀请码：" + title);
        }
        return true;
    }

    @Override
    public boolean setupToolBarRightButton2(View rightButton1) {
        if (rightButton1 instanceof TextView) {
            final String code = getIntent().getStringExtra("code");
            ((TextView) rightButton1).setText("立即邀请");

            rightButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShareDialogUtil.
                            showShareDialog(PKChooseDollActivity.this, AppHolder.getInstance().user.getNickname() + "向你发起抓娃娃挑战，快来一战吧!", "", Constants.SHARE_URL + code
                                    , R.mipmap.ic_launcher, new ShareListener() {
                                        @Override
                                        public void shareSuccess() {

                                        }

                                        @Override
                                        public void shareFailure(Exception e) {

                                        }

                                        @Override
                                        public void shareCancel() {

                                        }
                                    });
                }
            });

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.quitChatRoom(chatRoomId);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (cannotQuit) {
                showQuitTipsDialog();
                return false;
            } else {
                onBackPressed();
                return true;
            }

        }

        return false;
    }

    private void showQuitTipsDialog() {
        NeutralDialogFragment dialogFragment = new NeutralDialogFragment();
        dialogFragment.show("提示", "当前游戏中，不能退出", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }, getSupportFragmentManager());
    }


    @Override
    public void uiRefreshAvatar(Friend friend, Friend friend2) {
        if (friend.getId().equals(AppHolder.getInstance().user.getId())) {
            Glide.with(PKChooseDollActivity.this).load(friend.getIcon()).apply(RequestOptions.circleCropTransform()).into(mImgAvatarMe);
            mTxtStatusMe.setText(friend.getNickname());
            mTxtStatusMe.setVisibility(View.VISIBLE);

            Glide.with(PKChooseDollActivity.this).load(friend2.getIcon()).apply(RequestOptions.circleCropTransform()).into(mImgAvatarOther);
            mTxtStatusOther.setText(friend2.getNickname());
            mTxtStatusOther.setVisibility(View.VISIBLE);

        } else {

            Glide.with(PKChooseDollActivity.this).load(friend2.getIcon()).apply(RequestOptions.circleCropTransform()).into(mImgAvatarMe);
            mTxtStatusMe.setText(friend2.getNickname());
            mTxtStatusMe.setVisibility(View.VISIBLE);

            Glide.with(PKChooseDollActivity.this).load(friend.getIcon()).apply(RequestOptions.circleCropTransform()).into(mImgAvatarOther);
            mTxtStatusOther.setText(friend.getNickname());
            mTxtStatusOther.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void uiRefreshChallenger(boolean show) {

        if (show) {
            mBtnJoinReady.setEnabled(true);
            if (!TextUtils.isEmpty(mChallengerAvatarPath)) {
                Glide.with(PKChooseDollActivity.this).load(mChallengerAvatarPath).apply(RequestOptions.circleCropTransform()).into(mImgAvatarOther);
            }
            if (!TextUtils.isEmpty(mChallengerName)) {
                mTxtStatusOther.setText(mChallengerName);
            }
            mTxtStatusOther.setVisibility(View.VISIBLE);
            cannotQuit = true;
        } else {
            cannotQuit = false;
            mBtnJoinReady.setEnabled(false);
        }

        Glide.with(PKChooseDollActivity.this).load(AppHolder.getInstance().user.getIcon()).apply(RequestOptions.circleCropTransform()).into(mImgAvatarMe);
        mTxtStatusMe.setText(AppHolder.getInstance().user.getNickname());
    }

    @Override
    public void uiRefreshRecycleView(List<Dolls> dollsList) {


        mDollsList.clear();
        mDollsList.addAll(dollsList);
        mDollsMachineForPKAdapter.setSelectedPos(-1);
        mDollsMachineForPKAdapter.notifyDataSetChanged();
    }

    @Override
    public void uiReady(boolean isOK) {
        if (isOK) {
            mBtnReady.setEnabled(false);
            mTxtStatusMe.setText("已准备");
            mTxtStatusMe.setVisibility(View.VISIBLE);
        } else {
            ToastUtils.showShort("机器人被抢占，请选择其他机器");
        }
    }

    @Override
    public void uiGoToGame() {

        PKGameActivity.startActivity(this, chatRoomId);
        finish();
    }

    @OnClick(R.id.imageButton2)
    public void onViewClicked() {

        if (mDollsMachineForPKAdapter.getSelectedPos() >= 0) {
            Dolls mDollSelected = mDollsList.get(mDollsMachineForPKAdapter.getSelectedPos());

            if (mDollSelected != null) {
                mPresenter.pkConfirmReady(mDollSelected.getId(), chatRoomId);
            }
        }

    }


    public void onClickReady(View view) {

        TransitionManager.beginDelayedTransition(mLayout);
        mConstraintSet2.applyTo(mLayout);

    }

    public void onClickChangeDolls(View view) {
        mPresenter.dollsList();
    }
}