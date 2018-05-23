package com.beanu.l4_clean.ui.anchor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.recyclerview.divider.VerticalDividerItemDecoration;
import com.beanu.arad.utils.ScreenUtils;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.OnlinePeopleAvatarAdapter;
import com.beanu.l4_clean.model.bean.LiveRoom2Audience;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.mvp.contract.AnchorPlayContract;
import com.beanu.l4_clean.mvp.model.AnchorPlayModelImpl;
import com.beanu.l4_clean.mvp.presenter.AnchorPlayPresenterImpl;
import com.beanu.l4_clean.support.dialog.HelpGrabFragment;
import com.beanu.l4_clean.support.dialog.NeutralDialogFragment;
import com.beanu.l4_clean.ui.user.RechargeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主播播放页面
 */
public class AnchorPlayActivity extends ToolBarActivity<AnchorPlayPresenterImpl, AnchorPlayModelImpl> implements AnchorLivingInputFragment.OnFragmentInteractionListener,
        AnchorPlayContract.View, PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener {

    @BindView(R.id.frame_layout) FrameLayout mFrameLayout;
    @BindView(R.id.PLVideoView) PLVideoTextureView mPLVideoView;
    @BindView(R.id.img_back) ImageView mImgBack;
    @BindView(R.id.img_crawer_avatar) ImageView mImgCrawerAvatar;
    @BindView(R.id.txt_crawer_name) TextView mTxtCrawerName;
    @BindView(R.id.txt_crawer_status) TextView mTxtCrawerStatus;
    @BindView(R.id.recycle_view_online) RecyclerView mRecycleViewOnline;
    @BindView(R.id.toolbar_right_people) TextView mPeopleOnline;
    @BindView(R.id.txt_chat_hit) TextView mTxtChatHit;
    @BindView(R.id.img_chat_avatar) ImageView mImgChatAvatar;
    @BindView(R.id.img_help_bg) ImageView mImgHelpBg;
    @BindView(R.id.img_help_avatar) ImageView mImgHelpAvatar;
    @BindView(R.id.txt_help_name) TextView mTxtHelpName;

    String chatRoomId;
    String logId;
    LiveRoom2Audience mLiveRoom2Audience;

    //在线人数
    OnlinePeopleAvatarAdapter mOnlinePeopleAvatarAdapter;
    private List<OnlinePeople> mOnlinePeopleList;


    public static void startActivity(Context context, String chatRoomId, String logId) {
        Intent intent = new Intent(context, AnchorPlayActivity.class);
        intent.putExtra("chatRoomId", chatRoomId);
        intent.putExtra("logId", logId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor_play);
        ButterKnife.bind(this);

        AnchorLivingInputFragment fragment = AnchorLivingInputFragment.newInstance("", "");
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment, "fragment").commit();

        //初始化直播
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        mPLVideoView.setAVOptions(options);
        mPLVideoView.setOnPreparedListener(this);
        mPLVideoView.setOnInfoListener(this);
        mPLVideoView.setOnCompletionListener(this);
        mPLVideoView.setOnVideoSizeChangedListener(this);
        mPLVideoView.setOnErrorListener(this);
        mPLVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        mPLVideoView.setDisplayOrientation(270);
//        View loadingView = findViewById(R.id.LoadingView);
//        mVideoView.setBufferingIndicator(loadingView);

        //获取机器详情
        chatRoomId = getIntent().getStringExtra("chatRoomId");
        logId = getIntent().getStringExtra("logId");
        mPresenter.audienceLiveRoom(chatRoomId, logId);


        //在线人数
        mOnlinePeopleList = new ArrayList<>();
        mOnlinePeopleAvatarAdapter = new OnlinePeopleAvatarAdapter(this, mOnlinePeopleList);
        mRecycleViewOnline.setAdapter(mOnlinePeopleAvatarAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecycleViewOnline.setLayoutManager(linearLayoutManager);
        mRecycleViewOnline.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());


        //返回键
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Arad.bus.register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);

        //退出聊天室
        mPresenter.quitChatRoom(chatRoomId);

        //停止播放
        if (mPLVideoView != null && mPLVideoView.isPlaying()) {
            mPLVideoView.stopPlayback();
            mPLVideoView = null;
        }

    }

    @Override
    public void initUI(LiveRoom2Audience liveRoom) {

        mLiveRoom2Audience = liveRoom;

        //初始化直播(某些机器需要延迟一下)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPLVideoView.setVideoPath(mLiveRoom2Audience.getAnchorLiveFlow());
                mPLVideoView.start();
            }
        }, 100);


        //当前抓娃娃的主播
        if (!TextUtils.isEmpty(liveRoom.getAnchorIcon())) {
            Glide.with(this).load(liveRoom.getAnchorIcon()).apply(RequestOptions.circleCropTransform()).into(mImgCrawerAvatar);
        }
        mTxtCrawerName.setText(liveRoom.getAnchorNickName());
        mTxtCrawerStatus.setText("抓中" + liveRoom.getSuccessNum() + "次");

    }

    @Override
    public void refreshOnlinePeople(List<OnlinePeople> onlinePeopleList, int amount) {
        mOnlinePeopleList.clear();
        mOnlinePeopleList.addAll(onlinePeopleList);
        mOnlinePeopleAvatarAdapter.notifyDataSetChanged();
        mPeopleOnline.setText(amount + "人");
    }

    @Override
    public void uiHelpMe(boolean success) {
        if (success) {
            showPeopleHelpTipAnim(AppHolder.getInstance().user.getNickname() + "申请了抓娃娃", AppHolder.getInstance().user.getIcon());
        } else {
            ToastUtils.showShort("提交失败");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomInfoUpdate chatRoom) {
        if ("C0".equals(chatRoom.extra) || "C1".equals(chatRoom.extra)) {
            mPresenter.getChatRoomInfo(chatRoomId);
        }

        if ("C0".equals(chatRoom.extra)) {
            showPeopleHelpTipAnim(chatRoom.userName + "进来了", chatRoom.userIcon);
        }

        //申请抓娃娃
        if ("CZB0".equals(chatRoom.extra)) {
            showPeopleHelpTipAnim(chatRoom.userName + "申请了抓娃娃", chatRoom.userIcon);
        }

        //主播 接受了谁的请求
        if ("CZB1".equals(chatRoom.extra)) {
            showHelperUser(chatRoom.userName, chatRoom.userIcon);
        }

        //主播下线
        if ("CZB2".equals(chatRoom.extra)) {
            NeutralDialogFragment dialogFragment = new NeutralDialogFragment();
            dialogFragment.show("下线提醒", "当前主播已经下线", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }, getSupportFragmentManager());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomFormServer chatRoom) {

        if ("S0".equals(chatRoom.extra)) {
            //某个玩家正在玩

        } else if ("S1".equals(chatRoom.extra)) {
            //当前机器结束了
            hideHelperUser();
        }
    }


    @Override
    public void onSendMessage(String message) {
        mPresenter.selfSendMessage(message);
    }

    @Override
    public void onHelpMe() {


        HelpGrabFragment.show(getSupportFragmentManager(), "19", new HelpGrabFragment.Listener() {
            @Override
            public void onClickOK(View view) {
                mPresenter.anchorHelpMe(logId);
            }

            @Override
            public void onClickCharge(View view) {
                startActivity(RechargeActivity.class);
            }
        });

    }

    @Override
    public void onShare() {

    }

    @Override
    protected void setStatusBar() {

    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {

    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        return false;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {

    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {

    }

    private void showPeopleHelpTipAnim(String title, String headerUrl) {

        mTxtChatHit.setVisibility(View.VISIBLE);
        mImgChatAvatar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(headerUrl)) {
            Glide.with(this).load(headerUrl).apply(RequestOptions.circleCropTransform()).into(mImgChatAvatar);
        }

        int width = ScreenUtils.getScreenWidth();

        mTxtChatHit.setX(width);
        mImgChatAvatar.setX(width);

        mTxtChatHit.setText(title);


        int transX = width - 100;
        mTxtChatHit.animate().translationXBy(-transX).setDuration(1000).start();
        mImgChatAvatar.animate().translationXBy(-transX).setDuration(1000).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTxtChatHit.setVisibility(View.GONE);
                mImgChatAvatar.setVisibility(View.GONE);
            }
        }, 2000);
    }


    private void showHelperUser(String name, String icon) {

        if (!TextUtils.isEmpty(icon)) {
            Glide.with(this).load(icon).apply(RequestOptions.circleCropTransform()).into(mImgHelpAvatar);
        }
        mTxtHelpName.setText(name);

        mImgHelpBg.setVisibility(View.VISIBLE);
        mImgHelpAvatar.setVisibility(View.VISIBLE);
        mTxtHelpName.setVisibility(View.VISIBLE);
    }


    private void hideHelperUser() {
        mImgHelpBg.setVisibility(View.GONE);
        mImgHelpAvatar.setVisibility(View.GONE);
        mTxtHelpName.setVisibility(View.GONE);
    }

}