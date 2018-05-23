package com.beanu.l4_clean.ui.pk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.model.bean.PKMatchDetail;
import com.beanu.l4_clean.mvp.contract.PKGameContract;
import com.beanu.l4_clean.mvp.model.PKGameModelImpl;
import com.beanu.l4_clean.mvp.presenter.PKGamePresenterImpl;
import com.beanu.l4_clean.support.dialog.CountDownDialogFragment;
import com.beanu.l4_clean.support.dialog.NeutralDialogFragment;
import com.beanu.l4_clean.support.dialog.PKRestDialogFragment;
import com.beanu.l4_clean.support.dialog.PKResultDialogFragment;
import com.beanu.l4_clean.support.mina.manager.ClientConnectManager;
import com.beanu.l4_clean.support.service.BGMService;
import com.beanu.l4_clean.util.RongChatRoomUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.tee3.avd.VideoRenderer;

/**
 * PK页面
 */
public class PKGameActivity extends ToolBarActivity<PKGamePresenterImpl, PKGameModelImpl> implements PKGameContract.View {

    @BindView(R.id.PLVideoView_ME) FrameLayout mRemoteWindowA;
    @BindView(R.id.RemoteGLSurfaceViewA) GLSurfaceView mGLSurfaceViewA;

    @BindView(R.id.RemoteWindowB) FrameLayout mRemoteWindowB;
    @BindView(R.id.RemoteGLSurfaceViewB) GLSurfaceView mGLSurfaceViewB;

    @BindView(R.id.img_switch) ImageView mImgSwitch;
    @BindView(R.id.img_back) ImageButton mImgBack;
    @BindView(R.id.PLVideoView_Other) PLVideoTextureView mPLVideoViewOther;
    @BindView(R.id.img_avatar_other) ImageView mImgAvatarOther;
    @BindView(R.id.txt_status) TextView mTxtStatus;
    @BindView(R.id.txt_remaining_time) TextView mTxtRemainingTime;
    @BindView(R.id.rl_control2) ConstraintLayout mRlControl2;
    @BindView(R.id.btn_continue) Button mBtnContinue;
    @BindView(R.id.txt_price) TextView mTxtPrice;
    @BindView(R.id.txt_balance) TextView mTxtBalance;
    @BindView(R.id.layout_bottom) ConstraintLayout mLayoutBottom;
    @BindView(R.id.btn_up) ImageButton mBtnUp;
    @BindView(R.id.btn_left) ImageButton mBtnLeft;
    @BindView(R.id.btn_right) ImageButton mBtnRight;
    @BindView(R.id.btn_down) ImageButton mBtnDown;
    @BindView(R.id.btn_go) ImageButton mBtnGo;
    @BindView(R.id.txt_times_other) TextView mTxtTimesOther;
    @BindView(R.id.txt_times_me) TextView mTxtTimesMe;
    @BindView(R.id.rl_continue) RelativeLayout mRlContinue;
    @BindView(R.id.txt_other_win) TextView mTxtOtherWin;
    @BindView(R.id.img_music_switch) ImageView mImgMusicSwitch;
    @BindView(R.id.img_loading) ImageView mImgLoading;


    private static final int FLIP_MSG = 101;

    private String matchId;
    private int crawTimes = 1;//当前抓取的次数

    private int mMatchResult = -1; //比赛结果  0 失败 1赢
    private boolean cannotQuit = true;
    boolean isMusicOff = false;


    //游戏进行中25s倒计时
    CountDownTimer mCountDownTimer = new CountDownTimer(25 * 1000, 1000) {

        public void onTick(long millisUntilFinished) {
            long remaining = millisUntilFinished / 1000;
            mTxtRemainingTime.setText(remaining + "s");
        }

        public void onFinish() {
            //倒计时结束
            mPresenter.machineGo();
        }
    };

    //休息30s倒计时
    CountDownTimer mRestTimer = new CountDownTimer(30 * 1000, 1000) {

        public void onTick(long millisUntilFinished) {
            long remaining = millisUntilFinished / 1000;
            mBtnContinue.setText(remaining + "s");
            cannotQuit = false;
        }

        public void onFinish() {
            //倒计时结束
            restartPK();
            cannotQuit = true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pkgame);
        ButterKnife.bind(this);

        disableSlideBack();

        mPresenter.initPlayVideo();

        matchId = getIntent().getStringExtra("id");
        mPresenter.getChatRoomInfo(matchId);
        mPresenter.pkMatchDetail(matchId);

        roateRomoteView(mPresenter.getRTCVideoWindowA().b());
        roateRomoteView(mPresenter.getRTCVideoWindowB().b());

        //停止音乐服务
        Intent ser = new Intent(this, BGMService.class);
        startService(ser);

        Arad.bus.register(this);

        //倒计时dialog
        CountDownDialogFragment.show(getSupportFragmentManager(), new CountDownDialogFragment.Listener() {
            @Override
            public void onFinish() {
                mTxtStatus.setVisibility(View.GONE);
                mRlControl2.setVisibility(View.VISIBLE);

                //启动TCP
                ClientConnectManager.getInstance().init(PKGameActivity.this);
                ClientConnectManager.getInstance().connect(PKGameActivity.this);

            }
        });
    }

    public static void startActivity(Context context, String matchId) {
        Intent intent = new Intent(context, PKGameActivity.class);
        intent.putExtra("id", matchId);
        context.startActivity(intent);
    }

    @Override
    public void uiInit(final PKMatchDetail detail) {

        if (mPresenter.getFriendList() != null && mPresenter.getFriendList().size() == 2) {
            Friend friend = mPresenter.getFriendList().get(0);
            Friend friend1 = mPresenter.getFriendList().get(1);

            if (friend.getId().equals(AppHolder.getInstance().user.getId())) {

                Glide.with(this).load(friend1.getIcon()).apply(RequestOptions.circleCropTransform())
                        .into(mImgAvatarOther);

            } else {
                Glide.with(this).load(friend.getIcon()).apply(RequestOptions.circleCropTransform())
                        .into(mImgAvatarOther);
            }

        }
    }

    @Override
    public RTCVideoWindow createRTCVideoWindowA() {
        return new RTCVideoWindow(mRemoteWindowA, mGLSurfaceViewA);
    }

    @Override
    public RTCVideoWindow createRTCVideoWindowB() {
        return new RTCVideoWindow(mRemoteWindowB, mGLSurfaceViewB);
    }


    @Override
    public PLVideoTextureView findPLVideoOther() {
        return mPLVideoViewOther;
    }

    @Override
    public ImageView findLoadingView() {
        return mImgLoading;
    }

    @Override
    public void conferenceLoading(final boolean loading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loading) {
                    mImgLoading.setVisibility(View.VISIBLE);
                } else {
                    mImgLoading.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void activityFinish() {
        finish();
    }

    @Override
    public void uiUserBalance() {

        int price;
        if (mPresenter.getPKMatchDetail().getUserId().equals(AppHolder.getInstance().user.getId())) {
            price = mPresenter.getPKMatchDetail().getPrice();
        } else {
            price = mPresenter.getPKMatchDetail().getOpp_price();
        }
        int balance = AppHolder.getInstance().user.getCoins();

        mTxtPrice.setText(String.format("每次：%s币", price));
        mTxtBalance.setText(String.format("余额：%s币", balance));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.SocketConnect socketConnect) {
        if (socketConnect.success) {

            mPresenter.machineStart();

            //设置操作按钮可用
            mBtnUp.setClickable(true);
            mBtnDown.setClickable(true);
            mBtnLeft.setClickable(true);
            mBtnRight.setClickable(true);
            mBtnGo.setClickable(true);

            //开始倒计时
            mCountDownTimer.start();

        }
    }

    /**
     * 当前局结束及结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomFormServer chatRoom) {

        HashMap<String, String> map = chatRoom.message;
        String userId = map.get("userId");
        String icon = map.get("icon");
        String nickName = map.get("nickName");
        String result = map.get("result");

        if ("SPK1".equals(chatRoom.extra)) {

            if (userId.equals(AppHolder.getInstance().user.getId())) {

                //本次抓娃娃结束
                crawTimes++;
                mTxtTimesMe.setText("第" + crawTimes + "次");

                RongChatRoomUtil.sendChatRoomMessage(matchId, crawTimes + "", "CPK4", null);

                //显示本次结果
                if (mMatchResult == 0) {
                    pkLostDelay();
                } else {

                    if (result.equals("1")) {
                        mMatchResult = 1;
                        pkSuccess();
                    } else {
                        crawLost();
                    }
                }


            } else {

                if (result.equals("1") && mMatchResult != 1) {
                    mMatchResult = 0;
                    pkLost();
                }

            }


        }


    }


    /**
     * 对方 抓了多少次了
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomPKTimes chatRoomPKTimes) {
        mTxtTimesOther.setText("第" + chatRoomPKTimes.times + "次");
        mTxtTimesOther.setTag(chatRoomPKTimes.times);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Arad.bus.unregister(this);

        //停止TCP连接
//        SessionManager.getInstance().closeSession();
//        SessionManager.getInstance().removeSession();

        //停止音乐服务
        Intent ser = new Intent(this, BGMService.class);
        stopService(ser);

        mPresenter.pkMatchOver(matchId);
        mPresenter.quitChatRoom(matchId);

        //停止播放
        if (mPLVideoViewOther != null && mPLVideoViewOther.isPlaying()) {
            mPLVideoViewOther.stopPlayback();
            mPLVideoViewOther = null;
        }
    }


    /**
     * PK成功
     */
    private void pkSuccess() {

        mCountDownTimer.cancel();
        mRestTimer.cancel();


        int otherTimes = 1;
        try {
            otherTimes = Integer.parseInt((String) mTxtTimesOther.getTag());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Friend other = findOther(mPresenter.getFriendList());
        if (other != null) {
            PKResultDialogFragment.show(true, crawTimes, otherTimes, other.getIcon(), other.getNickname(), new PKResultDialogFragment.Listener() {
                @Override
                public void onClickLeft(View view) {
                    finish();
                }

                @Override
                public void onClickRight(View view) {

                }
            }, getSupportFragmentManager());
        }

    }

    /**
     * PK失败
     */
    private void pkLost() {

        mRestTimer.cancel();
        mTxtOtherWin.setVisibility(View.VISIBLE);

        if (mPresenter.isGrap()) {
            pkLostDelay();
        }
    }

    /**
     * PK失败  延时提示
     */
    private void pkLostDelay() {
        int otherTimes = 1;
        try {
            otherTimes = Integer.parseInt((String) mTxtTimesOther.getTag());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Friend other = findOther(mPresenter.getFriendList());
        if (other != null) {

            Fragment dialogFragment = getSupportFragmentManager().findFragmentByTag("PKResultDialog");
            if (dialogFragment == null) {
                PKResultDialogFragment.show(false, crawTimes, otherTimes, other.getIcon(), other.getNickname(), new PKResultDialogFragment.Listener() {
                    @Override
                    public void onClickLeft(View view) {
                        finish();
                    }

                    @Override
                    public void onClickRight(View view) {

                    }
                }, getSupportFragmentManager());
            }
        }
    }

    /**
     * 当前这次 没有抓中
     */
    private void crawLost() {

        int price;
        if (mPresenter.getPKMatchDetail().getUserId().equals(AppHolder.getInstance().user.getId())) {
            price = mPresenter.getPKMatchDetail().getPrice();
        } else {
            price = mPresenter.getPKMatchDetail().getOpp_price();
        }
        int balance = AppHolder.getInstance().user.getCoins();

        PKRestDialogFragment.show(getSupportFragmentManager(), price, balance, new PKRestDialogFragment.Listener() {
            @Override
            public void onClickLeft(View view) {

                //休息一会
                mRlContinue.setVisibility(View.VISIBLE);
                mRlControl2.setVisibility(View.GONE);

                mRestTimer.start();

            }

            @Override
            public void onClickRight(View view) {
                //继续抓
                restartPK();
            }

            @Override
            public void onFinish() {
                restartPK();
            }
        });
    }

    /**
     * 当前这次抓中了
     */
    private void crawSuccess() {

    }

    /**
     * 重新开始 下一轮
     */
    private void restartPK() {

        mRlContinue.setVisibility(View.GONE);
        mRlControl2.setVisibility(View.VISIBLE);

        mPresenter.machineStart();
        mCountDownTimer.start();

        if (mPresenter.getPKMatchDetail().getUserId().equals(AppHolder.getInstance().user.getId())) {
            mPresenter.pkMatchGoOn(mPresenter.getPKMatchDetail().getDollMaId(), matchId);
        } else {
            mPresenter.pkMatchGoOn(mPresenter.getPKMatchDetail().getOppDollMaId(), matchId);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FLIP_MSG) {

                int action = msg.arg1;
                switch (action) {
                    case 0:
                        mPresenter.machineDown();
                        break;
                    case 1:
                        mPresenter.machineUp();
                        break;
                    case 2:
                        mPresenter.machineLeft();
                        break;
                    case 3:
                        mPresenter.machineRight();
                        break;
                }

                msg = obtainMessage(FLIP_MSG);
                msg.arg1 = action;
                sendMessageDelayed(msg, 300);//关键处

            }

        }
    };

    private void sendMessage(MotionEvent event, int action) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Message msg = mHandler.obtainMessage(FLIP_MSG);
            msg.arg1 = action;
            mHandler.sendMessage(msg);

        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            mHandler.removeMessages(FLIP_MSG);
        }
    }

    /**
     * 方向键控制
     */
    @OnTouch({R.id.btn_up, R.id.btn_down, R.id.btn_left, R.id.btn_right})
    public boolean onViewTouch(View view, MotionEvent event) {

        switch (view.getId()) {
            case R.id.btn_down:
                sendMessage(event, 0);
                break;
            case R.id.btn_up:
                sendMessage(event, 1);
                break;
            case R.id.btn_left:
                sendMessage(event, 2);
                break;
            case R.id.btn_right:
                sendMessage(event, 3);
                break;
        }

        return false;
    }

    @OnClick({R.id.img_back, R.id.img_music_switch, R.id.btn_continue, R.id.btn_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                if (cannotQuit) {
                    showQuitTipsDialog();
                } else {

                    UIUtils.showAlertDialog(getSupportFragmentManager(), "提示", "确定要退出比赛吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });

                }

                break;
            case R.id.img_music_switch:

                if (!isMusicOff) {
                    mImgMusicSwitch.setImageResource(R.drawable.music_no);
                    //停止音乐服务
                    Intent ser = new Intent(this, BGMService.class);
                    stopService(ser);
                    isMusicOff = true;
                } else {

                    mImgMusicSwitch.setImageResource(R.drawable.music);
                    //停止音乐服务
                    Intent ser = new Intent(this, BGMService.class);
                    startService(ser);

                    isMusicOff = false;
                }

                break;
            case R.id.btn_continue:
                mRestTimer.cancel();
                restartPK();
                break;
            case R.id.btn_go:
                mPresenter.machineGo();

                if (mPresenter.getPKMatchDetail() != null) {
                    mCountDownTimer.cancel();
                }
                break;
        }
    }


    private Friend findOther(List<Friend> friendList) {
        if (friendList != null && friendList.size() == 2) {
            Friend friend = friendList.get(0);
            Friend friend1 = friendList.get(1);

            if (friend.getId().equals(AppHolder.getInstance().user.getId())) {
                return friend1;
            } else {
                return friend;
            }
        }
        return null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (cannotQuit) {
                showQuitTipsDialog();
                return false;
            } else {

                UIUtils.showAlertDialog(getSupportFragmentManager(), "提示", "确定要退出比赛吗？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        onBackPressed();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

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


    private void roateRomoteView(VideoRenderer videoRenderer) {

//        VideoRenderer videoRenderer = mPresenter.getRTCVideoWindowA().b();
        videoRenderer.setScalingType(VideoRenderer.ScalingType.Scale_Fill);
        Class myObjectClass = videoRenderer.getClass();
        try {
            Field fieldRendView = myObjectClass.getDeclaredField("glsurfaceViewRenderer");
            fieldRendView.setAccessible(true);
            Object videoClass = fieldRendView.get(videoRenderer);

            Field fieldRender = videoClass.getClass().getDeclaredField("yuvImageRenderer");
            fieldRender.setAccessible(true);
            Object render = fieldRender.get(videoClass);

            Method method = render.getClass().getDeclaredMethod("onRotation", int.class);
            method.setAccessible(true);
            method.invoke(render, 270);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}